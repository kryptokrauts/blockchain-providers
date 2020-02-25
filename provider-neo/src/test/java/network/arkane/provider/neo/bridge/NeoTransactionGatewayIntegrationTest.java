package network.arkane.provider.neo.bridge;

import io.neow3j.crypto.ECKeyPair;
import io.neow3j.model.types.GASAsset;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.http.HttpService;
import network.arkane.provider.neo.gateway.NeoW3JGateway;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.neo.sign.NeoAssetTransferSignable;
import network.arkane.provider.neo.sign.NeoAssetTransferSigner;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Optional;

@Disabled
class NeoTransactionGatewayIntegrationTest {

    private NeoTransactionGateway neoTransactionGateway;
    private NeoAssetTransferSigner neoAssetTransferSigner;

    @BeforeEach
    void setUp() {
        NeoW3JGateway neow3j = new NeoW3JGateway(Neow3j.build(new HttpService("https://neo-testnet.arkane.network")));
        neoAssetTransferSigner = new NeoAssetTransferSigner(neow3j.web3());
        neoTransactionGateway = new NeoTransactionGateway(neow3j);
    }

    @Test
    void test() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        NeoSecretKey secretKey = NeoSecretKey.builder().key(ECKeyPair.createEcKeyPair()).build();
        TransactionSignature sig = (TransactionSignature) neoAssetTransferSigner.createSignature(NeoAssetTransferSignable.builder()
                                                                                                                         .amount(BigDecimal.ONE)
                                                                                                                         .to("APgDMyjxmwRtTMpModMaUie6rF664PSqte")
                                                                                                                         .assetId(GASAsset.HASH_ID)
                                                                                                                         .build(),
                                                                                                 secretKey);
        neoTransactionGateway.submit(sig, Optional.empty());
    }
}