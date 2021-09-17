package network.arkane.provider.contract;

import network.arkane.provider.BytesUtils;
import network.arkane.provider.Prefix;
import org.web3j.abi.datatypes.Type;

import java.math.BigInteger;

public class ContractCallResultTypeFactory {
    private ContractCallResultTypeFactory() {
    }

    public static <T> ContractCallResultType getType(final Type<T> web3jType) {
        final String type = web3jType.getTypeAsString();
        final T value = web3jType.getValue();
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
            case 2:
                return ContractCallStringResult.builder()
                                               .type(type)
                                               .value((String) value)
                                               .build();
            case 1:
                return ContractCallBooleanResult.builder()
                                                .type(type)
                                                .value((Boolean) value)
                                                .build();
            case 3:
            case 68:
            case 69:
            case 71:
            case 70:
            case 73:
            case 72:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
                return ContractCallHexResult.builder()
                                            .type(type)
                                            .value(BytesUtils.toHexString((byte[]) value, Prefix.ZeroLowerX))
                                            .build();
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
                return ContractCallNumericResult.builder()
                                                .type(type)
                                                .value((BigInteger) value)
                                                .build();
            default:
                throw new UnsupportedOperationException("Unsupported type encountered: " + type);
        }
    }

}