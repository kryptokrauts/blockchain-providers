package network.arkane.business.token.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.chain.SecretType;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class TokenDto {
    private SecretType secretType;
    private String contractAddress;
    private BigInteger tokenId;
    private TokenType tokenType;
    private BigInteger amount;

    @Builder
    public TokenDto(SecretType secretType, String contractAddress, BigInteger tokenId, TokenType tokenType, BigInteger amount) {
        this.secretType = secretType;
        this.contractAddress = contractAddress;
        this.tokenId = tokenId;
        this.tokenType = tokenType;
        this.amount = amount;
    }
}
