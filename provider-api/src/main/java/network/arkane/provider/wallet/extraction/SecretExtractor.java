package network.arkane.provider.wallet.extraction;

import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

public interface SecretExtractor<T extends ExtractionRequest> {

    SecretKey extract(final T extractionRequest);

    Class<T> getImportRequestType();
}
