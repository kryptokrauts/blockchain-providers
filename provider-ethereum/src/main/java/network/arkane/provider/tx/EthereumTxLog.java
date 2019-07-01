package network.arkane.provider.tx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class EthereumTxLog {

    private BigInteger logIndex;
    private String data;
    private String type;
    private List<String> topics;
}
