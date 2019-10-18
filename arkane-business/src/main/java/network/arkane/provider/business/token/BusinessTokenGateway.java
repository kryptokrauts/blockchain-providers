package network.arkane.provider.business.token;

import network.arkane.provider.business.infrastructure.BusinessClient;
import network.arkane.provider.business.token.model.TokenContract;
import network.arkane.provider.business.token.model.TokenDto;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class BusinessTokenGateway {

    private BusinessClient businessClient;

    public BusinessTokenGateway(BusinessClient businessClient) {
        this.businessClient = businessClient;
    }

    public List<TokenDto> getTokensForAddress(final String address) {
        return businessClient.getTokensForAddress(address);
    }

    public TokenContract getContract(final String contractAddress) {
        return businessClient.getContract(contractAddress);
    }

    public TokenDto getToken(final String contractAddress,
                             final BigInteger tokenId) {
        return businessClient.getToken(contractAddress, tokenId);
    }
}
