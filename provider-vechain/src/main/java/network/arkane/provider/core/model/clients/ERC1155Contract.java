package network.arkane.provider.core.model.clients;

import network.arkane.provider.core.model.clients.base.AbiDefinition;
import network.arkane.provider.core.model.clients.base.AbstractContract;

import java.math.BigInteger;

public class ERC1155Contract extends AbstractContract {
    private static final String ERC1155ABIString = "[{\"inputs\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"symbol\",\"type\":\"string\"}],\"payable\":false,"
                                                   + "\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,"
                                                   + "\"name\":\"_owner\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"_operator\",\"type\":\"address\"},"
                                                   + "{\"indexed\":false,\"name\":\"_approved\",\"type\":\"bool\"}],\"name\":\"ApprovalForAll\",\"type\":\"event\"},"
                                                   + "{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"_operator\",\"type\":\"address\"},{\"indexed\":true,"
                                                   + "\"name\":\"_from\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"_to\",\"type\":\"address\"},{\"indexed\":false,"
                                                   + "\"name\":\"_ids\",\"type\":\"uint256[]\"},{\"indexed\":false,\"name\":\"_values\",\"type\":\"uint256[]\"}],"
                                                   + "\"name\":\"TransferBatch\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"_operator\","
                                                   + "\"type\":\"address\"},{\"indexed\":true,\"name\":\"_from\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"_to\","
                                                   + "\"type\":\"address\"},{\"indexed\":false,\"name\":\"_id\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"_value\","
                                                   + "\"type\":\"uint256\"}],\"name\":\"TransferSingle\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,"
                                                   + "\"name\":\"_value\",\"type\":\"string\"},{\"indexed\":true,\"name\":\"_id\",\"type\":\"uint256\"}],\"name\":\"URI\","
                                                   + "\"type\":\"event\"},{\"constant\":true,\"inputs\":[{\"name\":\"_owner\",\"type\":\"address\"},{\"name\":\"_id\","
                                                   + "\"type\":\"uint256\"}],\"name\":\"balanceOf\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,"
                                                   + "\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_owners\","
                                                   + "\"type\":\"address[]\"},{\"name\":\"_ids\",\"type\":\"uint256[]\"}],\"name\":\"balanceOfBatch\","
                                                   + "\"outputs\":[{\"name\":\"\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},"
                                                   + "{\"constant\":false,\"inputs\":[{\"name\":\"_newOwner\",\"type\":\"address\"}],\"name\":\"changeOwner\",\"outputs\":[],"
                                                   + "\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,"
                                                   + "\"inputs\":[{\"name\":\"_uri\",\"type\":\"string\"},{\"name\":\"_isNF\",\"type\":\"bool\"}],\"name\":\"create\","
                                                   + "\"outputs\":[{\"name\":\"_type\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\","
                                                   + "\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"creators\","
                                                   + "\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},"
                                                   + "{\"constant\":true,\"inputs\":[{\"name\":\"_id\",\"type\":\"uint256\"}],\"name\":\"getNonFungibleBaseType\","
                                                   + "\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"pure\",\"type\":\"function\"},"
                                                   + "{\"constant\":true,\"inputs\":[{\"name\":\"_id\",\"type\":\"uint256\"}],\"name\":\"getNonFungibleIndex\","
                                                   + "\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"pure\",\"type\":\"function\"},"
                                                   + "{\"constant\":true,\"inputs\":[{\"name\":\"_owner\",\"type\":\"address\"},{\"name\":\"_operator\",\"type\":\"address\"}],"
                                                   + "\"name\":\"isApprovedForAll\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,"
                                                   + "\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_id\","
                                                   + "\"type\":\"uint256\"}],\"name\":\"isFungible\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,"
                                                   + "\"stateMutability\":\"pure\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_id\","
                                                   + "\"type\":\"uint256\"}],\"name\":\"isNonFungible\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,"
                                                   + "\"stateMutability\":\"pure\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_id\","
                                                   + "\"type\":\"uint256\"}],\"name\":\"isNonFungibleBaseType\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],"
                                                   + "\"payable\":false,\"stateMutability\":\"pure\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_id\","
                                                   + "\"type\":\"uint256\"}],\"name\":\"isNonFungibleItem\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,"
                                                   + "\"stateMutability\":\"pure\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"}],"
                                                   + "\"name\":\"maxIndex\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\","
                                                   + "\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_id\",\"type\":\"uint256\"},{\"name\":\"_to\","
                                                   + "\"type\":\"address[]\"},{\"name\":\"_quantities\",\"type\":\"uint256[]\"}],\"name\":\"mintFungible\",\"outputs\":[],"
                                                   + "\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,"
                                                   + "\"inputs\":[{\"name\":\"_type\",\"type\":\"uint256\"},{\"name\":\"_to\",\"type\":\"address[]\"}],"
                                                   + "\"name\":\"mintNonFungible\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},"
                                                   + "{\"constant\":true,\"inputs\":[],\"name\":\"name\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,"
                                                   + "\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"owner\","
                                                   + "\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},"
                                                   + "{\"constant\":true,\"inputs\":[{\"name\":\"_id\",\"type\":\"uint256\"}],\"name\":\"ownerOf\",\"outputs\":[{\"name\":\"\","
                                                   + "\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,"
                                                   + "\"inputs\":[{\"name\":\"_from\",\"type\":\"address\"},{\"name\":\"_to\",\"type\":\"address\"},{\"name\":\"_ids\","
                                                   + "\"type\":\"uint256[]\"},{\"name\":\"_values\",\"type\":\"uint256[]\"},{\"name\":\"_data\",\"type\":\"bytes\"}],"
                                                   + "\"name\":\"safeBatchTransferFrom\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\","
                                                   + "\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_from\",\"type\":\"address\"},{\"name\":\"_to\","
                                                   + "\"type\":\"address\"},{\"name\":\"_id\",\"type\":\"uint256\"},{\"name\":\"_value\",\"type\":\"uint256\"},"
                                                   + "{\"name\":\"_data\",\"type\":\"bytes\"}],\"name\":\"safeTransferFrom\",\"outputs\":[],\"payable\":false,"
                                                   + "\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_operator\","
                                                   + "\"type\":\"address\"},{\"name\":\"_approved\",\"type\":\"bool\"}],\"name\":\"setApprovalForAll\",\"outputs\":[],"
                                                   + "\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,"
                                                   + "\"inputs\":[{\"name\":\"_type\",\"type\":\"uint256\"},{\"name\":\"_uri\",\"type\":\"string\"}],\"name\":\"setUri\","
                                                   + "\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,"
                                                   + "\"inputs\":[{\"name\":\"_interfaceId\",\"type\":\"bytes4\"}],\"name\":\"supportsInterface\",\"outputs\":[{\"name\":\"\","
                                                   + "\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],"
                                                   + "\"name\":\"symbol\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\","
                                                   + "\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_type\",\"type\":\"uint256\"}],\"name\":\"uri\","
                                                   + "\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]";


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
