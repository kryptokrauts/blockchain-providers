package network.arkane.provider.hedera.sign.handler;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TransferTransaction;
import network.arkane.provider.hedera.sign.TokenTransferSignable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class TokenTransferHandlerTest {

    private TokenTransferHandler tokenTransferHandler;

    @BeforeEach
    void setUp() {
        tokenTransferHandler = new TokenTransferHandler();
    }

    @Test
    void noSpender() {
        final TransferTransaction transferTransaction = spy(new TransferTransaction());
        final String from = "0.0.456";
        final String to = "0.0.925";
        final String tokenId = "2.2.537";
        final BigInteger amount = new BigInteger("467");
        final AccountId fromAccount = AccountId.fromString(from);
        final AccountId toAccount = AccountId.fromString(to);
        final TokenId tokenIdObj = TokenId.fromString(tokenId);
        final TokenTransferSignable transferSignable = TokenTransferSignable.builder()
                                                                            .from(from)
                                                                            .to(to)
                                                                            .tokenId(tokenId)
                                                                            .amount(amount)
                                                                            .build();

        tokenTransferHandler.addTransfer(transferTransaction, transferSignable);

        assertThat(transferTransaction.getTokenTransfers()).containsKey(tokenIdObj);
        assertThat(transferTransaction.getTokenTransfers().get(tokenIdObj)).containsKey(fromAccount);
        assertThat(transferTransaction.getTokenTransfers().get(tokenIdObj).get(fromAccount)).isEqualTo(amount.negate().longValueExact());
        verify(transferTransaction).addTokenTransfer(tokenIdObj, fromAccount, amount.negate().longValueExact());

        assertThat(transferTransaction.getTokenTransfers()).containsKey(tokenIdObj);
        assertThat(transferTransaction.getTokenTransfers().get(tokenIdObj)).containsKey(toAccount);
        assertThat(transferTransaction.getTokenTransfers().get(tokenIdObj).get(toAccount)).isEqualTo(amount.longValueExact());
        verify(transferTransaction).addTokenTransfer(tokenIdObj, toAccount, amount.longValueExact());

        verify(transferTransaction, never()).addApprovedTokenTransfer(any(TokenId.class), any(AccountId.class), anyLong());
    }

    @Test
    void withSpender() {
        final TransferTransaction transferTransaction = spy(new TransferTransaction());
        final String spender = "0.0.269";
        final String from = "0.0.456";
        final String to = "0.0.925";
        final String tokenId = "2.2.537";
        final BigInteger amount = new BigInteger("467");
        final AccountId fromAccount = AccountId.fromString(from);
        final AccountId toAccount = AccountId.fromString(to);
        final TokenId tokenIdObj = TokenId.fromString(tokenId);
        final TokenTransferSignable transferSignable = TokenTransferSignable.builder()
                                                                            .spender(spender)
                                                                            .from(from)
                                                                            .to(to)
                                                                            .tokenId(tokenId)
                                                                            .amount(amount)
                                                                            .build();

        tokenTransferHandler.addTransfer(transferTransaction, transferSignable);

        assertThat(transferTransaction.getTokenTransfers()).containsKey(tokenIdObj);
        assertThat(transferTransaction.getTokenTransfers().get(tokenIdObj)).containsKey(fromAccount);
        assertThat(transferTransaction.getTokenTransfers().get(tokenIdObj).get(fromAccount)).isEqualTo(amount.negate().longValueExact());
        verify(transferTransaction).addApprovedTokenTransfer(tokenIdObj, fromAccount, amount.negate().longValueExact());

        assertThat(transferTransaction.getTokenTransfers()).containsKey(tokenIdObj);
        assertThat(transferTransaction.getTokenTransfers().get(tokenIdObj)).containsKey(toAccount);
        assertThat(transferTransaction.getTokenTransfers().get(tokenIdObj).get(toAccount)).isEqualTo(amount.longValueExact());
        verify(transferTransaction).addApprovedTokenTransfer(tokenIdObj, toAccount, amount.longValueExact());

        verify(transferTransaction, never()).addTokenTransfer(any(TokenId.class), any(AccountId.class), anyLong());
    }
}