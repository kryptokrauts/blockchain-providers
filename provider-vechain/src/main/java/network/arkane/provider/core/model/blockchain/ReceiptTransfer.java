package network.arkane.provider.core.model.blockchain;

import java.io.Serializable;

public class ReceiptTransfer implements Serializable {
    private String sender;
    private String recipient;
    private String amount;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
