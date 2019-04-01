package network.arkane.provider.tron;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.tron.core.Constant;
import org.tron.core.Wallet;

@Configuration
public class TronAutoConfiguration {

    public TronAutoConfiguration(final @Value("${tron.network}") String tronNetwork) {
        if ("testnet".equals(tronNetwork)) {
            Wallet.setAddressPreFixByte(Constant.ADD_PRE_FIX_BYTE_TESTNET);
            Wallet.setAddressPreFixString(Constant.ADD_PRE_FIX_STRING_TESTNET);
        } else {
            Wallet.setAddressPreFixByte(Constant.ADD_PRE_FIX_BYTE_MAINNET);
            Wallet.setAddressPreFixString(Constant.ADD_PRE_FIX_STRING_MAINNET);
        }
    }
}
