package network.arkane.provider.bitcoin.balance;

import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.blockcypher.domain.BlockcypherAddress;
import network.arkane.provider.chain.SecretType;
import org.bitcoinj.params.TestNet3Params;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BitcoinBalanceGatewayTest {

    private BlockcypherGateway blockcypherGateway;
    private BitcoinBalanceGateway bitcoinBalanceGateway;

    @BeforeEach
    void setUp() {
        blockcypherGateway = mock(BlockcypherGateway.class);
        bitcoinBalanceGateway = new BitcoinBalanceGateway(blockcypherGateway, new BitcoinEnv(Network.BTC_TEST, TestNet3Params.get()));
    }

    @Test
    void emptyBalance() {
        String address = "mhxqpVGP4AiYNsmFwDGqBPaNimNZkqCX8o";

        BlockcypherAddress balanceResult = BlockcypherAddress.builder().address(address).balance(null).build();
        when(blockcypherGateway.getBalance(Network.BTC_TEST, address)).thenReturn(balanceResult);

        Balance result = bitcoinBalanceGateway.getBalance(address);

        assertThat(result.getBalance()).isEqualTo(0);
        assertThat(result.getGasBalance()).isEqualTo(0);
        assertThat(result.getRawBalance()).isEqualTo("0");
        assertThat(result.getRawGasBalance()).isEqualTo("0");
        assertThat(result.getSecretType()).isEqualTo(SecretType.BITCOIN);
        assertThat(result.getDecimals()).isEqualTo(8);
        assertThat(result.getSymbol()).isEqualTo("BTC");
        assertThat(result.getGasSymbol()).isEqualTo("BTC");
    }

    @Test
    void withBalance() {
        String address = "mhxqpVGP4AiYNsmFwDGqBPaNimNZkqCX8o";
        BlockcypherAddress balanceResult = BlockcypherAddress.builder().address(address).balance(new BigInteger("138478048")).build();
        when(blockcypherGateway.getBalance(Network.BTC_TEST, address)).thenReturn(balanceResult);

        Balance result = bitcoinBalanceGateway.getBalance(address);

        assertThat(result.getBalance()).isEqualTo(1.38478048);
        assertThat(result.getGasBalance()).isEqualTo(1.38478048);
        assertThat(result.getRawBalance()).isEqualTo("138478048");
        assertThat(result.getRawGasBalance()).isEqualTo("138478048");
        assertThat(result.getSecretType()).isEqualTo(SecretType.BITCOIN);
        assertThat(result.getDecimals()).isEqualTo(8);
        assertThat(result.getSymbol()).isEqualTo("BTC");
        assertThat(result.getGasSymbol()).isEqualTo("BTC");
    }
}