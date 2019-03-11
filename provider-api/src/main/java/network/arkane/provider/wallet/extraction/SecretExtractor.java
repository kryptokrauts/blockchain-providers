package network.arkane.provider.wallet.extraction;

import net.jodah.typetools.TypeResolver;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

public interface SecretExtractor<T extends ExtractionRequest> {

    /**
     * Extract a secret, given an extractionrequest
     *
     * @param extractionRequest
     * @return
     */
    SecretKey extract(final T extractionRequest);

    /**
     * The specific type of extraction request this extractor supports
     *
     * @return
     */
    default Class<T> getImportRequestType() {
        return (Class<T>) TypeResolver.resolveRawArguments(SecretExtractor.class, getClass())[0];
    }

}
