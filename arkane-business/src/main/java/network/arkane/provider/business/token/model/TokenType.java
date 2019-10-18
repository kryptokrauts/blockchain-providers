package network.arkane.provider.business.token.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
public class TokenType {

    private Long id;
    private String name;
    private String description;
    private boolean nf;
    private int decimals;
    private BigInteger typeId;
    private String transactionHash;
    private boolean confirmed;
    private Date mineDate;
    private Date transactionDate;
    private TokenContract tokenContract;
    private String properties;

    private String url;
    private String backgroundColor;

    @Builder
    public TokenType(final Long id,
                     final String name,
                     final String description,
                     final int decimals,
                     final BigInteger typeId,
                     final String transactionHash,
                     final Date mineDate,
                     final boolean confirmed,
                     final Date transactionDate,
                     final TokenContract tokenContract,
                     final boolean nf,
                     final String properties,
                     final String url,
                     final String backgroundColor) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.decimals = decimals;
        this.typeId = typeId;
        this.transactionHash = transactionHash;
        this.mineDate = mineDate;
        this.confirmed = confirmed;
        this.transactionDate = transactionDate;
        this.tokenContract = tokenContract;
        this.nf = nf;
        this.properties = properties;
        this.url = url;
        this.backgroundColor = backgroundColor;
    }
}
