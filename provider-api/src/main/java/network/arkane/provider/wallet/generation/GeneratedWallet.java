package network.arkane.provider.wallet.generation;

public interface GeneratedWallet {
    String getAddress();
    String secretAsBase64();
}
