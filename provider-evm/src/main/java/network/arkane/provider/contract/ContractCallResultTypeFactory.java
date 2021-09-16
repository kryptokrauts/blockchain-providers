package network.arkane.provider.contract;

import network.arkane.provider.BytesUtils;
import network.arkane.provider.Prefix;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.NumericType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;

import java.math.BigInteger;

public class ContractCallResultTypeFactory {

    private ContractCallResultTypeFactory() {
    }

    public static <T> ContractCallResultType getType(final Type<T> web3jType) {
        if (web3jType == null) return null;

        if (web3jType instanceof BytesType) {
            return ContractCallHexResult.builder()
                                        .type(web3jType.getTypeAsString())
                                        .value(BytesUtils.toHexString((byte[]) web3jType.getValue(), Prefix.ZeroLowerX))
                                        .build();
        }

        if (web3jType instanceof Utf8String || web3jType instanceof Address) {
            return ContractCallStringResult.builder()
                                           .type(web3jType.getTypeAsString())
                                           .value((String) web3jType.getValue())
                                           .build();
        }
        if (web3jType instanceof NumericType) {
            return ContractCallNumericResult.builder()
                                            .type(web3jType.getTypeAsString())
                                            .value((BigInteger) web3jType.getValue())
                                            .build();
        }
        if (web3jType instanceof Bool) {
            return ContractCallBooleanResult.builder()
                                            .type(web3jType.getTypeAsString())
                                            .value((Boolean) web3jType.getValue())
                                            .build();
        }
        throw new IllegalArgumentException("Invalid type: " + web3jType.getClass());

    }

}
