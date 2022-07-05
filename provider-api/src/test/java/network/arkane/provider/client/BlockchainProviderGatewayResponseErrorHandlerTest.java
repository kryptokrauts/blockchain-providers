package network.arkane.provider.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.exceptions.BlockchainProviderResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BlockchainProviderGatewayResponseErrorHandlerTest {

    private final ObjectMapper objectMapper = new JsonMapper();
    private final BlockchainProviderGatewayResponseErrorHandler toTest = new BlockchainProviderGatewayResponseErrorHandler(objectMapper);

    @ParameterizedTest
    @EnumSource(value = HttpStatus.class, names = {"BAD_REQUEST", "NOT_FOUND", "INTERNAL_SERVER_ERROR"})
    void returnTrueIfResponseHasError(HttpStatus status) throws IOException {
        final ClientHttpResponse response = mock(ClientHttpResponse.class);
        when(response.getStatusCode()).thenReturn(status);
        assertThat(toTest.hasError(response)).isTrue();
    }

    @Test
    void returnFalseIfResponseIsOk() throws IOException {
        final ClientHttpResponse response = mock(ClientHttpResponse.class);
        when(response.getStatusCode()).thenReturn(HttpStatus.OK);
        assertThat(toTest.hasError(response)).isFalse();
    }

    @Test
    void throwArkaneException() throws IOException {
        final ClientHttpResponse response = mock(ClientHttpResponse.class);
        when(response.getStatusCode()).thenReturn(HttpStatus.BAD_GATEWAY);
        when(response.getStatusText()).thenReturn("ERROR");
        when(response.getBody()).thenReturn(new ByteArrayInputStream("null".getBytes()));

        assertThatThrownBy(() -> toTest.handleError(response))
                .isInstanceOf(ArkaneException.class)
                .hasMessageContaining("An unexpected error occurred: ERROR");
    }

    @Test
    void throwNotFoundException() throws IOException {
        final ClientHttpResponse response = mock(ClientHttpResponse.class);
        when(response.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        when(response.getStatusText()).thenReturn("ERROR");
        when(response.getBody()).thenReturn(new ByteArrayInputStream("null".getBytes()));

        assertThatThrownBy(() -> toTest.handleError(response))
                .isInstanceOf(BlockchainProviderResourceNotFoundException.class)
                .hasMessageContaining("An unexpected error occurred: ERROR");
    }

}