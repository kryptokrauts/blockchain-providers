package network.arkane.business.token;

import network.arkane.business.infrastructure.BusinessClient;
import network.arkane.business.token.model.TokenDto;
import org.springframework.stereotype.Component;

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
}
