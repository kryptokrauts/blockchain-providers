package network.arkane.provider.nonfungible;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.animationtype.AnimationUrlParser;
import network.arkane.provider.nonfungible.animationtype.AnimationUrlParserFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class MetaDataParser {

    private ObjectMapper objectMapper;
    private Optional<CacheManager> cacheManager;
    private final AnimationUrlParser animationUrlParser;

    public MetaDataParser(
            final Optional<CacheManager> cacheManager) {
        this.cacheManager = cacheManager;
        this.animationUrlParser = AnimationUrlParserFactory.create();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @SneakyThrows
    public NonFungibleMetaData parseMetaData(SecretType secretType,
                                             String tokenId,
                                             String contractType,
                                             String contractAddress,
                                             String metaData) {
        String cacheKey = secretType + "-" + tokenId + "-" + contractType + "-" + contractAddress;
        if (this.cacheManager.isPresent()) {
            Cache cache = cacheManager.get().getCache("non-fungibles-meta-data");
            NonFungibleMetaData cachedResult = cache.get(cacheKey, NonFungibleMetaData.class);
            if (cachedResult != null) {
                return cachedResult;
            }
        }

        NonFungibleMetaData result = null;
        try {
            result = parseMetaData(objectMapper.readValue(metaData, JsonNode.class));
        } catch (IOException e) {
            log.error("Error parsing metadata for nonfungible", e);
        }

        if (this.cacheManager.isPresent()) {
            Cache cache = cacheManager.get().getCache("non-fungibles-meta-data");
            cache.put(cacheKey, result);
        }
        return result;
    }

    @SneakyThrows
    public NonFungibleMetaData parseMetaData(JsonNode metaData) {
        return NonFungibleMetaData.builder()
                                  .json(metaData)
                                  .objectMapper(objectMapper)
                                  .animationUrlParser(animationUrlParser)
                                  .build();

    }

}
