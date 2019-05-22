package network.arkane.provider.neo.bridge;

import io.neow3j.protocol.core.Response;
import io.neow3j.protocol.core.methods.response.NeoSendRawTransaction;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.neo.gateway.NeoW3JGateway;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import network.arkane.provider.sign.domain.TransactionSignatureMother;

class NeoTransactionGatewayTest {
    private NeoTransactionGateway NeoTransactionGateway;

    private network.arkane.provider.neo.gateway.NeoW3JGateway NeoW3JGateway;

    private  TransactionSignature signedTx;

    @BeforeEach
    public void setUp() {
        NeoW3JGateway = mock(NeoW3JGateway.class);
        NeoTransactionGateway = new NeoTransactionGateway(NeoW3JGateway);
        signedTx = TransactionSignature
                .signTransactionBuilder()
                .signedTransaction("80000001195876cb34364dc38b730077156c6bc3a7fc570044a66fbfeeea56f71327e8ab0000029b7cffdaa674beae0f930ebe6085af9093e5fe56b34a5c220ccdcf6efc336fc500c65eaf440000000f9a23e06f74cf86b8827a9108ec2e0f89ad956c9b7cffdaa674beae0f930ebe6085af9093e5fe56b34a5c220ccdcf6efc336fc50092e14b5e00000030aab52ad93f6ce17ca07fa88fc191828c58cb71014140915467ecd359684b2dc358024ca750609591aa731a0b309c7fb3cab5cd0836ad3992aa0a24da431f43b68883ea5651d548feb6bd3c8e16376e6e426f91f84c58232103322f35c7819267e721335948d385fae5be66e7ba8c748ac15467dcca0693692dac")
                .build();

    }

    @Test
    public void sendingTransactionCreatesTxHash() {
        final String expectedHash = "0xf4250dab094c38d8265acc15c366dc508d2e14bf5699e12d9df26577ed74d657";

        final TransactionSignature signTransactionResponse = signedTx;
        final NeoSendRawTransaction sendTransaction = mock(NeoSendRawTransaction.class);
        when(sendTransaction.hasError()).thenReturn(false);

        when(NeoW3JGateway.sendRawTransaction(eq(signTransactionResponse.getSignedTransaction())))
                .thenReturn(sendTransaction);

        final Signature response = NeoTransactionGateway.submit(signTransactionResponse);
        assertThat(response).isInstanceOf(SubmittedAndSignedTransactionSignature.class);
        assertThat(((SubmittedAndSignedTransactionSignature) response).getTransactionHash()).isEqualTo(expectedHash);
    }

    @Test
    public void sendingTransactionThrowsEvent() {
        final TransactionSignature signTransactionResponse = signedTx;
        final NeoSendRawTransaction ethSendTransaction = mock(NeoSendRawTransaction.class);
        when(ethSendTransaction.hasError()).thenReturn(false);

        when(NeoW3JGateway.sendRawTransaction(eq(signTransactionResponse.getSignedTransaction())))
                .thenReturn(ethSendTransaction);

        NeoTransactionGateway.submit(signTransactionResponse);
    }

    @Test
    void problemDuringTransactionCreation() {
        when(NeoW3JGateway.sendRawTransaction(any(String.class)))
                .thenThrow(IllegalArgumentException.class);
        final TransactionSignature signTransactionResponse = TransactionSignatureMother.aSignTransactionResponse();
        assertThrows(ArkaneException.class,
                () -> NeoTransactionGateway.submit(signTransactionResponse));
    }

    @Test
    void insufficientFunds() {
        NeoSendRawTransaction sendTransaction = mock(NeoSendRawTransaction.class);
        when(sendTransaction.hasError()).thenReturn(true);
        when(sendTransaction.getError()).thenReturn(new Response.Error(500, "Insufficient funds, not enough things in wallet"));
        when(NeoW3JGateway.sendRawTransaction(any(String.class)))
                .thenReturn(sendTransaction);

        final TransactionSignature signTransactionResponse = TransactionSignatureMother.aSignTransactionResponse();
        assertThrows(ArkaneException.class, () -> NeoTransactionGateway.submit(signTransactionResponse), "The account that initiated the transfer does not have enough energy");
    }
}