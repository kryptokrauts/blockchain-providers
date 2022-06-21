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
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        final String message = Optional.ofNullable(jsonNode)
                .map(json -> json.get("description"))
                .map(JsonNode::asText)
                .orElse("Unknown error");
        throw ArkaneException.arkaneException()
                .errorCode("transaction.submit.internal-error")
                .message(message)
                .build();
    }
}
