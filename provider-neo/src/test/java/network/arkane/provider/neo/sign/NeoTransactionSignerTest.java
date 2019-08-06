package network.arkane.provider.neo.sign;

import io.neow3j.crypto.ECKeyPair;
import io.neow3j.crypto.WIF;
import io.neow3j.crypto.transaction.*;
import io.neow3j.model.types.NEOAsset;
import io.neow3j.utils.Numeric;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class NeoTransactionSignerTest {

    private NeoSecretKey key;

    @BeforeEach
    void setUp() {
        ECKeyPair ecKeyPair = ECKeyPair.create(WIF.getPrivateKeyFromWIF("Kx9xMQVipBYAAjSxYEoZVatdVQfhYHbMFWSYPinSgAVd1d4Qgbpf"));
        key = NeoSecretKey.builder().key(ecKeyPair).build();
    }

    @Test
    void generateKey() {
        final String expect = "80000001d22dcd884f5d065ce8596629ad9abed8b24c3428c43767d8ac9bacb0940f4dc90000029b7cffdaa674beae0f930ebe6085af9093e5fe56b34a5c220ccdcf6efc336fc500ca9a3b0000000023ba2703c53263e8d6e522dc32203339dcd8eee99b7cffdaa674beae0f930ebe6085af9093e5fe56b34a5c220ccdcf6efc336fc5001a711802000000295f83f83fc439f56e6e1fb062d89c6f538263d701414066a5b1f8d3c7185e971686e1212ca0108aa518704cbc0fc6634f05beb0b1e6d710a9f4b408600986845d8b917a7c245a60629e30484f9ed015ddc0fbab2422ca232102789a9e63a054711b1ce7f91bf0d56886fee3bd9166e91761a92bb0a90fcfd442ac";

        final NeoTransactionSignable signable = NeoTransactionSignable
                .builder()
                .inputs(Collections.singletonList(
                        new RawTransactionInput("c94d0f94b0ac9bacd86737c428344cb2d8be9aad296659e85c065d4f88cd2dd2", 0)
                ))
                .outputs(Arrays.asList(
                        new RawTransactionOutput(NEOAsset.HASH_ID, "10.0", "AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y"),
                        new RawTransactionOutput(NEOAsset.HASH_ID, "90.0", "AKYdmtzCD6DtGx16KHzSTKY8ji29sMTbEZ")
                ))
                .build();


        final TransactionSignature signature = (TransactionSignature) (new NeoTransactionSigner().createSignature(signable, key));
        assertThat(expect).isEqualTo(signature.getSignedTransaction());
    }

}