package network.arkane.provider.sign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

@Slf4j
@Component
public class EthereumRawVerifier implements Verifier<EthereumRawVerifiable> {


    public static final String MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    @Override
    public boolean isValidSignature(EthereumRawVerifiable verifiable) {
        try {
            return verifyMessage(verifiable);
        } catch (Exception e) {
            return false;
        }

    }

    private boolean verifyMessage(EthereumRawVerifiable verifiable) {
        String address = verifiable.getAddress();
        String message = verifiable.getMessage();
        if (!address.startsWith("0x")) {
            address = "0x" + address;
        }

        if (verifiable.isPrefix()) {
            String prefix = MESSAGE_PREFIX + message.length();
            message = prefix + message;
        }
        byte[] msgHash = Hash.sha3((message).getBytes());

        byte[] signatureBytes = Numeric.hexStringToByteArray(verifiable.getSignature());
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        Sign.SignatureData sd = new Sign.SignatureData(
                v,
                (byte[]) Arrays.copyOfRange(signatureBytes, 0, 32),
                (byte[]) Arrays.copyOfRange(signatureBytes, 32, 64));

        String addressRecovered = null;
        boolean match = false;

        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            BigInteger publicKey = Sign.recoverFromSignature(
                    (byte) i,
                    new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
                    msgHash);

            if (publicKey != null) {
                addressRecovered = "0x" + Keys.getAddress(publicKey);

                if (addressRecovered.equalsIgnoreCase(address)) {
                    match = true;
                    break;
                }
            }
        }
        return match;
    }

}
