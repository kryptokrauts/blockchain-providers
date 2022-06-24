package network.arkane.provider.hedera.sign.handler;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.NftId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TransferTransaction;
import network.arkane.provider.hedera.sign.NftTransferSignable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class NftTransferHandlerTest {

    private NftTransferHandler nftTransferHandler;

    @BeforeEach
    void setUp() {
        nftTransferHandler = new NftTransferHandler();
    }

    @Test
    void noSpender() {
        final TransferTransaction transferTransaction = spy(new TransferTransaction());
        final String from = "0.0.456";
        final String to = "0.0.925";
        final String tokenId = "1.4.842";
        final long serial = 37l;
        final BigInteger amount = new BigInteger("467");
        final AccountId fromAccount = AccountId.fromString(from);
        final AccountId toAccount = AccountId.fromString(to);
        final TokenId tokenIdObj = TokenId.fromString(tokenId);
        final NftId nftId = NftId.fromString(tokenId + "/" + serial);

        final NftTransferSignable transferSignable = NftTransferSignable.builder()
                                                                        .from(from)
                                                                        .to(to)
                                                                        .tokenId(tokenId)
                                                                        .serialNumber(serial)
                                                                        .build();

        nftTransferHandler.addTransfer(transferTransaction, transferSignable);

        assertThat(transferTransaction.getTokenNftTransfers()).containsKey(tokenIdObj);
        assertThat(transferTransaction.getTokenNftTransfers().get(tokenIdObj)).extracting("tokenId").containsExactly(tokenIdObj);
        assertThat(transferTransaction.getTokenNftTransfers().get(tokenIdObj)).extracting("sender").containsExactly(fromAccount);
        assertThat(transferTransaction.getTokenNftTransfers().get(tokenIdObj)).extracting("receiver").containsExactly(toAccount);
        assertThat(transferTransaction.getTokenNftTransfers().get(tokenIdObj)).extracting("serial").containsExactly(serial);
        assertThat(transferTransaction.getTokenNftTransfers().get(tokenIdObj)).extracting("isApproved").containsExactly(false);
        verify(transferTransaction).addNftTransfer(nftId, fromAccount, toAccount);

        verify(transferTransaction, never()).addApprovedNftTransfer(any(NftId.class), any(AccountId.class), any(AccountId.class));
    }

    @Test
    void withSpender() {
        final TransferTransaction transferTransaction = spy(new TransferTransaction());
        final String spender = "0.0.520";
        final String from = "0.0.456";
        final String to = "0.0.925";
        final String tokenId = "1.4.842";
        final long serial = 37l;
        final BigInteger amount = new BigInteger("467");
        final AccountId fromAccount = AccountId.fromString(from);
        final AccountId toAccount = AccountId.fromString(to);
        final Hbar hbarAmount = Hbar.fromTinybars(amount.longValueExact());
        final TokenId tokenIdObj = TokenId.fromString(tokenId);
        final NftId nftId = NftId.fromString(tokenId + "/" + serial);

        final NftTransferSignable transferSignable = NftTransferSignable.builder()
                                                                        .spender(spender)
                                                                        .from(from)
                                                                        .to(to)
                                                                        .tokenId(tokenId)
                                                                        .serialNumber(serial)
                                                                        .build();

        nftTransferHandler.addTransfer(transferTransaction, transferSignable);

        assertThat(transferTransaction.getTokenNftTransfers()).containsKey(tokenIdObj);
        assertThat(transferTransaction.getTokenNftTransfers().get(tokenIdObj)).extracting("tokenId").containsExactly(tokenIdObj);
        assertThat(transferTransaction.getTokenNftTransfers().get(tokenIdObj)).extracting("sender").containsExactly(fromAccount);
        assertThat(transferTransaction.getTokenNftTransfers().get(tokenIdObj)).extracting("receiver").containsExactly(toAccount);
        assertThat(transferTransaction.getTokenNftTransfers().get(tokenIdObj)).extracting("serial").containsExactly(serial);
        assertThat(transferTransaction.getTokenNftTransfers().get(tokenIdObj)).extracting("isApproved").containsExactly(true);
        verify(transferTransaction).addApprovedNftTransfer(nftId, fromAccount, toAccount);

        verify(transferTransaction, never()).addNftTransfer(any(NftId.class), any(AccountId.class), any(AccountId.class));
    }
}