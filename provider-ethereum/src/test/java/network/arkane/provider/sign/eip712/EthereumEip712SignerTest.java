package network.arkane.provider.sign.eip712;

import network.arkane.provider.secret.generation.EthereumSecretKey;
import network.arkane.provider.sign.EthereumEip712Signable;
import network.arkane.provider.sign.domain.HexSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class EthereumEip712SignerTest {

    private EthereumEip712Signer ethereumEip712Signer;

    @BeforeEach
    void setUp() {
        ethereumEip712Signer = new EthereumEip712Signer();
    }

    @Test
    void testEip712SignatureSameResultAsMetaMask() {
        EthereumEip712Signable signable = EthereumEip712Signable.builder()
                                                                .data(""
                                                                      + "{"
                                                                      + "  \"types\": {"
                                                                      + "    \"EIP712Domain\": ["
                                                                      + "      {\"name\": \"name\", \"type\": \"string\"},"
                                                                      + "      {\"name\": \"version\", \"type\": \"string\"},"
                                                                      + "      {\"name\": \"chainId\", \"type\": \"uint256\"},"
                                                                      + "      {\"name\": \"verifyingContract\", \"type\": \"address\"}"
                                                                      + "    ],"
                                                                      + "    \"Person\": ["
                                                                      + "      {\"name\": \"name\", \"type\": \"string\"},"
                                                                      + "      {\"name\": \"wallet\", \"type\": \"address\"}"
                                                                      + "    ],"
                                                                      + "    \"Mail\": ["
                                                                      + "      {\"name\": \"from\", \"type\": \"Person\"},"
                                                                      + "      {\"name\": \"to\", \"type\": \"Person\"},"
                                                                      + "      {\"name\": \"contents\", \"type\": \"string\"}"
                                                                      + "    ]"
                                                                      + "  },"
                                                                      + "  \"primaryType\": \"Mail\","
                                                                      + "  \"domain\": {"
                                                                      + "    \"name\": \"Ether Mail\","
                                                                      + "    \"version\": \"1\","
                                                                      + "    \"chainId\": 1,"
                                                                      + "    \"verifyingContract\": \"0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC\""
                                                                      + "  },"
                                                                      + "  \"message\": {"
                                                                      + "    \"from\": {"
                                                                      + "      \"name\": \"Cow\","
                                                                      + "      \"wallet\": \"0xCD2a3d9F938E13CD947Ec05AbC7FE734Df8DD826\""
                                                                      + "    },"
                                                                      + "    \"to\": {"
                                                                      + "      \"name\": \"Bob\","
                                                                      + "      \"wallet\": \"0xbBbBBBBbbBBBbbbBbbBbbbbBBbBbbbbBbBbbBBbB\""
                                                                      + "    },"
                                                                      + "    \"contents\": \"Hello, Bob!\""
                                                                      + "  }"
                                                                      + "}")
                                                                .build();

        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);
        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        System.out.println(signable.getData());

        HexSignature result = ethereumEip712Signer.createSignature(signable, EthereumSecretKey.builder().keyPair(aPair).build());

        assertThat(result.getSignature()).isEqualTo(
                "0x19e4c5dcf77c10f4b30df3d4cb5822a99a69ec18e6f5d7b9a889a5a4815740732656ebdcc5a9c8eee63e684a5ed282d6b59ffe2914076b025a77df76559403111c");
    }

    @Test
    void testEip712SignatureSameResultAsMetaMaskWithSalt() {
        EthereumEip712Signable signable = EthereumEip712Signable.builder()
                                                                .data("{\"types\":{\"EIP712Domain\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"version\","
                                                                      + "\"type\":\"string\"},{\"name\":\"chainId\",\"type\":\"uint256\"},{\"name\":\"verifyingContract\","
                                                                      + "\"type\":\"address\"},{\"name\":\"salt\",\"type\":\"bytes32\"}],\"Bid\":[{\"name\":\"amount\","
                                                                      + "\"type\":\"uint256\"},{\"name\":\"bidder\",\"type\":\"Identity\"}],\"Identity\":[{\"name\":\"userId\","
                                                                      + "\"type\":\"uint256\"},{\"name\":\"wallet\",\"type\":\"address\"}]},\"domain\":{\"name\":\"My amazing "
                                                                      + "dApp\",\"version\":\"2\",\"chainId\":1,"
                                                                      + "\"verifyingContract\":\"0x1C56346CD2A2Bf3202F771f50d3D14a367B48070\","
                                                                      + "\"salt\":\"0xf2d857f4a3edcb9b78b4d503bfe733db1e3f6cdc2b7971ee739626c97e86a558\"},"
                                                                      + "\"primaryType\":\"Bid\",\"message\":{\"amount\":100,\"bidder\":{\"userId\":323,"
                                                                      + "\"wallet\":\"0x3333333333333333333333333333333333333333\"}}}")
                                                                .build();

        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);
        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        System.out.println(signable.getData());

        HexSignature result = ethereumEip712Signer.createSignature(signable, EthereumSecretKey.builder().keyPair(aPair).build());

        assertThat(result.getSignature()).isEqualTo(
                "0x38980a398c5e52d2e0dd197cf3a7660e2c57f65c9a51c1ad7d2c6ce5050036a1108e1fe7bab6d20f0cf3f5aad1e42ad941ce232ff57aa8ed35d5a818fb51e0e31b");
    }
}
