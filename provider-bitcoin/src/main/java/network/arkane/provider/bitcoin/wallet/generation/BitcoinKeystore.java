package network.arkane.provider.bitcoin.wallet.generation;


public class BitcoinKeystore {
    private String pubKey;
    private String initialisationVector;
    private String encryptedBytes;
    private String salt;

    public BitcoinKeystore() {
    }

    public BitcoinKeystore(String pubKey,
                           String initialisationVector,
                           String encryptedBytes,
                           String salt) {
        this.pubKey = pubKey;
        this.initialisationVector = initialisationVector;
        this.encryptedBytes = encryptedBytes;
        this.salt = salt;
    }

    public String getPubKey() {
        return pubKey;
    }

    public String getInitialisationVector() {
        return initialisationVector;
    }

    public String getEncryptedBytes() {
        return encryptedBytes;
    }

    public String getSalt() {
        return salt;
    }
}
