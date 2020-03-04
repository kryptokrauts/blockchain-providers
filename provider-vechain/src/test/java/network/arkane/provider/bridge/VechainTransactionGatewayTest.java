package network.arkane.provider.bridge;

import network.arkane.provider.core.model.blockchain.TransferResult;
import network.arkane.provider.gateway.VechainGateway;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VechainTransactionGatewayTest {

    private VechainTransactionGateway vechainTransactionGateway;
    private VechainGateway vechainGateway;

    @BeforeEach
    void setUp() {
        vechainGateway = mock(VechainGateway.class);

        vechainTransactionGateway = new VechainTransactionGateway(vechainGateway);
    }

    @Test
    void submit() {
        TransactionSignature transactionSignature = TransactionSignature.signTransactionBuilder().signedTransaction("signed").build();
        TransferResult transferResult = new TransferResult();
        transferResult.setId("transferId");
        when(vechainGateway.sendRawTransaction(transactionSignature.getSignedTransaction())).thenReturn(transferResult);

        Signature result = vechainTransactionGateway.submit(transactionSignature, Optional.empty());

        assertThat(result).isExactlyInstanceOf(SubmittedAndSignedTransactionSignature.class);
        assertThat(((SubmittedAndSignedTransactionSignature) result).getTransactionHash()).isEqualTo("transferId");
    }

    @Test
    void submitWithException() {
        final TransactionSignature transactionSignature = TransactionSignature.signTransactionBuilder().signedTransaction("signed").build();
        final TransferResult transferResult = new TransferResult();
        transferResult.setId("transferId");
        when(vechainGateway.sendRawTransaction(transactionSignature.getSignedTransaction())).thenThrow(new RuntimeException("error signing"));

        assertThatThrownBy(() -> vechainTransactionGateway.submit(transactionSignature, Optional.empty()))
                .isEqualToComparingFieldByField(arkaneException()
                                                        .errorCode("transaction.submit.internal-error")
                                                        .message("problem trying to submit transaction to vechain: error signing")
                                                        .build());
    }
}
