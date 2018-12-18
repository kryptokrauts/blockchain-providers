package network.arkane.provider.bridge;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.token.TokenInfo;

import java.util.Optional;

public interface TransactionGateway {
    SecretType getType();

    Signature submit(TransactionSignature transactionSignature);

    Optional<TokenInfo> getTokenInfo(String tokenAddress);
}
