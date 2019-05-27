package network.arkane.provider.clients.base;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import network.arkane.provider.Prefix;
import network.arkane.provider.core.model.blockchain.ContractCall;
import network.arkane.provider.core.model.blockchain.ContractCallResult;
import network.arkane.provider.core.model.blockchain.NodeProvider;
import network.arkane.provider.core.model.clients.Address;
import network.arkane.provider.core.model.clients.Revision;
import network.arkane.provider.core.model.exception.ClientArgumentException;
import network.arkane.provider.core.model.exception.ClientIOException;
import network.arkane.provider.utils.URLUtils;

import java.io.IOException;
import java.util.HashMap;

public abstract class AbstractClient {

    public enum Path {

        // Accounts
        GetAccountPath("/accounts/{address}"), PostContractCallPath("/accounts/{address}"), PostDeployContractPath(
                "/accounts"), GetAccountCodePath(
                "/accounts/{address}/code"), GetStorageValuePath("/accounts/{address}/storage/{key}"),

        // Transactions
        GetTransactionPath("/transactions/{id}"), GetTransactionReceipt("/transactions/{id}/receipt"), PostTransaction(
                "/transactions"),

        // Blocks
        GetBlockPath("/blocks/{revision}"),

        // Events
        PostFilterEventsLogPath("/events"),

        // Transfers
        PostFilterTransferLogPath("/transfers"),

        // Nodes
        GetNodeInfoPath("/node/network/peers"),
        ;
        private final String value;

        Path(String value) {
            this.value = value;
        }

        public String getPath() {
            return value;
        }

    }

    private static ObjectMapper objectMapper;

    static {
        setTimeout(5000);
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private static String rawUrl(Path path) {
        return NodeProvider.getNodeProvider().getProvider() + path.getPath();
    }

    public static void setTimeout(int timeout) {
        try {
            Unirest.shutdown();
        } catch (IOException e) {

        }
        Unirest.setTimeouts(timeout, timeout);
    }

    /**
     * Get the request
     *
     * @param path        {@link Path}
     * @param uriParams   uri parameters
     * @param queryParams query string parameters
     * @param tClass      the class of result java object.
     * @param <T>         Type of result java object.
     * @return response java object, could be null, mean can not find any result.
     * @throws IOException node is not reachable or request is not valid.
     */
    public static <T> T sendGetRequest(Path path, HashMap<String, String> uriParams,
                                       HashMap<String, String> queryParams, Class<T> tClass) throws IOException {
        String rawURL = rawUrl(path);
        String getURL = URLUtils.urlComposite(rawURL, uriParams, queryParams);
        HttpResponse<String> jsonNode = null;
        try {
            jsonNode = Unirest.get(getURL).asString();
        } catch (UnirestException e) {
            throw new ClientIOException(e);
        }
        return parseResult(tClass, jsonNode);
    }

    private static <T> T parseResult(Class<T> tClass, HttpResponse<String> jsonNode) throws IOException {
        int status = jsonNode.getStatus();
        if (status != 200) {
            String exception_msg = "response exception";
            if (status == 400) {
                exception_msg = "bad request";
            } else if (status == 403) {
                exception_msg = "request forbidden";
            }
            ClientIOException clientIOException = new ClientIOException(
                    exception_msg + " " + jsonNode.getBody());
            clientIOException.setHttpStatus(status);
            throw clientIOException;
        } else {
            return objectMapper.readValue(jsonNode.getBody(), tClass);
        }
    }

    /**
     * Post the request
     *
     * @param path        {@link Path}
     * @param uriParams   uri parameters
     * @param queryParams query string parameters
     * @param tClass      the class of result java object.
     * @param <T>         Type of result java object.
     * @return response java object, could be null, mean can not find any result.
     * @throws ClientIOException http status 4xx means not enough energy amount.
     */
    public static <T> T sendPostRequest(Path path, HashMap<String, String> uriParams,
                                        HashMap<String, String> queryParams, Object postBody, Class<T> tClass) throws IOException {
        String rawURL = rawUrl(path);
        String postURL = URLUtils.urlComposite(rawURL, uriParams, queryParams);

        HttpResponse<String> jsonNode = null;
        String postString = objectMapper.writeValueAsString(postBody);
        try {
            jsonNode = Unirest.post(postURL).body(postString).asString();
        } catch (UnirestException e) {
            throw new ClientIOException(e);
        }
        return parseResult(tClass, jsonNode);
    }

    protected static HashMap<String, String> parameters(String[] keys, String[] values) {
        if (keys == null || values == null || keys.length != values.length) {
            throw ClientArgumentException.exception("Parameters creating failed");
        }

        HashMap<String, String> params = new HashMap<>();
        for (int index = 0; index < keys.length; index++) {
            params.put(keys[index], values[index]);
        }
        return params;
    }

    /**
     * Call the contract view function.
     *
     * @param call            {@link ContractCall}
     * @param contractAddress {@link Address}
     * @param revision        {@link Revision}
     * @return {@link ContractCallResult}
     */
    public static ContractCallResult callContract(ContractCall call, Address contractAddress, Revision revision)
            throws IOException {
        Revision currentRevision = revision;
        if (currentRevision == null) {
            currentRevision = Revision.BEST;
        }

        HashMap<String, String> uriParams = parameters(new String[] {"address"},
                                                       new String[] {contractAddress.toHexString(Prefix.ZeroLowerX)});
        HashMap<String, String> queryParams = parameters(new String[] {"revision"},
                                                         new String[] {currentRevision.toString()});

        return sendPostRequest(Path.PostContractCallPath, uriParams, queryParams, call, ContractCallResult.class);
    }

}
