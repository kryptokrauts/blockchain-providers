package network.arkane.provider.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.provider.exceptions.ArkaneException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.util.Optional;

public class BlockchainProviderGatewayResponseErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper objectMapper;

    public BlockchainProviderGatewayResponseErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        final Optional<JsonNode> errorNode = Optional.ofNullable(objectMapper.readTree(response.getBody()))
                                                     .map(node -> node.get("errors"))
                                                     .filter(node -> !node.isEmpty())
                                                     .map(node -> node.get(0));
        final String code = errorNode.map(json -> json.get("errorCode").asText())
                                     .orElse("unknown-error");
        final String message = errorNode.map(json -> json.get("errorMessage").asText())
                                        .orElse("An unexpected error occurred: " + response.getStatusText());
        throw ArkaneException.arkaneException()
                             .errorCode(code)
                             .message(message)
                             .build();
    }
}
