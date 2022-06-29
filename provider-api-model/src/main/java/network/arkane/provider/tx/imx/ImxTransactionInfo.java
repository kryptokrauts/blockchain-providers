package network.arkane.provider.tx.imx;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.tx.TxInfo;

@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({@JsonSubTypes.Type(value = ImxTransactionMintInfo.class, name = "MINT"),
        @JsonSubTypes.Type(value = ImxTransactionTransferInfo.class, name = "TRANSFER")})

public class ImxTransactionInfo extends TxInfo {
    private String timestamp;
    private ImxTransactionInfoType type;
    private String user;
}
