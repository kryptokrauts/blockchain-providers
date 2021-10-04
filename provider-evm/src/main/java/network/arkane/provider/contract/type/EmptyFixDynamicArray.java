package network.arkane.provider.contract.type;

import org.web3j.abi.datatypes.AbiTypes;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.StructType;
import org.web3j.abi.datatypes.Type;

import java.util.List;

/**
 * This should be removed and standard dynamic arrays should be used once
 * https://github.com/web3j/web3j/issues/1474 is fixed and merged into a release
 */
@Deprecated
public class EmptyFixDynamicArray<T extends Type> extends DynamicArray<T> {


    @Deprecated
    @SafeVarargs
    @SuppressWarnings( {"unchecked"})
    public EmptyFixDynamicArray(T... values) {
        super(
                StructType.class.isAssignableFrom(values[0].getClass())
                ? (Class<T>) values[0].getClass()
                : (Class<T>) AbiTypes.getType(values[0].getTypeAsString()),
                values);
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public EmptyFixDynamicArray(List<T> values) {
        super(
                StructType.class.isAssignableFrom(values.get(0).getClass())
                ? (Class<T>) values.get(0).getClass()
                : (Class<T>) AbiTypes.getType(values.get(0).getTypeAsString()),
                values);
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    private EmptyFixDynamicArray(String type) {
        super((Class<T>) AbiTypes.getType(type));
    }

    @Deprecated
    public static EmptyFixDynamicArray empty(String type) {
        return new EmptyFixDynamicArray(type);
    }

    public EmptyFixDynamicArray(Class<T> type,
                                List<T> values) {
        super(type, values);
    }

    @Override
    public int bytes32PaddedLength() {
        return super.bytes32PaddedLength() + MAX_BYTE_LENGTH;
    }

    @SafeVarargs
    public EmptyFixDynamicArray(Class<T> type,
                                T... values) {
        super(type, values);
    }

    @Override
    public String getTypeAsString() {
        String type;
        if (!value.isEmpty() && StructType.class.isAssignableFrom(value.get(0).getClass())) {
            type = value.get(0).getTypeAsString();
        } else {
            type = AbiTypes.getTypeAString(getComponentType());
        }
        return type + "[]";
    }
}
