package network.arkane.provider.gas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VechainEstimateGasCall {
    private String data;
    private String caller;
    private String value;
    @JsonIgnore
    private String to;
}
