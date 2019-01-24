package network.arkane.provider.wallet.exporting;

import network.arkane.provider.wallet.exporting.request.KeyExportRequest;

public interface KeyExporter<T extends KeyExportRequest> {
    String export(T extractionRequest);
}
