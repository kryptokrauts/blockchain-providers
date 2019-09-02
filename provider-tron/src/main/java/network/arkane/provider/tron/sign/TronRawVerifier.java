package network.arkane.provider.tron.sign;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.sign.Verifier;
import org.springframework.stereotype.Component;
import org.tron.common.crypto.ECKey;
import org.tron.core.Wallet;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Arrays;

@Slf4j
@Component
public class TronRawVerifier implements Verifier<TronRawVerifiable> {


    private static final String MESSAGE_PREFIX = "\u0019TRON Signed Message:\n32";

    @Override
    public boolean isValidSignature(TronRawVerifiable verifiable) {
        try {
            return verifyMessage(verifiable);
        } catch (Exception e) {
            return false;
        }

    }

    private boolean verifyMessage(TronRawVerifiable verifiable) {
        String address = verifiable.getAddress();
        String message = verifiable.getMessage();

        byte[] msgHash = getTronMessageHash(message.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = Numeric.hexStringToByteArray(verifiable.getSignature());
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        try {
            byte[] r = Arrays.copyOfRange(signatureBytes, 0, 32);
            byte[] s = Arrays.copyOfRange(signatureBytes, 32, 64);
            ECKey.ECDSASignature ecdsaSignature = ECKey.ECDSASignature.fromComponents((byte[]) r, (byte[]) s, v);
            String resultAddress = Wallet.encode58Check(ECKey.signatureToAddress(msgHash, ecdsaSignature));
            return resultAddress.equalsIgnoreCase(address);
        } catch (SignatureException e) {
            return false;
        }
    }

    private byte[] getTronMessageHash(byte[] message) {
        byte[] prefix = getTronMessagePrefix();
        byte[] result = new byte[prefix.length + message.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(message, 0, result, prefix.length, message.length);

        return Hash.sha3(result);
    }

    private byte[] getTronMessagePrefix() {
        return MESSAGE_PREFIX.getBytes();
    }
}
