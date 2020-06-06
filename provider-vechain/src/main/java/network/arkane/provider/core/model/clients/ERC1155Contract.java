package network.arkane.provider.core.model.clients;

import network.arkane.provider.core.model.clients.base.AbiDefinition;
import network.arkane.provider.core.model.clients.base.AbstractContract;

import java.math.BigInteger;

public class ERC1155Contract extends AbstractContract {
    private static final String ERC1155ABIString = "[{\"inputs\":[{\"internalType\":\"string\",\"name\":\"name\",\"type\":\"string\"},{\"internalType\":\"string\","
                                                   + "\"name\":\"symbol\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},"
                                                   + "{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"_owner\",\"type\":\"address\"},"
                                                   + "{\"indexed\":true,\"internalType\":\"address\",\"name\":\"_operator\",\"type\":\"address\"},{\"indexed\":false,"
                                                   + "\"internalType\":\"bool\",\"name\":\"_approved\",\"type\":\"bool\"}],\"name\":\"ApprovalForAll\",\"type\":\"event\"},"
                                                   + "{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"_operator\","
                                                   + "\"type\":\"address\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"_from\",\"type\":\"address\"},"
                                                   + "{\"indexed\":true,\"internalType\":\"address\",\"name\":\"_to\",\"type\":\"address\"},{\"indexed\":false,"
                                                   + "\"internalType\":\"uint256[]\",\"name\":\"_ids\",\"type\":\"uint256[]\"},{\"indexed\":false,\"internalType\":\"uint256[]\","
                                                   + "\"name\":\"_values\",\"type\":\"uint256[]\"}],\"name\":\"TransferBatch\",\"type\":\"event\"},{\"anonymous\":false,"
                                                   + "\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"_operator\",\"type\":\"address\"},{\"indexed\":true,"
                                                   + "\"internalType\":\"address\",\"name\":\"_from\",\"type\":\"address\"},{\"indexed\":true,\"internalType\":\"address\","
                                                   + "\"name\":\"_to\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"_id\","
                                                   + "\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"_value\",\"type\":\"uint256\"}],"
                                                   + "\"name\":\"TransferSingle\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,"
                                                   + "\"internalType\":\"string\",\"name\":\"_value\",\"type\":\"string\"},{\"indexed\":true,\"internalType\":\"uint256\","
                                                   + "\"name\":\"_id\",\"type\":\"uint256\"}],\"name\":\"URI\",\"type\":\"event\"},{\"constant\":true,"
                                                   + "\"inputs\":[{\"internalType\":\"address\",\"name\":\"_owner\",\"type\":\"address\"},{\"internalType\":\"uint256\","
                                                   + "\"name\":\"_id\",\"type\":\"uint256\"}],\"name\":\"balanceOf\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\","
                                                   + "\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,"
                                                   + "\"inputs\":[{\"internalType\":\"address[]\",\"name\":\"_owners\",\"type\":\"address[]\"},{\"internalType\":\"uint256[]\","
                                                   + "\"name\":\"_ids\",\"type\":\"uint256[]\"}],\"name\":\"balanceOfBatch\",\"outputs\":[{\"internalType\":\"uint256[]\","
                                                   + "\"name\":\"\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},"
                                                   + "{\"constant\":false,\"inputs\":[{\"internalType\":\"address\",\"name\":\"_newOwner\",\"type\":\"address\"}],"
                                                   + "\"name\":\"changeOwner\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},"
                                                   + "{\"constant\":false,\"inputs\":[{\"internalType\":\"string\",\"name\":\"_uri\",\"type\":\"string\"},"
                                                   + "{\"internalType\":\"bool\",\"name\":\"_isNF\",\"type\":\"bool\"}],\"name\":\"create\","
                                                   + "\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"_type\",\"type\":\"uint256\"}],\"payable\":false,"
                                                   + "\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"internalType\":\"uint256\","
                                                   + "\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"creators\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\","
                                                   + "\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,"
                                                   + "\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"_id\",\"type\":\"uint256\"}],\"name\":\"getNonFungibleBaseType\","
                                                   + "\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,"
                                                   + "\"stateMutability\":\"pure\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"internalType\":\"uint256\","
                                                   + "\"name\":\"_id\",\"type\":\"uint256\"}],\"name\":\"getNonFungibleIndex\",\"outputs\":[{\"internalType\":\"uint256\","
                                                   + "\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"pure\",\"type\":\"function\"},"
                                                   + "{\"constant\":true,\"inputs\":[{\"internalType\":\"address\",\"name\":\"_owner\",\"type\":\"address\"},"
                                                   + "{\"internalType\":\"address\",\"name\":\"_operator\",\"type\":\"address\"}],\"name\":\"isApprovedForAll\","
                                                   + "\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\","
                                                   + "\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"_id\","
                                                   + "\"type\":\"uint256\"}],\"name\":\"isFungible\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],"
                                                   + "\"payable\":false,\"stateMutability\":\"pure\",\"type\":\"function\"},{\"constant\":true,"
                                                   + "\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"_id\",\"type\":\"uint256\"}],\"name\":\"isNonFungible\","
                                                   + "\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"pure\","
                                                   + "\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"_id\","
                                                   + "\"type\":\"uint256\"}],\"name\":\"isNonFungibleBaseType\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\","
                                                   + "\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"pure\",\"type\":\"function\"},{\"constant\":true,"
                                                   + "\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"_id\",\"type\":\"uint256\"}],\"name\":\"isNonFungibleItem\","
                                                   + "\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"pure\","
                                                   + "\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],"
                                                   + "\"name\":\"maxIndex\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,"
                                                   + "\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"internalType\":\"uint256\","
                                                   + "\"name\":\"_id\",\"type\":\"uint256\"},{\"internalType\":\"address[]\",\"name\":\"_to\",\"type\":\"address[]\"},"
                                                   + "{\"internalType\":\"uint256[]\",\"name\":\"_quantities\",\"type\":\"uint256[]\"}],\"name\":\"mintFungible\",\"outputs\":[],"
                                                   + "\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,"
                                                   + "\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"_type\",\"type\":\"uint256\"},{\"internalType\":\"address[]\","
                                                   + "\"name\":\"_to\",\"type\":\"address[]\"}],\"name\":\"mintNonFungible\",\"outputs\":[],\"payable\":false,"
                                                   + "\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"name\","
                                                   + "\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"payable\":false,"
                                                   + "\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"owner\","
                                                   + "\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"payable\":false,"
                                                   + "\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"internalType\":\"uint256\","
                                                   + "\"name\":\"_id\",\"type\":\"uint256\"}],\"name\":\"ownerOf\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\","
                                                   + "\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,"
                                                   + "\"inputs\":[{\"internalType\":\"address\",\"name\":\"_from\",\"type\":\"address\"},{\"internalType\":\"address\","
                                                   + "\"name\":\"_to\",\"type\":\"address\"},{\"internalType\":\"uint256[]\",\"name\":\"_ids\",\"type\":\"uint256[]\"},"
                                                   + "{\"internalType\":\"uint256[]\",\"name\":\"_values\",\"type\":\"uint256[]\"},{\"internalType\":\"bytes\","
                                                   + "\"name\":\"_data\",\"type\":\"bytes\"}],\"name\":\"safeBatchTransferFrom\",\"outputs\":[],\"payable\":false,"
                                                   + "\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"internalType\":\"address\","
                                                   + "\"name\":\"_from\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"_to\",\"type\":\"address\"},"
                                                   + "{\"internalType\":\"uint256\",\"name\":\"_id\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"_value\","
                                                   + "\"type\":\"uint256\"},{\"internalType\":\"bytes\",\"name\":\"_data\",\"type\":\"bytes\"}],\"name\":\"safeTransferFrom\","
                                                   + "\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,"
                                                   + "\"inputs\":[{\"internalType\":\"address\",\"name\":\"_operator\",\"type\":\"address\"},{\"internalType\":\"bool\","
                                                   + "\"name\":\"_approved\",\"type\":\"bool\"}],\"name\":\"setApprovalForAll\",\"outputs\":[],\"payable\":false,"
                                                   + "\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"internalType\":\"uint256\","
                                                   + "\"name\":\"_type\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"_uri\",\"type\":\"string\"}],"
                                                   + "\"name\":\"setUri\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},"
                                                   + "{\"constant\":true,\"inputs\":[{\"internalType\":\"bytes4\",\"name\":\"_interfaceId\",\"type\":\"bytes4\"}],"
                                                   + "\"name\":\"supportsInterface\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,"
                                                   + "\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"symbol\","
                                                   + "\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"payable\":false,"
                                                   + "\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"internalType\":\"uint256\","
                                                   + "\"name\":\"_type\",\"type\":\"uint256\"}],\"name\":\"uri\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\","
                                                   + "\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]";


    public ERC1155Contract() {
        super(ERC1155ABIString);
    }

    public static final ERC1155Contract defaultERC1155Contract = new ERC1155Contract();

    public static String createType(final String tokenURI,
                                    final boolean nonFungible) {
        final AbiDefinition abiDefinition = ERC1155Contract.defaultERC1155Contract.findAbiDefinition("create");
        return buildData(abiDefinition,
                  tokenURI,
                  nonFungible);
    }

    public static String mintFungible(final BigInteger type,
                               final String[] to,
                               final BigInteger[] amount) {
        final AbiDefinition abiDefinition = ERC1155Contract.defaultERC1155Contract.findAbiDefinition("mintFungible");
        return buildData(abiDefinition,
                         type,
                         to,
                         amount);
    }

    public static String mintNonFungible(final BigInteger type,
                                  final String[] to) {
        final AbiDefinition abiDefinition = ERC1155Contract.defaultERC1155Contract.findAbiDefinition("mintNonFungible");
        return buildData(abiDefinition,
                         type,
                         to);
    }
}
