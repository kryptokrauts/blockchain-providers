package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfoQuery;
import com.hedera.hashgraph.sdk.PublicKey;
import lombok.SneakyThrows;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.sign.Verifier;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class HederaRawVerifier implements Verifier<HederaRawVerifiable> {
    private final HederaClientFactory hederaClientFactory;

    public HederaRawVerifier(HederaClientFactory hederaClientFactory) {
        this.hederaClientFactory = hederaClientFactory;
    }
    @Override
    @SneakyThrows
    public boolean isValidSignature(HederaRawVerifiable verifiable) {
        final AccountInfoQuery accountInfoQuery = new AccountInfoQuery()
                .setAccountId(AccountId.fromString(verifiable.getAddress()));
        final byte[] publicKey = accountInfoQuery.execute(hederaClientFactory.getClientWithOperator()).key.toBytes();
        return PublicKey.fromBytes(publicKey)
                        .verify(verifiable.getMessage().getBytes(StandardCharsets.UTF_8), Hex.decode(verifiable.getSignature()));
    }

}
