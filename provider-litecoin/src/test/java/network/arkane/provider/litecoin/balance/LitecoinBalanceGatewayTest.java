package network.arkane.provider.litecoin.balance;

import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.blockcypher.domain.BlockcypherAddress;
import network.arkane.provider.chain.SecretType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LitecoinBalanceGatewayTest {

    private BlockcypherGateway blockcypherGateway;
    private LitecoinBalanceGateway litecoinBalanceGateway;

    @BeforeEach
    void setUp() {
        blockcypherGateway = mock(BlockcypherGateway.class);
        litecoinBalanceGateway = new LitecoinBalanceGateway(blockcypherGateway);
    }

    @Test
    public void emptyBalance() {
        String address = "VuXFIzGlKwF6A1m5vUI44S9MXBmE7sCDwE";

        when(blockcypherGateway.getBalance(Network.LITECOIN, address)).thenReturn(
                BlockcypherAddress.builder()
                        .address(address)
                        .balance(null)
                        .build()
        );

        Balance result = litecoinBalanceGateway.getBalance(address);

        assertThat(result.getBalance()).isEqualTo(0);
        assertThat(result.getGasBalance()).isEqualTo(0);
        assertThat(result.getRawBalance()).isEqualTo("0");
        assertThat(result.getRawGasBalance()).isEqualTo("0");
        assertThat(result.getSecretType()).isEqualTo(SecretType.LITECOIN);
        assertThat(result.getDecimals()).isEqualTo(8);
        assertThat(result.getSymbol()).isEqualTo("LTC");
        assertThat(result.getGasSymbol()).isEqualTo("LTC");
    }

    @Test
    public void withBalance() {
        String address = "VuXFIzGlKwF6A1m5vUI44S9MXBmE7sCDwE";

        when(blockcypherGateway.getBalance(Network.LITECOIN, address)).thenReturn(
                BlockcypherAddress.builder()
                        .address(address)
                        .balance(new BigInteger("123456789"))
                        .build()
        );

        Balance result = litecoinBalanceGateway.getBalance(address);

        assertThat(result.getBalance()).isEqualTo(1.23456789);
        assertThat(result.getGasBalance()).isEqualTo(1.23456789);
        assertThat(result.getRawBalance()).isEqualTo("123456789");
        assertThat(result.getRawGasBalance()).isEqualTo("123456789");
        assertThat(result.getSecretType()).isEqualTo(SecretType.LITECOIN);
        assertThat(result.getDecimals()).isEqualTo(8);
        assertThat(result.getSymbol()).isEqualTo("LTC");
        assertThat(result.getGasSymbol()).isEqualTo("LTC");
    }

    @Test
    public void correctType() {
        SecretType type = litecoinBalanceGateway.type();

        assertThat(type).isEqualTo(SecretType.LITECOIN);
    }

    @Test
    public void getTokenBalanceIsUnsupported() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> litecoinBalanceGateway.getTokenBalance("", "")
        );
    }

    @Test
    public void getTokenBalancesIsUnsupported() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> litecoinBalanceGateway.getTokenBalances("")
        );
    }

}