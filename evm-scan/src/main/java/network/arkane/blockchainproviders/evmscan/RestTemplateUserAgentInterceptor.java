package network.arkane.blockchainproviders.evmscan;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;

import java.io.IOException;

public class RestTemplateUserAgentInterceptor implements ClientHttpRequestInterceptor {

    @Override
    @NonNull
    public ClientHttpResponse intercept(HttpRequest request,
                                        @NonNull byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().set("User-Agent", "curl/7.54.0");
        return execution.execute(request, body);
    }
}