package network.arkane.provider.nonfungable;

import network.arkane.provider.gateway.EthereumWeb3JGateway;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class NonFungibleContractTypeMapper {

    public static final String ERC_1155_SIGNATURE = "d9b67a26";
    public static final String ERC_1155 = "ERC_1155";
    public static final String ERC_721 = "ERC_721";
    private EthereumWeb3JGateway ethereumWeb3JGateway;

    public NonFungibleContractTypeMapper(EthereumWeb3JGateway ethereumWeb3JGateway) {
        this.ethereumWeb3JGateway = ethereumWeb3JGateway;
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
            ethCall = ethereumWeb3JGateway.web3().ethCall(
                    Transaction.createEthCallTransaction("0x0000000000000000000000000000000000000000", address, data),
                    DefaultBlockParameterName.LATEST).send();
            String value = ethCall.getValue();
            List<Type> decoded = FunctionReturnDecoder.decode(value, function.getOutputParameters());
            return Boolean.parseBoolean(decoded.get(0).getValue().toString());


        } catch (IOException e) {
            return false;
        }

    }

}
