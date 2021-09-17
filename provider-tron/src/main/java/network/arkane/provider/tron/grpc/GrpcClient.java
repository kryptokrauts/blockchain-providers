package network.arkane.provider.tron.grpc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.tron.common.utils.Base58;
import org.tron.common.utils.Sha256Hash;

import static org.tron.common.utils.DecodeUtil.addressValid;


@Slf4j
@Component
public class GrpcClient {

    public static String encode58Check(byte[] input) {
        byte[] hash0 = Sha256Hash.hash(true, input);
        byte[] hash1 = Sha256Hash.hash(true, hash0);
        byte[] inputCheck = new byte[input.length + 4];
        System.arraycopy(input, 0, inputCheck, 0, input.length);
        System.arraycopy(hash1, 0, inputCheck, input.length, 4);
        return Base58.encode(inputCheck);
    }

    public static byte[] decodeFromBase58Check(String addressBase58) {
        if (StringUtils.isEmpty(addressBase58)) {
            log.warn("Warning: Address is empty !!");
            return null;
        }
        byte[] address = decode58Check(addressBase58);
        if (!addressValid(address)) {
            return null;
        }
        return address;
    }

    private static byte[] decode58Check(String input) {
        byte[] decodeCheck = Base58.decode(input);
        if (decodeCheck.length <= 4) {
            return null;
        }
        byte[] decodeData = new byte[decodeCheck.length - 4];
        System.arraycopy(decodeCheck, 0, decodeData, 0, decodeData.length);
        byte[] hash0 = Sha256Hash.hash(true, decodeData);
        byte[] hash1 = Sha256Hash.hash(true, hash0);
        if (hash1[0] == decodeCheck[decodeData.length] &&
            hash1[1] == decodeCheck[decodeData.length + 1] &&
            hash1[2] == decodeCheck[decodeData.length + 2] &&
            hash1[3] == decodeCheck[decodeData.length + 3]) {
            return decodeData;
        }
        return null;
    }
}
