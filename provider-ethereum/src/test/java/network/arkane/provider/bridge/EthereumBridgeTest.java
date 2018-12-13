package network.arkane.provider.bridge;

import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.gateway.Web3JGateway;
import network.arkane.provider.signature.Signature;
import network.arkane.provider.signature.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.signature.TransactionSignature;
import network.arkane.provider.signature.TransactionSignatureMother;
import network.arkane.provider.token.TokenInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.math.BigInteger;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EthereumBridgeTest {
    private EthereumBridge ethereumBridge;

    private Web3JGateway web3JGateway;
    private ApplicationEventPublisher applicationEventPublisher;

    @BeforeEach
    public void setUp() {
        web3JGateway = mock(Web3JGateway.class);
        applicationEventPublisher = mock(ApplicationEventPublisher.class);
        ethereumBridge = new EthereumBridge(web3JGateway, applicationEventPublisher);
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

        final Signature response = ethereumBridge.submit(signTransactionResponse);
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

        ethereumBridge.submit(signTransactionResponse);
    }

    @Test
    void problemDuringTransactionCreation() {
        when(web3JGateway.ethSendRawTransaction(any(String.class)))
                .thenThrow(IllegalArgumentException.class);
        final TransactionSignature signTransactionResponse = TransactionSignatureMother.aSignTransactionResponse();
        assertThrows(ArkaneException.class,
                     () -> ethereumBridge.submit(signTransactionResponse));
    }

    @Test
    void insufficientFunds() {
        EthSendTransaction sendTransaction = mock(EthSendTransaction.class);
        when(sendTransaction.hasError()).thenReturn(true);
        when(sendTransaction.getError()).thenReturn(new Response.Error(500, "Insufficient funds, not enough things in wallet"));
        when(web3JGateway.ethSendRawTransaction(any(String.class)))
                .thenReturn(sendTransaction);

        final TransactionSignature signTransactionResponse = TransactionSignatureMother.aSignTransactionResponse();
        assertThrows(ArkaneException.class, () -> ethereumBridge.submit(signTransactionResponse), "The account that initiated the transfer does not have enough energy");
    }

    @Test
    void getTokenInfo() {
        final String tokenAddress = "0x0";
        final String tokenName = "SomeToken";
        final String tokenSymbol = "STN";
        final int tokenDecimals = 15;

        when(web3JGateway.getDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));
        when(web3JGateway.getName(tokenAddress)).thenReturn(tokenName);
        when(web3JGateway.getSymbol(tokenAddress)).thenReturn(tokenSymbol);

        final Optional<TokenInfo> result = ethereumBridge.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get().getAddress()).isEqualTo(tokenAddress);
        Assertions.assertThat(result.get().getName()).isEqualTo(tokenName);
        Assertions.assertThat(result.get().getSymbol()).isEqualTo(tokenSymbol);
        Assertions.assertThat(result.get().getDecimals()).isEqualTo(tokenDecimals);
        Assertions.assertThat(result.get().getType()).isEqualTo("ERC20");
    }

    @Test
    void getTokenInfo_noName() {
        final String tokenAddress = "0x0";
        final String tokenSymbol = "STN";
        final int tokenDecimals = 15;

        when(web3JGateway.getDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));
        when(web3JGateway.getName(tokenAddress)).thenReturn(null);
        when(web3JGateway.getSymbol(tokenAddress)).thenReturn(tokenSymbol);

        final Optional<TokenInfo> result = ethereumBridge.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getTokenInfo_noDecimals() {
        final String tokenAddress = "0x0";
        final String tokenName = "SomeToken";
        final String tokenSymbol = "STN";

        when(web3JGateway.getDecimals(tokenAddress)).thenReturn(null);
        when(web3JGateway.getName(tokenAddress)).thenReturn(tokenName);
        when(web3JGateway.getSymbol(tokenAddress)).thenReturn(tokenSymbol);

        final Optional<TokenInfo> result = ethereumBridge.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getTokenInfo_noSymbol() {
        final String tokenAddress = "0x0";
        final String tokenName = "SomeToken";
        final int tokenDecimals = 15;

        when(web3JGateway.getDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));
        when(web3JGateway.getName(tokenAddress)).thenReturn(tokenName);
        when(web3JGateway.getSymbol(tokenAddress)).thenReturn(null);

        final Optional<TokenInfo> result = ethereumBridge.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }
}