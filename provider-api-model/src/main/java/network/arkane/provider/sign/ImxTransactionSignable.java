package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class ImxTransactionSignable implements Signable {

    private String amount;

    private String sender;

    private String receiver;

    private String token;

    private String data;

    @Builder
    public ImxTransactionSignable(final String amount,
                                  final String sender,
                                  final String token,
                                  final String receiver,
                                  final String data) {
        this.amount = amount;
        this.sender = sender;
        this.token = token;
        this.receiver = receiver;
        this.data = data;
    }
}
