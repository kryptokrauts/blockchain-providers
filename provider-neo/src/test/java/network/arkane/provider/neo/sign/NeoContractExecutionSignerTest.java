package network.arkane.provider.neo.sign;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.neow3j.crypto.ECKeyPair;
import io.neow3j.crypto.WIF;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.http.HttpService;
import network.arkane.provider.neo.bridge.NeoTransactionGateway;
import network.arkane.provider.neo.gateway.NeoW3JGateway;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class NeoContractExecutionSignerTest {

    private NeoContractExecutionSigner signer;
    private NeoTransactionGateway neoTransactionGateway;
    private Neow3j neow3j;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        neow3j = Neow3j.build(new HttpService("http://seed5.ngd.network:20332", true));
        objectMapper = new ObjectMapper();
        neoTransactionGateway = new NeoTransactionGateway(new NeoW3JGateway(neow3j));
        signer = new NeoContractExecutionSigner(neow3j, objectMapper);
    }

    @Test
    void name() throws IOException {
        final List<JsonNode> inputs = Arrays.asList(objectMapper.readTree("{\"type\": \"address\", \"value\": \"AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y\"}"),
                                                    objectMapper.readTree("{\"type\": \"integer\", \"value\": 0}"));

        NeoContractExecutionSignable signable = NeoContractExecutionSignable.builder()
                                                                            .contractScriptHash("94a24ee381bc386daa91984c7dd606f6fdd8f19e")
                                                                            .functionName("approve")
                                                                            .inputs(inputs)
                                                                            .networkFee("0.1")
                                                                            .build();
        ECKeyPair ecKeyPair = ECKeyPair.create(WIF.getPrivateKeyFromWIF("Kx9xMQVipBYAAjSxYEoZVatdVQfhYHbMFWSYPinSgAVd1d4Qgbpf"));
        NeoSecretKey secretKey = NeoSecretKey.builder().key(ecKeyPair).build();

        System.out.println("using wallet: " + secretKey.getKey().getAddress());
        TransactionSignature signature = signer.createSignature(signable, secretKey);
        System.out.println(signature);
        Signature result = neoTransactionGateway.submit(TransactionSignature.signTransactionBuilder().signedTransaction(signature.getSignedTransaction()).build(),
                                                        Optional.empty());

        assertThat(result).isNotNull();
    }
}