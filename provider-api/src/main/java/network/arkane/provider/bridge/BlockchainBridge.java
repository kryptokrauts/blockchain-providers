package network.arkane.provider.bridge;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.signature.Signature;
import network.arkane.provider.signature.TransactionSignature;
import network.arkane.provider.token.TokenInfo;

import java.util.Optional;

public interface BlockchainBridge {
    SecretType getType();

    Signature submit(TransactionSignature transactionSignature);

    Optional<TokenInfo> getTokenInfo(String tokenAddress);
}
