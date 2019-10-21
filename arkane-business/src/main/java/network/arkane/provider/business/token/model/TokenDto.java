package network.arkane.provider.business.token.model;

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
    private String imageUrl;
    private String imagePreviewUrl;
    private String imageThumbnailUrl;
    private String url;
    private String backgroundColor;

    @Builder
    public TokenDto(SecretType secretType,
                    String contractAddress,
                    BigInteger tokenId,
                    TokenType tokenType,
                    BigInteger amount,
                    String imageUrl,
                    String imagePreviewUrl,
                    String imageThumbnailUrl,
                    String url,
                    String backgroundColor) {
        this.secretType = secretType;
        this.contractAddress = contractAddress;
        this.tokenId = tokenId;
        this.tokenType = tokenType;
        this.amount = amount;
        this.imageUrl = imageUrl;
        this.imagePreviewUrl = imagePreviewUrl;
        this.imageThumbnailUrl = imageThumbnailUrl;
        this.url = url;
        this.backgroundColor = backgroundColor;
    }
}
