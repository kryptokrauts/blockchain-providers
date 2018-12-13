package network.arkane.provider.gas;

import network.arkane.provider.BytesUtils;
import network.arkane.provider.clients.AccountClient;
import network.arkane.provider.clients.base.AbstractClient;
import network.arkane.provider.utils.StringUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

@Component
public class VechainGasEstimator {
    private static final int TX_GAS = 5000;
    private static final int CLAUSE_GAS = 21000 - TX_GAS;
    private static final int CLAUSE_GAS_CONTRACT_CREATION = 53000 - TX_GAS;
    private static final int TX_DATA_ZERO_GAS = 4;
    private static final int TX_DATA_NON_ZERO_GAS = 68;
    public static final BigDecimal EXTRA_GAS_PCT = new BigDecimal("1.2");

    private final AccountClient accountClient;

    public VechainGasEstimator() {
        this.accountClient = new AccountClient();
    }

    public VechainEstimateGasCallResult getEstimatedGas(List<VechainEstimateGasCall> clauses) {
        BigInteger total = BigInteger.valueOf(TX_GAS);
        boolean reverted = false;
        for (VechainEstimateGasCall clause : clauses) {

            VechainEstimateGasCallResult result = getEstimatedGas(clause);
            if (result.getReverted()) {
                reverted = true;
                result.setGasUsed(new BigInteger("120000"));
            }
            total = total.add(result.getGasUsed());
        }
        return VechainEstimateGasCallResult.builder()
                                           .gasUsed(total)
                                           .reverted(reverted)
                                           .build();
    }

    public VechainEstimateGasCallResult getEstimatedGas(VechainEstimateGasCall clause) {
        HashMap<String, String> uriParams = new HashMap<>();
        uriParams.put("address", clause.getTo());
        if (clause.getData() == null) {
            clause.setData("0x");
        }
        try {
            VechainEstimateGasCallResult result = accountClient.sendPostRequest(AbstractClient.Path.GetAccountPath,
                                                                                uriParams,
                                                                                new HashMap<>(),
                                                                                clause,
                                                                                VechainEstimateGasCallResult.class);
            BigInteger gasUsed = addIntrinsicGas(result, clause.getTo(), clause.getData());
            if (!"0x".equalsIgnoreCase(clause.getData())) {
                gasUsed = new BigDecimal(gasUsed).multiply(EXTRA_GAS_PCT).toBigInteger();
            }
            result.setGasUsed(gasUsed);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BigInteger addIntrinsicGas(VechainEstimateGasCallResult result, String to, String data) {
        return new BigDecimal(result.getGasUsed()).multiply(new BigDecimal("1")).toBigInteger()
                                                  .add(new BigInteger("" + getIntrinsicGas(to, data)));
    }

    private int dataGas(String data) {
        try {
            byte[] decoded = Hex.decodeHex(BytesUtils.cleanHexPrefix(data));
            if (decoded.length == 0) {
                return 0;
            }
            int z = 0;
            int nz = 0;
            for (byte byt : decoded) {
                if (byt == 0) {
                    z += 1;
                } else {
                    nz += 1;
                }
            }
            return (TX_DATA_ZERO_GAS * z) + (TX_DATA_NON_ZERO_GAS * nz);
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
    }

    private int getIntrinsicGas(String to, String data) {
        int total = 0;
        int gas = dataGas(data);
        total += gas;
        int cgas = CLAUSE_GAS;
        if (StringUtils.isBlank(to)) {
            cgas = CLAUSE_GAS_CONTRACT_CREATION;
        }
        total += cgas;
        return total;
    }
}
