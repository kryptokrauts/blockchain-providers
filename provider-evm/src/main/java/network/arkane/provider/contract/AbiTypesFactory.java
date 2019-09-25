//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package network.arkane.provider.contract;

import network.arkane.provider.BytesUtils;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes1;
import org.web3j.abi.datatypes.generated.Bytes10;
import org.web3j.abi.datatypes.generated.Bytes11;
import org.web3j.abi.datatypes.generated.Bytes12;
import org.web3j.abi.datatypes.generated.Bytes13;
import org.web3j.abi.datatypes.generated.Bytes14;
import org.web3j.abi.datatypes.generated.Bytes15;
import org.web3j.abi.datatypes.generated.Bytes16;
import org.web3j.abi.datatypes.generated.Bytes17;
import org.web3j.abi.datatypes.generated.Bytes18;
import org.web3j.abi.datatypes.generated.Bytes19;
import org.web3j.abi.datatypes.generated.Bytes2;
import org.web3j.abi.datatypes.generated.Bytes20;
import org.web3j.abi.datatypes.generated.Bytes21;
import org.web3j.abi.datatypes.generated.Bytes22;
import org.web3j.abi.datatypes.generated.Bytes23;
import org.web3j.abi.datatypes.generated.Bytes24;
import org.web3j.abi.datatypes.generated.Bytes25;
import org.web3j.abi.datatypes.generated.Bytes26;
import org.web3j.abi.datatypes.generated.Bytes27;
import org.web3j.abi.datatypes.generated.Bytes28;
import org.web3j.abi.datatypes.generated.Bytes29;
import org.web3j.abi.datatypes.generated.Bytes3;
import org.web3j.abi.datatypes.generated.Bytes30;
import org.web3j.abi.datatypes.generated.Bytes31;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.abi.datatypes.generated.Bytes5;
import org.web3j.abi.datatypes.generated.Bytes6;
import org.web3j.abi.datatypes.generated.Bytes7;
import org.web3j.abi.datatypes.generated.Bytes8;
import org.web3j.abi.datatypes.generated.Bytes9;
import org.web3j.abi.datatypes.generated.Int104;
import org.web3j.abi.datatypes.generated.Int112;
import org.web3j.abi.datatypes.generated.Int120;
import org.web3j.abi.datatypes.generated.Int128;
import org.web3j.abi.datatypes.generated.Int136;
import org.web3j.abi.datatypes.generated.Int144;
import org.web3j.abi.datatypes.generated.Int152;
import org.web3j.abi.datatypes.generated.Int16;
import org.web3j.abi.datatypes.generated.Int160;
import org.web3j.abi.datatypes.generated.Int168;
import org.web3j.abi.datatypes.generated.Int176;
import org.web3j.abi.datatypes.generated.Int184;
import org.web3j.abi.datatypes.generated.Int192;
import org.web3j.abi.datatypes.generated.Int200;
import org.web3j.abi.datatypes.generated.Int208;
import org.web3j.abi.datatypes.generated.Int216;
import org.web3j.abi.datatypes.generated.Int224;
import org.web3j.abi.datatypes.generated.Int232;
import org.web3j.abi.datatypes.generated.Int24;
import org.web3j.abi.datatypes.generated.Int240;
import org.web3j.abi.datatypes.generated.Int248;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Int32;
import org.web3j.abi.datatypes.generated.Int40;
import org.web3j.abi.datatypes.generated.Int48;
import org.web3j.abi.datatypes.generated.Int56;
import org.web3j.abi.datatypes.generated.Int64;
import org.web3j.abi.datatypes.generated.Int72;
import org.web3j.abi.datatypes.generated.Int8;
import org.web3j.abi.datatypes.generated.Int80;
import org.web3j.abi.datatypes.generated.Int88;
import org.web3j.abi.datatypes.generated.Int96;
import org.web3j.abi.datatypes.generated.Uint104;
import org.web3j.abi.datatypes.generated.Uint112;
import org.web3j.abi.datatypes.generated.Uint120;
import org.web3j.abi.datatypes.generated.Uint128;
import org.web3j.abi.datatypes.generated.Uint136;
import org.web3j.abi.datatypes.generated.Uint144;
import org.web3j.abi.datatypes.generated.Uint152;
import org.web3j.abi.datatypes.generated.Uint16;
import org.web3j.abi.datatypes.generated.Uint160;
import org.web3j.abi.datatypes.generated.Uint168;
import org.web3j.abi.datatypes.generated.Uint176;
import org.web3j.abi.datatypes.generated.Uint184;
import org.web3j.abi.datatypes.generated.Uint192;
import org.web3j.abi.datatypes.generated.Uint200;
import org.web3j.abi.datatypes.generated.Uint208;
import org.web3j.abi.datatypes.generated.Uint216;
import org.web3j.abi.datatypes.generated.Uint224;
import org.web3j.abi.datatypes.generated.Uint232;
import org.web3j.abi.datatypes.generated.Uint24;
import org.web3j.abi.datatypes.generated.Uint240;
import org.web3j.abi.datatypes.generated.Uint248;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.abi.datatypes.generated.Uint40;
import org.web3j.abi.datatypes.generated.Uint48;
import org.web3j.abi.datatypes.generated.Uint56;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.abi.datatypes.generated.Uint72;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.abi.datatypes.generated.Uint80;
import org.web3j.abi.datatypes.generated.Uint88;
import org.web3j.abi.datatypes.generated.Uint96;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class AbiTypesFactory {
    private AbiTypesFactory() {
    }

    public static Type getType(String type, String value) {
        if (isArray(type, value)) {
            List<Type> types = Arrays.stream(value.substring(0, value.length() - 1)
                                                  .substring(1)
                                                  .replaceAll(" ", "")
                                                  .split(","))
                                     .map(x -> getType(type.substring(0, type.length() - 2), x))
                                     .collect(Collectors.toList());
            return new DynamicArray(types);
        }
        byte var2 = -1;
        switch (type.hashCode()) {
            case -1374008026:
                if (type.equals("bytes1")) {
                    var2 = 68;
                }
                break;
            case -1374008025:
                if (type.equals("bytes2")) {
                    var2 = 69;
                }
                break;
            case -1374008024:
                if (type.equals("bytes3")) {
                    var2 = 70;
                }
                break;
            case -1374008023:
                if (type.equals("bytes4")) {
                    var2 = 71;
                }
                break;
            case -1374008022:
                if (type.equals("bytes5")) {
                    var2 = 72;
                }
                break;
            case -1374008021:
                if (type.equals("bytes6")) {
                    var2 = 73;
                }
                break;
            case -1374008020:
                if (type.equals("bytes7")) {
                    var2 = 74;
                }
                break;
            case -1374008019:
                if (type.equals("bytes8")) {
                    var2 = 75;
                }
                break;
            case -1374008018:
                if (type.equals("bytes9")) {
                    var2 = 76;
                }
                break;
            case -1183814746:
                if (type.equals("int104")) {
                    var2 = 29;
                }
                break;
            case -1183814717:
                if (type.equals("int112")) {
                    var2 = 31;
                }
                break;
            case -1183814688:
                if (type.equals("int120")) {
                    var2 = 33;
                }
                break;
            case -1183814680:
                if (type.equals("int128")) {
                    var2 = 35;
                }
                break;
            case -1183814651:
                if (type.equals("int136")) {
                    var2 = 37;
                }
                break;
            case -1183814622:
                if (type.equals("int144")) {
                    var2 = 39;
                }
                break;
            case -1183814593:
                if (type.equals("int152")) {
                    var2 = 41;
                }
                break;
            case -1183814564:
                if (type.equals("int160")) {
                    var2 = 43;
                }
                break;
            case -1183814556:
                if (type.equals("int168")) {
                    var2 = 45;
                }
                break;
            case -1183814527:
                if (type.equals("int176")) {
                    var2 = 47;
                }
                break;
            case -1183814498:
                if (type.equals("int184")) {
                    var2 = 49;
                }
                break;
            case -1183814469:
                if (type.equals("int192")) {
                    var2 = 51;
                }
                break;
            case -1183813789:
                if (type.equals("int200")) {
                    var2 = 53;
                }
                break;
            case -1183813781:
                if (type.equals("int208")) {
                    var2 = 55;
                }
                break;
            case -1183813752:
                if (type.equals("int216")) {
                    var2 = 57;
                }
                break;
            case -1183813723:
                if (type.equals("int224")) {
                    var2 = 59;
                }
                break;
            case -1183813694:
                if (type.equals("int232")) {
                    var2 = 61;
                }
                break;
            case -1183813665:
                if (type.equals("int240")) {
                    var2 = 63;
                }
                break;
            case -1183813657:
                if (type.equals("int248")) {
                    var2 = 65;
                }
                break;
            case -1183813628:
                if (type.equals("int256")) {
                    var2 = 67;
                }
                break;
            case -1147692044:
                if (type.equals("address")) {
                    var2 = 0;
                }
                break;
            case -891985903:
                if (type.equals("string")) {
                    var2 = 2;
                }
                break;
            case -844996865:
                if (type.equals("uint16")) {
                    var2 = 6;
                }
                break;
            case -844996836:
                if (type.equals("uint24")) {
                    var2 = 8;
                }
                break;
            case -844996807:
                if (type.equals("uint32")) {
                    var2 = 10;
                }
                break;
            case -844996778:
                if (type.equals("uint40")) {
                    var2 = 12;
                }
                break;
            case -844996770:
                if (type.equals("uint48")) {
                    var2 = 14;
                }
                break;
            case -844996741:
                if (type.equals("uint56")) {
                    var2 = 16;
                }
                break;
            case -844996712:
                if (type.equals("uint64")) {
                    var2 = 18;
                }
                break;
            case -844996683:
                if (type.equals("uint72")) {
                    var2 = 20;
                }
                break;
            case -844996654:
                if (type.equals("uint80")) {
                    var2 = 22;
                }
                break;
            case -844996646:
                if (type.equals("uint88")) {
                    var2 = 24;
                }
                break;
            case -844996617:
                if (type.equals("uint96")) {
                    var2 = 26;
                }
                break;
            case -425099173:
                if (type.equals("uint104")) {
                    var2 = 28;
                }
                break;
            case -425099144:
                if (type.equals("uint112")) {
                    var2 = 30;
                }
                break;
            case -425099115:
                if (type.equals("uint120")) {
                    var2 = 32;
                }
                break;
            case -425099107:
                if (type.equals("uint128")) {
                    var2 = 34;
                }
                break;
            case -425099078:
                if (type.equals("uint136")) {
                    var2 = 36;
                }
                break;
            case -425099049:
                if (type.equals("uint144")) {
                    var2 = 38;
                }
                break;
            case -425099020:
                if (type.equals("uint152")) {
                    var2 = 40;
                }
                break;
            case -425098991:
                if (type.equals("uint160")) {
                    var2 = 42;
                }
                break;
            case -425098983:
                if (type.equals("uint168")) {
                    var2 = 44;
                }
                break;
            case -425098954:
                if (type.equals("uint176")) {
                    var2 = 46;
                }
                break;
            case -425098925:
                if (type.equals("uint184")) {
                    var2 = 48;
                }
                break;
            case -425098896:
                if (type.equals("uint192")) {
                    var2 = 50;
                }
                break;
            case -425098216:
                if (type.equals("uint200")) {
                    var2 = 52;
                }
                break;
            case -425098208:
                if (type.equals("uint208")) {
                    var2 = 54;
                }
                break;
            case -425098179:
                if (type.equals("uint216")) {
                    var2 = 56;
                }
                break;
            case -425098150:
                if (type.equals("uint224")) {
                    var2 = 58;
                }
                break;
            case -425098121:
                if (type.equals("uint232")) {
                    var2 = 60;
                }
                break;
            case -425098092:
                if (type.equals("uint240")) {
                    var2 = 62;
                }
                break;
            case -425098084:
                if (type.equals("uint248")) {
                    var2 = 64;
                }
                break;
            case -425098055:
                if (type.equals("uint256")) {
                    var2 = 66;
                }
                break;
            case 3029738:
                if (type.equals("bool")) {
                    var2 = 1;
                }
                break;
            case 3237417:
                if (type.equals("int8")) {
                    var2 = 5;
                }
                break;
            case 94224491:
                if (type.equals("bytes")) {
                    var2 = 3;
                }
                break;
            case 100359764:
                if (type.equals("int16")) {
                    var2 = 7;
                }
                break;
            case 100359793:
                if (type.equals("int24")) {
                    var2 = 9;
                }
                break;
            case 100359822:
                if (type.equals("int32")) {
                    var2 = 11;
                }
                break;
            case 100359851:
                if (type.equals("int40")) {
                    var2 = 13;
                }
                break;
            case 100359859:
                if (type.equals("int48")) {
                    var2 = 15;
                }
                break;
            case 100359888:
                if (type.equals("int56")) {
                    var2 = 17;
                }
                break;
            case 100359917:
                if (type.equals("int64")) {
                    var2 = 19;
                }
                break;
            case 100359946:
                if (type.equals("int72")) {
                    var2 = 21;
                }
                break;
            case 100359975:
                if (type.equals("int80")) {
                    var2 = 23;
                }
                break;
            case 100359983:
                if (type.equals("int88")) {
                    var2 = 25;
                }
                break;
            case 100360012:
                if (type.equals("int96")) {
                    var2 = 27;
                }
                break;
            case 111289374:
                if (type.equals("uint8")) {
                    var2 = 4;
                }
                break;
            case 355424202:
                if (type.equals("bytes10")) {
                    var2 = 77;
                }
                break;
            case 355424203:
                if (type.equals("bytes11")) {
                    var2 = 78;
                }
                break;
            case 355424204:
                if (type.equals("bytes12")) {
                    var2 = 79;
                }
                break;
            case 355424205:
                if (type.equals("bytes13")) {
                    var2 = 80;
                }
                break;
            case 355424206:
                if (type.equals("bytes14")) {
                    var2 = 81;
                }
                break;
            case 355424207:
                if (type.equals("bytes15")) {
                    var2 = 82;
                }
                break;
            case 355424208:
                if (type.equals("bytes16")) {
                    var2 = 83;
                }
                break;
            case 355424209:
                if (type.equals("bytes17")) {
                    var2 = 84;
                }
                break;
            case 355424210:
                if (type.equals("bytes18")) {
                    var2 = 85;
                }
                break;
            case 355424211:
                if (type.equals("bytes19")) {
                    var2 = 86;
                }
                break;
            case 355424233:
                if (type.equals("bytes20")) {
                    var2 = 87;
                }
                break;
            case 355424234:
                if (type.equals("bytes21")) {
                    var2 = 88;
                }
                break;
            case 355424235:
                if (type.equals("bytes22")) {
                    var2 = 89;
                }
                break;
            case 355424236:
                if (type.equals("bytes23")) {
                    var2 = 90;
                }
                break;
            case 355424237:
                if (type.equals("bytes24")) {
                    var2 = 91;
                }
                break;
            case 355424238:
                if (type.equals("bytes25")) {
                    var2 = 92;
                }
                break;
            case 355424239:
                if (type.equals("bytes26")) {
                    var2 = 93;
                }
                break;
            case 355424240:
                if (type.equals("bytes27")) {
                    var2 = 94;
                }
                break;
            case 355424241:
                if (type.equals("bytes28")) {
                    var2 = 95;
                }
                break;
            case 355424242:
                if (type.equals("bytes29")) {
                    var2 = 96;
                }
                break;
            case 355424264:
                if (type.equals("bytes30")) {
                    var2 = 97;
                }
                break;
            case 355424265:
                if (type.equals("bytes31")) {
                    var2 = 98;
                }
                break;
            case 355424266:
                if (type.equals("bytes32")) {
                    var2 = 99;
                }
        }

        switch (var2) {
            case 0:
                return new Address(value);
            case 1:
                return new Bool(Boolean.valueOf(value));
            case 2:
                return new Utf8String(value);
            case 3:
                return new DynamicBytes(BytesUtils.toByteArray(value));
            case 4:
                return new Uint8(new BigInteger(value));
            case 5:
                return new Int8(new BigInteger(value));
            case 6:
                return new Uint16(new BigInteger(value));
            case 7:
                return new Int16(new BigInteger(value));
            case 8:
                return new Uint24(new BigInteger(value));
            case 9:
                return new Int24(new BigInteger(value));
            case 10:
                return new Uint32(new BigInteger(value));
            case 11:
                return new Int32(new BigInteger(value));
            case 12:
                return new Uint40(new BigInteger(value));
            case 13:
                return new Int40(new BigInteger(value));
            case 14:
                return new Uint48(new BigInteger(value));
            case 15:
                return new Int48(new BigInteger(value));
            case 16:
                return new Uint56(new BigInteger(value));
            case 17:
                return new Int56(new BigInteger(value));

            case 18:
                return new Uint64(new BigInteger(value));

            case 19:
                return new Int64(new BigInteger(value));

            case 20:
                return new Uint72(new BigInteger(value));

            case 21:
                return new Int72(new BigInteger(value));

            case 22:
                return new Uint80(new BigInteger(value));

            case 23:
                return new Int80(new BigInteger(value));

            case 24:
                return new Uint88(new BigInteger(value));

            case 25:
                return new Int88(new BigInteger(value));

            case 26:
                return new Uint96(new BigInteger(value));

            case 27:
                return new Int96(new BigInteger(value));

            case 28:
                return new Uint104(new BigInteger(value));

            case 29:
                return new Int104(new BigInteger(value));

            case 30:
                return new Uint112(new BigInteger(value));

            case 31:
                return new Int112(new BigInteger(value));

            case 32:
                return new Uint120(new BigInteger(value));

            case 33:
                return new Int120(new BigInteger(value));

            case 34:
                return new Uint128(new BigInteger(value));

            case 35:
                return new Int128(new BigInteger(value));

            case 36:
                return new Uint136(new BigInteger(value));

            case 37:
                return new Int136(new BigInteger(value));

            case 38:
                return new Uint144(new BigInteger(value));

            case 39:
                return new Int144(new BigInteger(value));

            case 40:
                return new Uint152(new BigInteger(value));

            case 41:
                return new Int152(new BigInteger(value));

            case 42:
                return new Uint160(new BigInteger(value));

            case 43:
                return new Int160(new BigInteger(value));

            case 44:
                return new Uint168(new BigInteger(value));

            case 45:
                return new Int168(new BigInteger(value));

            case 46:
                return new Uint176(new BigInteger(value));

            case 47:
                return new Int176(new BigInteger(value));

            case 48:
                return new Uint184(new BigInteger(value));

            case 49:
                return new Int184(new BigInteger(value));

            case 50:
                return new Uint192(new BigInteger(value));

            case 51:
                return new Int192(new BigInteger(value));

            case 52:
                return new Uint200(new BigInteger(value));

            case 53:
                return new Int200(new BigInteger(value));

            case 54:
                return new Uint208(new BigInteger(value));

            case 55:
                return new Int208(new BigInteger(value));

            case 56:
                return new Uint216(new BigInteger(value));

            case 57:
                return new Int216(new BigInteger(value));

            case 58:
                return new Uint224(new BigInteger(value));

            case 59:
                return new Int224(new BigInteger(value));

            case 60:
                return new Uint232(new BigInteger(value));

            case 61:
                return new Int232(new BigInteger(value));

            case 62:
                return new Uint240(new BigInteger(value));

            case 63:
                return new Int240(new BigInteger(value));

            case 64:
                return new Uint248(new BigInteger(value));

            case 65:
                return new Int248(new BigInteger(value));

            case 66:
                return new Uint256(new BigInteger(value));

            case 67:
                return new Int256(new BigInteger(value));

            case 68:
                return new Bytes1(BytesUtils.toByteArray(value));

            case 69:
                return new Bytes2(BytesUtils.toByteArray(value));

            case 70:
                return new Bytes3(BytesUtils.toByteArray(value));

            case 71:
                return new Bytes4(BytesUtils.toByteArray(value));

            case 72:
                return new Bytes5(BytesUtils.toByteArray(value));

            case 73:
                return new Bytes6(BytesUtils.toByteArray(value));

            case 74:
                return new Bytes7(BytesUtils.toByteArray(value));

            case 75:
                return new Bytes8(BytesUtils.toByteArray(value));

            case 76:
                return new Bytes9(BytesUtils.toByteArray(value));

            case 77:
                return new Bytes10(BytesUtils.toByteArray(value));

            case 78:
                return new Bytes11(BytesUtils.toByteArray(value));

            case 79:
                return new Bytes12(BytesUtils.toByteArray(value));

            case 80:
                return new Bytes13(BytesUtils.toByteArray(value));

            case 81:
                return new Bytes14(BytesUtils.toByteArray(value));

            case 82:
                return new Bytes15(BytesUtils.toByteArray(value));

            case 83:
                return new Bytes16(BytesUtils.toByteArray(value));

            case 84:
                return new Bytes17(BytesUtils.toByteArray(value));

            case 85:
                return new Bytes18(BytesUtils.toByteArray(value));

            case 86:
                return new Bytes19(BytesUtils.toByteArray(value));

            case 87:
                return new Bytes20(BytesUtils.toByteArray(value));

            case 88:
                return new Bytes21(BytesUtils.toByteArray(value));

            case 89:
                return new Bytes22(BytesUtils.toByteArray(value));

            case 90:
                return new Bytes23(BytesUtils.toByteArray(value));

            case 91:
                return new Bytes24(BytesUtils.toByteArray(value));

            case 92:
                return new Bytes25(BytesUtils.toByteArray(value));

            case 93:
                return new Bytes26(BytesUtils.toByteArray(value));

            case 94:
                return new Bytes27(BytesUtils.toByteArray(value));

            case 95:
                return new Bytes28(BytesUtils.toByteArray(value));

            case 96:
                return new Bytes29(BytesUtils.toByteArray(value));

            case 97:
                return new Bytes30(BytesUtils.toByteArray(value));

            case 98:
                return new Bytes31(BytesUtils.toByteArray(value));

            case 99:
                return new Bytes32(BytesUtils.toByteArray(value));

            default:
                throw new UnsupportedOperationException("Unsupported type encountered: " + type);
        }
    }

    private static boolean isArray(String type, String value) {
        return type.endsWith("[]") && value.startsWith("[") && value.endsWith("]");
    }
}
