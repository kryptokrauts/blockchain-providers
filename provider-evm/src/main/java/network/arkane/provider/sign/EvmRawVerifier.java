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
public class EvmRawVerifier implements Verifier<EvmRawVerifiable> {


    public static final String MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    @Override
    public boolean isValidSignature(final EvmRawVerifiable verifiable) {
        try {
            final String address = enrichAddress(verifiable);
            final String message = enrichMessage(verifiable);
            return verifyMessage(address, message.getBytes(), verifiable.getSignature());
        } catch (Exception e) {
            return false;
        }
    }

    private String enrichAddress(final EvmRawVerifiable verifiable) {
        final String address = verifiable.getAddress();
        if (!address.startsWith("0x")) {
            return "0x" + address;
        }
        return address;
    }

    private String enrichMessage(final EvmRawVerifiable verifiable) {
        final String message = verifiable.getMessage();
        if (verifiable.isPrefix()) {
            String prefix = MESSAGE_PREFIX + message.length();
            return prefix + message;
        }
        return message;
    }

    public boolean verifyMessage(final String address, final byte[] message, final String signature) {
        byte[] msgHash = Hash.sha3(message);

        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        Sign.SignatureData sd = new Sign.SignatureData(v,
                                                       Arrays.copyOfRange(signatureBytes, 0, 32),
                                                       Arrays.copyOfRange(signatureBytes, 32, 64));

        String addressRecovered;
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
