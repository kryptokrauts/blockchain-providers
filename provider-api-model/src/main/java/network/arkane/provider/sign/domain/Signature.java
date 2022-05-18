package network.arkane.provider.sign.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXISTING_PROPERTY;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes( {
                       @JsonSubTypes.Type(value = HexSignature.class, name = SignatureType.Values.HEX_SIGNATURE),
                       @JsonSubTypes.Type(value = SubmittedAndSignedTransactionSignature.class, name = SignatureType.Values.SUBMITTED_AND_SIGNED_TRANSACTION_SIGNATURE),
                       @JsonSubTypes.Type(value = TransactionSignature.class, name = SignatureType.Values.TRANSACTION_SIGNATURE),
                       @JsonSubTypes.Type(value = RawSignature.class, name = SignatureType.Values.RAW_SIGNATURE),
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Signature {
    private SignatureType type;
}
