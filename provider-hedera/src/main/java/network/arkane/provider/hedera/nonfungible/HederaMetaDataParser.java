package network.arkane.provider.hedera.nonfungible;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.ipfs.IpfsUtil;
import network.arkane.provider.nonfungible.MetaDataParser;
import network.arkane.provider.nonfungible.NonFungibleMetaData;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static network.arkane.provider.hedera.nonfungible.HederaNonfungibleGateway.NON_FUNGIBLE_UNIQUE;

public class HederaMetaDataParser {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final MetaDataParser metaDataParser;

    public HederaMetaDataParser(final Optional<CacheManager> cacheManager) {
        restTemplate = new RestTemplateBuilder()
                .defaultMessageConverters()
                .requestFactory(() -> {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    ConnectionPool okHttpConnectionPool = new ConnectionPool();
                    builder.connectionPool(okHttpConnectionPool);
                    builder.connectTimeout(5, TimeUnit.SECONDS);
                    builder.readTimeout(15, TimeUnit.SECONDS);
                    builder.retryOnConnectionFailure(true);
                    return new OkHttp3ClientHttpRequestFactory(builder.build());
                })
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        metaDataParser = new MetaDataParser(cacheManager);
    }

    public NonFungibleMetaData parseMetaData(String tokenId,
                                             Long serialNumber,
                                             String metadata) {
        metadata = IpfsUtil.replaceIpfsLink(metadata);
        if (metadata.startsWith("http")) {
            metadata = restTemplate.getForObject(metadata, String.class);
        } else {
            String decodedBase64 = decodeBase64(metadata).orElse(metadata);
            if (!StringUtils.equals(metadata, decodedBase64)) {
                if (decodedBase64.startsWith("http") || isValidJSON(decodedBase64)) {
                    return parseMetaData(tokenId, serialNumber, decodedBase64);
                } else {
                    return parseMetaData(tokenId, serialNumber, "ipfs://" + decodedBase64);
                }
            }
        }
        if (StringUtils.isBlank(metadata)) return null;
        String json = removeBOM(metadata);
        if (isValidJSON(json)) {
            return metaDataParser.parseMetaData(SecretType.HEDERA, String.valueOf(serialNumber), NON_FUNGIBLE_UNIQUE, tokenId, json);
        }
        return null;
    }

    private Optional<String> decodeBase64(String tokenUri) {
        final String base64Metadata = tokenUri.substring(tokenUri.indexOf(",") + 1);
        try {
            return org.apache.commons.codec.binary.Base64.isBase64(base64Metadata)
                   ? Optional.of(new String(Base64.getDecoder().decode(base64Metadata)))
                   : Optional.empty();
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }


    private String removeBOM(String input) {
        try {
            return IOUtils.toString(new BOMInputStream(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return input;
        }
    }

    private boolean isValidJSON(final String json) {
        boolean valid = true;
        try {
            objectMapper.readTree(json);
        } catch (IOException e) {
            valid = false;
        }
        return valid;
    }
}
