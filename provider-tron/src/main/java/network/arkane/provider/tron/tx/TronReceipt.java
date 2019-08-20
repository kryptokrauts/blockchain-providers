package network.arkane.provider.tron.tx;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class TronReceipt {

    private Long energyUsage;
    private Long energyFee;
    private Long originEnergyUsage;
    private Long energyUsageTotal;
    private Long netUsage;
    private Long netFee;
    private String result;

}
