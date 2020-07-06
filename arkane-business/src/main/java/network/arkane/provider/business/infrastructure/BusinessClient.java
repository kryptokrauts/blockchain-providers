package network.arkane.provider.business.infrastructure;

import network.arkane.provider.business.token.model.TokenContract;
import network.arkane.provider.business.token.model.TokenDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigInteger;
import java.util.List;

public interface BusinessClient {

    List<TokenDto> getTokensForAddress(@PathVariable("userAddress") final String userAddress);

    TokenContract getContract(@PathVariable("contractAddress") final String contractAddress);

    TokenDto getToken(@PathVariable("contractAddress") final String contractAddress,
                      @PathVariable("tokenId") final BigInteger tokenId);
}
