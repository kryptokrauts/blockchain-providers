package network.arkane.provider.bridge;

import lombok.SneakyThrows;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignatureMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EthereumTransactionGatewayTest {
    private EthereumTransactionGateway ethereumTransactionGateway;

    private Web3j web3j;

    @BeforeEach
    void setUp() {
        web3j = mock(Web3j.class);
        ethereumTransactionGateway = new EthereumTransactionGateway(web3j);
    }

    @Test
    @SneakyThrows
    void sendingTransactionCreatesTxHash() {
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
    void sendingTransactionThrowsEvent() {
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

        assertThatThrownBy(() -> ethereumTransactionGateway.submit(signTransactionResponse, Optional.empty()))
                .isInstanceOf(ArkaneException.class)
                .hasMessage("A problem occurred trying to submit the transaction to the Ethereum network")
                .hasFieldOrPropertyWithValue("errorCode", "web3j.transaction.submit.internal-error");
    }

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"insufficient funds for gas * price + value", "Insufficient funds, not enough things in wallet"})
    void insufficientFunds(final String errorMessage) {
        final EthSendTransaction sendTransaction = mock(EthSendTransaction.class);
        when(sendTransaction.hasError()).thenReturn(true);
        when(sendTransaction.getError()).thenReturn(new Response.Error(500, errorMessage));
        final TransactionSignature signTransactionResponse = TransactionSignatureMother.aSignTransactionResponse();
        final Request request = mock(Request.class);

        when(request.send()).thenReturn(sendTransaction);
        when(web3j.ethSendRawTransaction(eq(signTransactionResponse.getSignedTransaction()))).thenReturn(request);

        assertThatThrownBy(() -> ethereumTransactionGateway.submit(signTransactionResponse, Optional.empty()))
                .isInstanceOf(ArkaneException.class)
                .hasMessage("The account that initiated the transfer does not have enough energy")
                .hasFieldOrPropertyWithValue("errorCode", "transaction.insufficient-funds");
    }
}