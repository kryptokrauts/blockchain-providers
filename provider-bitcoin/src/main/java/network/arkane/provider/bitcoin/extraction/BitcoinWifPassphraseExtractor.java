package network.arkane.provider.bitcoin.extraction;

import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.bitcoin.secret.generation.BitcoinSecretKey;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.wallet.extraction.SecretExtractor;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.BIP38PrivateKey;
import org.springframework.stereotype.Component;

@Component
public class BitcoinWifPassphraseExtractor implements SecretExtractor<BitcoinWifPassphraseExtractionRequest> {

    private final NetworkParameters networkParameters;

    public BitcoinWifPassphraseExtractor(final BitcoinEnv bitcoinEnv) {
        this.networkParameters = bitcoinEnv.getNetworkParameters();
    }

    @Override
    public BitcoinSecretKey extract(final BitcoinWifPassphraseExtractionRequest extractionRequest) {
        final BIP38PrivateKey bip38PrivateKey = BIP38PrivateKey.fromBase58(networkParameters, extractionRequest.getWif());
        try {
            return BitcoinSecretKey.builder()
                                   .key(bip38PrivateKey.decrypt(extractionRequest.getPassphrase()))
                                   .build();
        } catch (BIP38PrivateKey.BadPassphraseException e) {
            throw new ArkaneException("Wrong BIP38PrivateKey passphrase", e);
        }
    }
}