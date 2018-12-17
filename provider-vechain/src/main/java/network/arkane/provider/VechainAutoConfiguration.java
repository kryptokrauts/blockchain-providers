package network.arkane.provider;

import network.arkane.provider.core.model.blockchain.NodeProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VechainAutoConfiguration {

    public VechainAutoConfiguration(final @Value("${vechain.node}") String vechainProvider) {
        if (StringUtils.isBlank(vechainProvider)) {
            throw new IllegalArgumentException("Providing a vechain node is necessary (vechain.node)");
        }
        NodeProvider nodeProvider = NodeProvider.getNodeProvider();
        nodeProvider.setProvider(vechainProvider);
        nodeProvider.setTimeout(10000);
    }
}
