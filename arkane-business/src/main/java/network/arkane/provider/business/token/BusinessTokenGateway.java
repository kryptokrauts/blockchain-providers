package network.arkane.provider.business.token;

import network.arkane.provider.business.infrastructure.BusinessClient;
import network.arkane.provider.business.token.model.TokenContract;
import network.arkane.provider.business.token.model.TokenDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class BusinessTokenGateway {

    private BusinessClient businessClient;

    public BusinessTokenGateway(BusinessClient businessClient) {
        this.businessClient = businessClient;
    }

    @Cacheable("business-list-tokens")
    public List<TokenDto> getTokensForAddress(final String address) {
        return businessClient.getTokensForAddress(address);
    }

    @Cacheable("business-contract")
    public TokenContract getContract(final String contractAddress) {
        return businessClient.getContract(contractAddress);
    }

    @Cacheable("business-token")
    public TokenDto getToken(final String contractAddress,
                             final BigInteger tokenId) {
        return businessClient.getToken(contractAddress, tokenId);
    }
}
