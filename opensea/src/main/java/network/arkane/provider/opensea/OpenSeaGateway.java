package network.arkane.provider.opensea;

import com.google.common.util.concurrent.RateLimiter;
import network.arkane.provider.opensea.domain.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Component
public class OpenSeaGateway {

    private final OpenSeaClient openSeaClient;
    private final RateLimiter rateLimiter;

    @Autowired
    public OpenSeaGateway(final OpenSeaClient openSeaClient,
                          @Value("${opensea.maxrequestspersecond:10000}") final Long maxRequestsPerSecond) {
        this.openSeaClient = openSeaClient;
        this.rateLimiter = RateLimiter.create(maxRequestsPerSecond);
    }

    public List<Asset> listAssets(final String owner, final String... contractAddresses) {
        return executeWithRateLimiter(() -> this.openSeaClient.listAssets(owner, resolveContractAddresses(contractAddresses)).getAssets());
    }

    public Asset getAsset(final String contractAddress, final String tokenId) {
        return executeWithRateLimiter(() -> this.openSeaClient.getAsset(contractAddress, tokenId));
    }

    private List<String> resolveContractAddresses(final String[] contractAddresses) {
        return contractAddresses != null ? Arrays.asList(contractAddresses) : new ArrayList<>();
    }

    private <T> T executeWithRateLimiter(Callable<T> callable) {
        if (rateLimiter.tryAcquire(10, TimeUnit.SECONDS)) {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("OpenSea endpoint too busy");
        }
    }
}
