package network.arkane.provider.clients;

import network.arkane.provider.Prefix;
import network.arkane.provider.core.model.blockchain.ContractCall;
import network.arkane.provider.core.model.blockchain.ContractCallResult;
import network.arkane.provider.core.model.blockchain.TransferResult;
import network.arkane.provider.core.model.clients.Address;
import network.arkane.provider.core.model.clients.Amount;
import network.arkane.provider.core.model.clients.ERC20Contract;
import network.arkane.provider.core.model.clients.ERC20Token;
import network.arkane.provider.core.model.clients.ProtoTypeContract;
import network.arkane.provider.core.model.clients.Revision;
import network.arkane.provider.core.model.clients.ToClause;
import network.arkane.provider.core.model.clients.base.AbiDefinition;
import network.arkane.provider.core.model.exception.ClientArgumentException;
import network.arkane.provider.core.model.exception.ClientIOException;
import network.arkane.provider.utils.crypto.ECKeyPair;

import java.io.IOException;
import java.math.BigInteger;

public class ERC20ContractClient extends TransactionClient {

    /**
     * Get amount from ERC20 contract.
     *
     * @param address  address of token holder.
     * @param token    {@link ERC20Token} required, the token {@link ERC20Token}
     * @param revision {@link Revision} if it is null, it will fallback to default
     *                 {@link Revision#BEST}
     * @return {@link Amount}
     * @throws ClientIOException {@link ClientIOException}
     */
    public static Amount getERC20Balance(Address address, ERC20Token token, Revision revision)
            throws IOException {
        Address contractAddr = token.getContractAddress();
        Revision currRevision = revision;
        if (currRevision == null) {
            currRevision = Revision.BEST;
        }
        AbiDefinition abiDefinition = ERC20Contract.defaultERC20Contract.findAbiDefinition("balanceOf");
        ContractCall call = ERC20Contract.buildCall(abiDefinition, address.toHexString(null));
        ContractCallResult contractCallResult = callContract(call, contractAddr, currRevision);
        if (contractCallResult == null) {
            return null;
        }
        return contractCallResult.getBalance(token);
    }

    public static String name(final Address address) throws IOException {
        Revision currRevision = Revision.BEST;
        AbiDefinition abiDefinition = ERC20Contract.defaultERC20Contract.findAbiDefinition("name");
        ContractCall call = ERC20Contract.buildCall(abiDefinition);
        ContractCallResult contractCallResult = callContract(call, address, currRevision);
        return contractCallResult.getData();
    }

    public static String symbol(final Address address) throws IOException {
        Revision currRevision = Revision.BEST;
        AbiDefinition abiDefinition = ERC20Contract.defaultERC20Contract.findAbiDefinition("symbol");
        ContractCall call = ERC20Contract.buildCall(abiDefinition);
        ContractCallResult contractCallResult = callContract(call, address, currRevision);
        return contractCallResult.getData();
    }

    public static BigInteger decimals(final Address address) throws IOException {
        Revision currRevision = Revision.BEST;
        AbiDefinition abiDefinition = ERC20Contract.defaultERC20Contract.findAbiDefinition("decimals");
        ContractCall call = ERC20Contract.buildCall(abiDefinition);
        ContractCallResult contractCallResult = callContract(call, address, currRevision);
        return new BigInteger(contractCallResult.getData());
    }

    /**
     * Transfer ERC20 token
     *
     * @param receivers  {@link Address} array
     * @param amounts    {@link Amount} array
     * @param gas        gas at least 7000
     * @param gasCoef    gas coef
     * @param expiration expiration
     * @param keyPair    your private key.
     * @return {@link TransferResult}
     * @throws ClientIOException
     */
    public static TransferResult transferERC20Token(Address[] receivers, Amount[] amounts, int gas, byte gasCoef,
                                                    int expiration, ECKeyPair keyPair) throws IOException {
        if (receivers == null) {
            throw ClientArgumentException.exception("receivers is null");
        }
        if (amounts == null) {
            throw ClientArgumentException.exception("amounts is null");
        }
        if (receivers.length != amounts.length) {
            throw ClientArgumentException.exception("receivers length equal to amounts length.");
        }

        AbiDefinition abi = ERC20Contract.defaultERC20Contract.findAbiDefinition("transfer");
        if (abi == null) {
            throw new IllegalArgumentException("Can not find abi master method");
        }
        ToClause[] clauses = new ToClause[receivers.length];
        for (int index = 0; index < receivers.length; index++) {
            if (!(amounts[index].getAbstractToken() instanceof ERC20Token)) {
                throw ClientArgumentException.exception("Token is not ERC20");
            }
            ERC20Token token = (ERC20Token) amounts[index].getAbstractToken();
            clauses[index] = ProtoTypeContract.buildToClause(token.getContractAddress(), abi,
                                                             receivers[index].toHexString(Prefix.ZeroLowerX), amounts[index].toBigInteger());

        }
        return invokeContractMethod(clauses, gas, gasCoef, expiration, keyPair);

    }
}
