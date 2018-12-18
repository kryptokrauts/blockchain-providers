package network.arkane.provider.bridge;

import network.arkane.provider.core.model.blockchain.TransferResult;
import network.arkane.provider.gateway.VechainGateway;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.token.TokenInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VechainTransactionGatewayTest {

    private VechainTransactionGateway vechainTransactionGateway;
    private VechainGateway vechainGateway;

    @BeforeEach
    void setUp() {
        vechainGateway = mock(VechainGateway.class);

        vechainTransactionGateway = new VechainTransactionGateway(vechainGateway);
    }

    @Test
    void submit() {
        TransactionSignature transactionSignature = TransactionSignature.signTransactionBuilder().signedTransaction("signed").build();
        TransferResult transferResult = new TransferResult();
        transferResult.setId("transferId");
        when(vechainGateway.sendRawTransaction(transactionSignature.getSignedTransaction())).thenReturn(transferResult);

        Signature result = vechainTransactionGateway.submit(transactionSignature);

        assertThat(result).isExactlyInstanceOf(SubmittedAndSignedTransactionSignature.class);
        assertThat(((SubmittedAndSignedTransactionSignature) result).getTransactionHash()).isEqualTo("transferId");
    }

    @Test
    void submitWithException() {
        final TransactionSignature transactionSignature = TransactionSignature.signTransactionBuilder().signedTransaction("signed").build();
        final TransferResult transferResult = new TransferResult();
        transferResult.setId("transferId");
        when(vechainGateway.sendRawTransaction(transactionSignature.getSignedTransaction())).thenThrow(new RuntimeException("error signing"));

        assertThatThrownBy(() -> vechainTransactionGateway.submit(transactionSignature)).hasMessage(
                "problem trying to submit transaction to vechain: error signing");
    }

    @Test
    void getTokenInfo() {
        final String tokenAddress = "0x0";
        final String tokenName = "Some Token";
        final String tokenSymbol = "STN";
        final int tokenDecimals = 13;

        when(vechainGateway.getTokenName(tokenAddress)).thenReturn(tokenName);
        when(vechainGateway.getTokenSymbol(tokenAddress)).thenReturn(tokenSymbol);
        when(vechainGateway.getTokenDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));

        final Optional<TokenInfo> result = vechainTransactionGateway.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get().getAddress()).isEqualTo(tokenAddress);
        Assertions.assertThat(result.get().getName()).isEqualTo(tokenName);
        Assertions.assertThat(result.get().getSymbol()).isEqualTo(tokenSymbol);
        Assertions.assertThat(result.get().getDecimals()).isEqualTo(tokenDecimals);
        Assertions.assertThat(result.get().getType()).isEqualTo("VIP180");
    }

    @Test
    void getTokenInfo_noName() {
        final String tokenAddress = "0x0";
        final String tokenSymbol = "STN";
        final int tokenDecimals = 15;

        when(vechainGateway.getTokenName(tokenAddress)).thenReturn(null);
        when(vechainGateway.getTokenSymbol(tokenAddress)).thenReturn(tokenSymbol);
        when(vechainGateway.getTokenDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));

        final Optional<TokenInfo> result = vechainTransactionGateway.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getTokenInfo_noDecimals() {
        final String tokenAddress = "0x0";
        final String tokenName = "SomeToken";
        final String tokenSymbol = "STN";

        when(vechainGateway.getTokenName(tokenAddress)).thenReturn(tokenName);
        when(vechainGateway.getTokenSymbol(tokenAddress)).thenReturn(tokenSymbol);
        when(vechainGateway.getTokenDecimals(tokenAddress)).thenReturn(null);

        final Optional<TokenInfo> result = vechainTransactionGateway.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getTokenInfo_noSymbol() {
        final String tokenAddress = "0x0";
        final String tokenName = "SomeToken";
        final int tokenDecimals = 15;

        when(vechainGateway.getTokenName(tokenAddress)).thenReturn(tokenName);
        when(vechainGateway.getTokenSymbol(tokenAddress)).thenReturn(null);
        when(vechainGateway.getTokenDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));

        final Optional<TokenInfo> result = vechainTransactionGateway.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }
}