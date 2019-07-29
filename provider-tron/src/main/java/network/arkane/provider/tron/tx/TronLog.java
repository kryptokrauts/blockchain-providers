package network.arkane.provider.tron.tx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class TronLog {
    private String address;
    private List<String> topics;
    private String data;
}
