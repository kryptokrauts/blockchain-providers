
package network.arkane.provider.sign.domain;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataSignature extends Signature {

    private String data;

    @Builder
    public DataSignature(String data) {
        this.data = data;
    }
}
