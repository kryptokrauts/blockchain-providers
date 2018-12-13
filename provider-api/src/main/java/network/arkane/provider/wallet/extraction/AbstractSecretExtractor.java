package network.arkane.provider.wallet.extraction;

import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

public interface AbstractSecretExtractor<T extends ExtractionRequest> {

    SecretKey extract(final T importWalletRequest);

    Class<T> getImportRequestType();
}
