package network.arkane.provider.core.model.blockchain;

import network.arkane.provider.core.model.clients.Amount;
import network.arkane.provider.core.model.clients.ERC20Token;
import network.arkane.provider.core.model.clients.base.AbstractToken;

import java.io.Serializable;

public class Account implements Serializable {

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    public boolean getHasCode() {
        return hasCode;
    }

    public void setHasCode(boolean hasCode) {
        this.hasCode = hasCode;
    }

    public Amount VETBalance(){
        Amount balance = Amount.createFromToken(AbstractToken.VET);
        balance.setHexAmount( this.balance );
        return balance;
    }

    /**
     * On VeChain mainnet, it has two native currencies, one is VET, the other is VTHO
     * @return
     */
    public Amount energyBalance(){
        Amount balance = Amount.createFromToken(ERC20Token.VTHO);
        balance.setHexAmount( this.energy );
        return balance;
    }

    private String balance;

    private String energy;

    private boolean hasCode;

}
