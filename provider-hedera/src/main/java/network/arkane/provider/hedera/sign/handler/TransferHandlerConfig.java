package network.arkane.provider.hedera.sign.handler;

import network.arkane.provider.hedera.sign.HbarTransferSignable;
import network.arkane.provider.hedera.sign.HederaTransferSignable;
import network.arkane.provider.hedera.sign.NftTransferSignable;
import network.arkane.provider.hedera.sign.TokenTransferSignable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TransferHandlerConfig {

    @Bean("transferHandlerMap")
    public Map<Class<? extends HederaTransferSignable>, TransferHandler<? extends HederaTransferSignable>> transferHandlerMap(final HbarTransferHandler hbarTransferHandler,
                                                                                                                              final TokenTransferHandler tokenTransferHandler,
                                                                                                                              final NftTransferHandler nftTransferHandler) {
        final Map<Class<? extends HederaTransferSignable>, TransferHandler<? extends HederaTransferSignable>> handlerMap = new HashMap<>();
        handlerMap.put(HbarTransferSignable.class, hbarTransferHandler);
        handlerMap.put(TokenTransferSignable.class, tokenTransferHandler);
        handlerMap.put(NftTransferSignable.class, nftTransferHandler);
        return handlerMap;
    }
}
