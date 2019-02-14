package network.arkane.provider.tron.token;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.token.NativeTokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import network.arkane.provider.tron.grpc.GrpcClient;
import org.springframework.stereotype.Component;
import org.tron.protos.Contract;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class NativeTronTokenDiscoveryService implements NativeTokenDiscoveryService {

    private GrpcClient grpcClient;

    public NativeTronTokenDiscoveryService(final GrpcClient grpcClient) {
        this.grpcClient = grpcClient;
    }

    @Override
    public Optional<TokenInfo> getTokenInfo(final String tokenAddress) {
        if ("BANDWIDTH".equals(tokenAddress)) {
            return Optional.of(TokenInfo.builder()
                                        .decimals(0)
                                        .address("BANDWIDTH")
                                        .name("Bandwidth")
                                        .symbol("bandwidth")
                                        .logo("tron-bandwidth")
                                        .build());
        } else {
            final Contract.AssetIssueContract asset = grpcClient.getAssetIssueById(tokenAddress);
            final String name = asset.getName().toStringUtf8();
            final String symbol = asset.getAbbr().toStringUtf8();
            final BigInteger decimals = BigInteger.valueOf(asset.getPrecision());

            if (name != null && decimals != null && symbol != null) {
                return Optional.of(TokenInfo.builder()
                                            .address(tokenAddress)
                                            .name(name)
                                            .decimals(decimals.intValue())
                                            .symbol(symbol)
                                            .type("Tron-native")
                                            .build());
            } else {
                return Optional.empty();
            }
        }

    }

    @Override
    public SecretType type() {
        return SecretType.TRON;
    }
}
