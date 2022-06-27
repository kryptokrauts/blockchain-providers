package network.arkane.provider.hedera.sign.handler;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.TransferTransaction;
import network.arkane.provider.hedera.sign.HbarTransferSignable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class HbarTransferHandlerTest {

    private HbarTransferHandler hbarTransferHandler;

    @BeforeEach
    void setUp() {
        hbarTransferHandler = new HbarTransferHandler();
    }

    @Test
    void noSpender() {
        final TransferTransaction transferTransaction = spy(new TransferTransaction());
        final String from = "0.0.456";
        final String to = "0.0.925";
        final BigInteger amount = new BigInteger("467");
        final AccountId fromAccount = AccountId.fromString(from);
        final AccountId toAccount = AccountId.fromString(to);
        final Hbar hbarAmount = Hbar.fromTinybars(amount.longValueExact());
        final HbarTransferSignable transferSignable = HbarTransferSignable.builder()
                                                                          .from(from)
                                                                          .to(to)
                                                                          .amount(amount)
                                                                          .build();

        hbarTransferHandler.addTransfer(transferTransaction, transferSignable);

        assertThat(transferTransaction.getHbarTransfers()).containsKey(fromAccount);
        assertThat(transferTransaction.getHbarTransfers().get(fromAccount)).isEqualTo(hbarAmount.negated());
        verify(transferTransaction).addHbarTransfer(fromAccount, hbarAmount.negated());

        assertThat(transferTransaction.getHbarTransfers()).containsKey(toAccount);
        assertThat(transferTransaction.getHbarTransfers().get(toAccount)).isEqualTo(hbarAmount);
        verify(transferTransaction).addHbarTransfer(toAccount, hbarAmount);

        verify(transferTransaction, never()).addApprovedHbarTransfer(any(AccountId.class), any(Hbar.class));
    }

    @Test
    void withSpender() {
        final TransferTransaction transferTransaction = spy(new TransferTransaction());
        final String spender = "0.0.269";
        final String from = "0.0.943";
        final String to = "0.0.357";
        final BigInteger amount = new BigInteger("36");
        final AccountId fromAccount = AccountId.fromString(from);
        final AccountId toAccount = AccountId.fromString(to);
        final Hbar hbarAmount = Hbar.fromTinybars(amount.longValueExact());
        final HbarTransferSignable transferSignable = HbarTransferSignable.builder()
                                                                          .spender(spender)
                                                                          .from(from)
                                                                          .to(to)
                                                                          .amount(amount)
                                                                          .build();

        hbarTransferHandler.addTransfer(transferTransaction, transferSignable);

        assertThat(transferTransaction.getHbarTransfers()).containsKey(fromAccount);
        assertThat(transferTransaction.getHbarTransfers().get(fromAccount)).isEqualTo(hbarAmount.negated());
        verify(transferTransaction).addApprovedHbarTransfer(fromAccount, hbarAmount.negated());

        assertThat(transferTransaction.getHbarTransfers()).containsKey(toAccount);
        assertThat(transferTransaction.getHbarTransfers().get(toAccount)).isEqualTo(hbarAmount);
        verify(transferTransaction).addApprovedHbarTransfer(toAccount, hbarAmount);

        verify(transferTransaction, never()).addHbarTransfer(any(AccountId.class), any(Hbar.class));
    }
}