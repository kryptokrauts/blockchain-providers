package network.arkane.provider.opensea;

import network.arkane.provider.web3j.EvmWeb3jGateway;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;

import java.util.Collections;
import java.util.List;

public class NonFungibleContractTypeMapper {

    public static final String ERC_1155_SIGNATURE = "d9b67a26";
    public static final String ERC_1155 = "ERC_1155";
    public static final String ERC_721 = "ERC_721";
    private EvmWeb3jGateway evmWeb3jGateway;

    public NonFungibleContractTypeMapper(EvmWeb3jGateway evmWeb3jGateway) {
        this.evmWeb3jGateway = evmWeb3jGateway;
    }

    public String getType(String address) {
        if (isErc1155(address)) {
            return ERC_1155;
        } else {
            return ERC_721;
        }
    }

    private boolean isErc1155(String address) {
        final byte[] erc1155Bytes = Hex.decode(ERC_1155_SIGNATURE);
        Function function = new Function("supportsInterface",
                                         Collections.singletonList(new Bytes4(erc1155Bytes)),
                                         Collections.singletonList(new TypeReference<Bool>() {})
        );

        String data = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall ethCall = null;
        try {
            ethCall = evmWeb3jGateway.web3().ethCall(
                    Transaction.createEthCallTransaction("0x0000000000000000000000000000000000000000", address, data),
                    DefaultBlockParameterName.LATEST).send();
            String value = ethCall.getValue();
            List<Type> decoded = FunctionReturnDecoder.decode(value, function.getOutputParameters());
            return !decoded.isEmpty() && Boolean.parseBoolean(decoded.get(0).getValue().toString());
        } catch (Exception e) {
            return false;
        }

    }

}
