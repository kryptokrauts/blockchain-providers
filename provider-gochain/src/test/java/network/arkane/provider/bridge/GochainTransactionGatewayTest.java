package network.arkane.provider.bridge;

import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.gateway.GochainWeb3JGateway;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignatureMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GochainTransactionGatewayTest {
    private GochainTransactionGateway gochainTransactionGateway;

    private GochainWeb3JGateway web3JGateway;

    @BeforeEach
    public void setUp() {
        web3JGateway = mock(GochainWeb3JGateway.class);
        gochainTransactionGateway = new GochainTransactionGateway(web3JGateway);
    }

    @Test
    public void sendingTransactionCreatesTxHash() {
        final String expectedHash = "txHash";

        final TransactionSignature signTransactionResponse = TransactionSignatureMother.aSignTransactionResponse();
        final EthSendTransaction ethSendTransaction = mock(EthSendTransaction.class);
        when(ethSendTransaction.hasError()).thenReturn(false);

        when(ethSendTransaction.getTransactionHash()).thenReturn(expectedHash);
        when(web3JGateway.ethSendRawTransaction(eq(signTransactionResponse.getSignedTransaction())))
                .thenReturn(ethSendTransaction);

        final Signature response = gochainTransactionGateway.submit(signTransactionResponse, Optional.empty());
        assertThat(response).isInstanceOf(SubmittedAndSignedTransactionSignature.class);
        assertThat(((SubmittedAndSignedTransactionSignature) response).getTransactionHash()).isEqualTo(expectedHash);
    }

    @Test
    public void sendingTransactionThrowsEvent() {
        final TransactionSignature signTransactionResponse = TransactionSignatureMother.aSignTransactionResponse();
        final EthSendTransaction ethSendTransaction = mock(EthSendTransaction.class);
        when(ethSendTransaction.hasError()).thenReturn(false);

        when(ethSendTransaction.getTransactionHash()).thenReturn("txHash");
        when(web3JGateway.ethSendRawTransaction(eq(signTransactionResponse.getSignedTransaction())))
                .thenReturn(ethSendTransaction);

        gochainTransactionGateway.submit(signTransactionResponse, Optional.empty());
    }

    @Test
    void problemDuringTransactionCreation() {
        when(web3JGateway.ethSendRawTransaction(any(String.class)))
                .thenThrow(IllegalArgumentException.class);
        final TransactionSignature signTransactionResponse = TransactionSignatureMother.aSignTransactionResponse();
        assertThrows(ArkaneException.class,
                     () -> gochainTransactionGateway.submit(signTransactionResponse, Optional.empty()));
    }

    @Test
    void insufficientFunds() {
        EthSendTransaction sendTransaction = mock(EthSendTransaction.class);
        when(sendTransaction.hasError()).thenReturn(true);
        when(sendTransaction.getError()).thenReturn(new Response.Error(500, "Insufficient funds, not enough things in wallet"));
        when(web3JGateway.ethSendRawTransaction(any(String.class)))
                .thenReturn(sendTransaction);

        final TransactionSignature signTransactionResponse = TransactionSignatureMother.aSignTransactionResponse();
        assertThrows(ArkaneException.class, () -> gochainTransactionGateway.submit(signTransactionResponse, Optional.empty()), "The account that initiated the transfer does not have enough energy");
    }
}
