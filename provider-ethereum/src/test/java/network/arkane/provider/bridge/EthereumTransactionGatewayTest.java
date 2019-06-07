package network.arkane.provider.bridge;

import lombok.SneakyThrows;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignatureMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EthereumTransactionGatewayTest {
    private EthereumTransactionGateway ethereumTransactionGateway;

    private Web3j web3j;

    @BeforeEach
    public void setUp() {
        web3j = mock(Web3j.class);
        ethereumTransactionGateway = new EthereumTransactionGateway(web3j);
    }

    @Test
    @SneakyThrows
    public void sendingTransactionCreatesTxHash() {
        final String expectedHash = "txHash";

        final TransactionSignature signTransactionResponse = TransactionSignatureMother.aSignTransactionResponse();
        final EthSendTransaction ethSendTransaction = mock(EthSendTransaction.class);
        when(ethSendTransaction.hasError()).thenReturn(false);

        when(ethSendTransaction.getTransactionHash()).thenReturn(expectedHash);
        final Request<?, EthSendTransaction> request = mock(Request.class);
        when(request.send()).thenReturn(ethSendTransaction);

        doReturn(request).when(web3j).ethSendRawTransaction(signTransactionResponse.getSignedTransaction());

        final Signature response = ethereumTransactionGateway.submit(signTransactionResponse, Optional.empty());
        assertThat(response).isInstanceOf(SubmittedAndSignedTransactionSignature.class);
        assertThat(((SubmittedAndSignedTransactionSignature) response).getTransactionHash()).isEqualTo(expectedHash);
    }

    @Test
    @SneakyThrows
    public void sendingTransactionThrowsEvent() {
        final TransactionSignature signTransactionResponse = TransactionSignatureMother.aSignTransactionResponse();
        final EthSendTransaction ethSendTransaction = mock(EthSendTransaction.class);
        when(ethSendTransaction.hasError()).thenReturn(false);

        when(ethSendTransaction.getTransactionHash()).thenReturn("txHash");
        final Request<?, EthSendTransaction> request = mock(Request.class);
        when(request.send()).thenReturn(ethSendTransaction);

        doReturn(request).when(web3j).ethSendRawTransaction(signTransactionResponse.getSignedTransaction());

        ethereumTransactionGateway.submit(signTransactionResponse, Optional.empty());
    }

    @Test
    void problemDuringTransactionCreation() {
        when(web3j.ethSendRawTransaction(any(String.class)))
                .thenThrow(IllegalArgumentException.class);
        final TransactionSignature signTransactionResponse = TransactionSignatureMother.aSignTransactionResponse();

        assertThrows(ArkaneException.class,
                     () -> ethereumTransactionGateway.submit(signTransactionResponse, Optional.empty()));
    }

    @Test
    @SneakyThrows
    void insufficientFunds() {
        final EthSendTransaction sendTransaction = mock(EthSendTransaction.class);
        when(sendTransaction.hasError()).thenReturn(true);
        when(sendTransaction.getError()).thenReturn(new Response.Error(500, "Insufficient funds, not enough things in wallet"));
        final TransactionSignature signTransactionResponse = TransactionSignatureMother.aSignTransactionResponse();

        Request mock = mock(Request.class);
        Response expectedResponse = new Response();
        expectedResponse.setResult(sendTransaction);
        when(mock.send()).thenReturn(expectedResponse);
        when(web3j.ethSendRawTransaction(eq(signTransactionResponse.getSignedTransaction())))
                .thenReturn(mock);

        assertThrows(ArkaneException.class,
                     () -> ethereumTransactionGateway.submit(signTransactionResponse, Optional.empty()),
                     "The account that initiated the transfer does not have enough energy");
    }
}