package network.arkane.provider;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockchainProviderTestHelper {

    private static final String BASIC_AUTH_PREFIX = "Basic";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    public static ResponseDefinitionBuilder jsonFromFile(final String fileName) {
        return okJson(readFile(fileName));
    }

    public static ResponseDefinitionBuilder jsonFromFile(final String fileName, HttpStatus httpStatus) {
        return aResponse().withBody(readFile(fileName)).withStatus(httpStatus.value());
    }

    public static String createBasicAuthHeaderValue(final String user,
                                                    final String password) {
        return "%s %s".formatted(BASIC_AUTH_PREFIX, base64Encode("%s:%s".formatted(user, password)));
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static void verifyRequestSentWithAuthorization(final String url,
                                                          final String user,
                                                          final String password
                                                         ) {
        final String expectedAuthHeader = createBasicAuthHeaderValue(user, password);
        verify(getRequestedFor(urlEqualTo(url))
                       .withHeader(AUTHORIZATION_HEADER, equalTo(expectedAuthHeader))
              );
    }

    @SneakyThrows
    private static String readFile(final String fileName) {
        final URL resource = BlockchainProviderTestHelper.class.getResource(fileName);
        return Files.readString(Paths.get(resource.toURI()));
    }

    private static String base64Encode(final String s) {
        return encodeBase64String(s.getBytes(StandardCharsets.UTF_8));
    }
}
