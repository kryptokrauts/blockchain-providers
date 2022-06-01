package network.arkane.provider.bitcoin;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.bitcoin.bip38.BIP38;
import network.arkane.provider.bitcoin.bip38.BIP38EncryptionService;
import network.arkane.provider.bitcoin.extraction.BitcoinWifPassphraseExtractionRequest;
import network.arkane.provider.bitcoin.extraction.BitcoinWifPassphraseExtractor;
import network.arkane.provider.bitcoin.secret.generation.BitcoinSecretGenerator;
import network.arkane.provider.bitcoin.secret.generation.BitcoinSecretKey;
import network.arkane.provider.bitcoin.wallet.exporting.BitcoinKeyExporter;
import network.arkane.provider.bitcoin.wallet.generation.BitcoinWalletGenerator;
import network.arkane.provider.bitcoin.wallet.generation.GeneratedBitcoinWallet;
import network.arkane.provider.blockcypher.Network;
import org.bitcoinj.params.TestNet3Params;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class BitcoinGenerateImportExportIntegrationTest {

    private BitcoinWalletGenerator bitcoinWalletGenerator;
    private BitcoinEnv bitcoinEnv;
    private BitcoinKeyExporter bitcoinKeyExporter;
    private BitcoinWifPassphraseExtractor bitcoinWifPassphraseExtractor;
    private BitcoinSecretGenerator bitcoinSecretGenerator;

    @BeforeEach
    void setUp() {
        bitcoinEnv = new BitcoinEnv(Network.BTC_TEST, TestNet3Params.get());
        bitcoinSecretGenerator = new BitcoinSecretGenerator();
        bitcoinWalletGenerator = new BitcoinWalletGenerator(bitcoinEnv);
        BIP38 bip38 = new BIP38(bitcoinEnv);
        bitcoinKeyExporter = new BitcoinKeyExporter(new BIP38EncryptionService(bitcoinEnv, bip38));
        bitcoinWifPassphraseExtractor = new BitcoinWifPassphraseExtractor(bitcoinEnv);
    }

    @Test
    void shouldReturnTheSameAddressAfterImportAndExportGeneratedWallet() throws IOException {
        BitcoinSecretKey secretKey = bitcoinSecretGenerator.generate();
        GeneratedBitcoinWallet generatedWallet = bitcoinWalletGenerator.generateWallet("firstPassword", secretKey);
        String wif = secretKey.getKey().getPrivateKeyAsWiF(bitcoinEnv.getNetworkParameters());
        log.info("Generated wallet: {}", generatedWallet.getAddress());

        String exportWithPassword = bitcoinKeyExporter.export(secretKey, "secondPassword");

        String exportedWifWithPassword = new ObjectMapper().readTree(exportWithPassword).get("value").textValue();

        BitcoinSecretKey importedSecretKey = bitcoinWifPassphraseExtractor.extract(BitcoinWifPassphraseExtractionRequest.builder()
                                                                                                                        .wif(exportedWifWithPassword)
                                                                                                                        .passphrase("secondPassword")
                                                                                                                        .build());
        String importedAddress = importedSecretKey.getKey().toAddress(bitcoinEnv.getNetworkParameters()).toBase58();
        log.info(importedAddress);

        assertThat(generatedWallet.getAddress()).isEqualToIgnoringCase(importedAddress);
    }
}
