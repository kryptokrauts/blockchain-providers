package network.arkane.provider.neo.sign;

import io.neow3j.contract.ContractInvocation;
import io.neow3j.contract.ContractParameter;
import io.neow3j.contract.ScriptHash;
import io.neow3j.protocol.Neow3j;
import io.neow3j.transaction.InvocationTransaction;
import io.neow3j.utils.Numeric;
import io.neow3j.wallet.Account;
import lombok.SneakyThrows;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class NeoContractExecutionSigner implements Signer<NeoContractExecutionSignable, NeoSecretKey> {

    private Neow3j neow3j;

    public NeoContractExecutionSigner(final Neow3j neow3j) {
        this.neow3j = neow3j;
    }

    @Override
    @SneakyThrows
    public TransactionSignature createSignature(final NeoContractExecutionSignable signable,
                                                final NeoSecretKey key) {


        Account account = Account.fromECKeyPair(key.getKey()).build();
        updateAccountAssetBalances(account);

        ContractInvocation.Builder builder = new ContractInvocation.Builder(neow3j);
        builder = builder
                .account(account)
                .contractScriptHash(new ScriptHash(signable.getContractScriptHash()))
                .networkFee(StringUtils.isBlank(signable.getNetworkFee()) ? "0.1" : signable.getNetworkFee())
                .function(signable.getFunctionName());

        for (NeoContractParameter input : signable.getInputs()) {
            builder = builder.parameter(mapInput(input.getType(), input.getValue()));
        }

        ContractInvocation invoc = builder
                .build()
                .sign();

        InvocationTransaction transaction = invoc.getTransaction();
        String rawTx = Numeric.toHexStringNoPrefix(transaction.toArray());

        return TransactionSignature.signTransactionBuilder().signedTransaction(rawTx).build();
    }

    private ContractParameter mapInput(String type, String value) {
        if (type.endsWith("[]")) {
            return ContractParameter.array(
                    Arrays.stream(parseArray(value))
                          .map(x -> mapInput(type.substring(0, type.length() - 2), x))
                          .collect(Collectors.toList()));
        }
        switch (type.toLowerCase()) {
            case "signature":
                return ContractParameter.signature(value);
            case "boolean":
                return ContractParameter.bool(Boolean.parseBoolean(value));
            case "integer":
                return ContractParameter.integer(new BigInteger(value));
            case "hash160":
                return ContractParameter.hash160(value);
            case "hash256":
                return ContractParameter.hash256(value);
            case "byte_array":
                return ContractParameter.byteArray(value);
            case "address":
                return ContractParameter.byteArrayFromAddress(value);
            case "public_key":
                return ContractParameter.publicKey(value);
            case "string":
                return ContractParameter.string(value);
            case "array":
                return ContractParameter.array(
                        Arrays.stream(parseArray(value))
                              .map(x -> mapInput("string", x))
                              .collect(Collectors.toList()));
            default:
                throw new RuntimeException("Cannot map input to a contract input");
        }

    }

    private String[] parseArray(String value) {
        return value.substring(0, value.length() - 1)
                    .substring(1)
                    .replaceAll(" ", "")
                    .split(",");
    }

    @SneakyThrows
    private void updateAccountAssetBalances(Account account) {
        account.updateAssetBalances(neow3j);
    }
}
