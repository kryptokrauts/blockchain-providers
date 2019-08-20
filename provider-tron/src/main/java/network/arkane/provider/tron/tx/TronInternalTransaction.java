package network.arkane.provider.tron.tx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class TronInternalTransaction {

    private String callerAddress;
    private String transferToAddress;
    private List<TronCallValueInfo> callValueInfo;
    private String note;
    private Boolean rejected;

}
