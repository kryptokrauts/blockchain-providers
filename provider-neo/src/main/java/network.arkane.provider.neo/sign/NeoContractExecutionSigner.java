package network.arkane.provider.neo.sign;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.neow3j.contract.ContractInvocation;
import io.neow3j.contract.ContractParameter;
import io.neow3j.contract.ScriptHash;
import io.neow3j.crypto.transaction.RawTransactionOutput;
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
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class NeoContractExecutionSigner implements Signer<NeoContractExecutionSignable, NeoSecretKey> {

    private static final String TYPE_KEY = "type";
    private static final String VALUE_KEY = "value";

    private Neow3j neow3j;
    private final ObjectMapper objectMapper;

    public NeoContractExecutionSigner(final Neow3j neow3j, final ObjectMapper objectMapper) {
        this.neow3j = neow3j;
        this.objectMapper = objectMapper;
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
                .function(signable.getFunctionName());

        if (StringUtils.isNotBlank(signable.getSystemFee())) {
            builder.systemFee(signable.getSystemFee());
        }

        if (StringUtils.isNotBlank(signable.getNetworkFee())) {
            builder.networkFee(signable.getNetworkFee());
        }

        for (JsonNode input : signable.getInputs()) {
            builder = builder.parameter(mapInput(input));
        }

        if (!CollectionUtils.isEmpty(signable.getOutputs())) {
            for (NeoAssetTransferSignable output : signable.getOutputs()) {
                builder = builder.output(new RawTransactionOutput(output.getAssetId(), output.getAmount().toString(), output.getTo()));
            }
        }

        ContractInvocation invoc = builder
                .build()
                .sign();

        InvocationTransaction transaction = invoc.getTransaction();
        String rawTx = Numeric.toHexStringNoPrefix(transaction.toArray());

        return TransactionSignature.signTransactionBuilder().signedTransaction(rawTx).build();
    }

    private ContractParameter mapInput(final JsonNode input) {
        final String type = input.get(TYPE_KEY).asText();
        if (!isArrayType(type)) {
            return mapSingleInputNode(type, input.get(VALUE_KEY).asText());
        } else {
            return mapArrayNode(input);
        }
    }

    private boolean isArrayType(String type) {
        return type.endsWith("[]");
    }

    private ContractParameter mapSingleInputNode(String type,
                                                 String value) {
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
            case "bytarray":
                return ContractParameter.byteArray(value);
            case "address":
                return ContractParameter.byteArrayFromAddress(value);
            case "public_key":
            case "publickey":
                return ContractParameter.publicKey(value);
            case "string":
                return ContractParameter.string(value);
            case "array":
                return ContractParameter.array(
                        Arrays.stream(parseArray(value))
                              .map(x -> mapSingleInputNode("string", x))
                              .collect(Collectors.toList()));
            default:
                throw new RuntimeException("Cannot map input to a contract input");
        }
    }

    private ContractParameter mapArrayNode(final JsonNode arrayNode) {
        try {
            final String arrayType = arrayNode.get(TYPE_KEY).asText().replace("[]", "");
            final JsonNode valueNode = arrayNode.get(VALUE_KEY);

            if (valueNode != null) {
                final List<ContractParameter> contractParameters;
                switch (valueNode.getNodeType()) {
                    case ARRAY:
                        contractParameters = mapToContractParameters(arrayType, valueNode);
                        break;
                    case STRING:
                        final JsonNode arrayValueNode = objectMapper.readTree(valueNode.asText());
                        contractParameters = mapToContractParameters(arrayType, arrayValueNode);
                        break;
                    default:
                        throw new IllegalArgumentException("JsonNodeType of an array node needs to contain a JSON array or a string representation of a JSON array: "
                                                           + arrayNode.toString());
                }
                return ContractParameter.array(contractParameters);
            } else {
                throw new IllegalArgumentException("No 'value' present on JsonNode: " + arrayNode.toString());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("JsonNodeType of an array node needs to contain a JSON array or a string representation of a JSON array: " + arrayNode.toString());
        }
    }

    private List<ContractParameter> mapToContractParameters(final String type,
                                                            final JsonNode node) {
        return StreamSupport.stream(node.spliterator(), false)
                            .map(JsonNode::asText)
                            .map(value -> mapSingleInputNode(type, value))
                            .collect(Collectors.toList());
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
