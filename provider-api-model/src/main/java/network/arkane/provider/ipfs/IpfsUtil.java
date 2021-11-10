package network.arkane.provider.ipfs;

public final class IpfsUtil {
    private IpfsUtil() {
    }

    public static String replaceIpfsLink(String link) {
        if (link != null && link.length() > 0) {
            if (link.startsWith("ipfs")) {
                String cid = link.replace("ipfs://", "");
                if (!cid.startsWith("ipfs/")) {
                    cid = "ipfs/" + cid;
                }
                return "https://ipfs.io/" + cid;
            } else if (link.startsWith("https://gateway.pinata.cloud")) {
                return link.replaceFirst("https://gateway.pinata.cloud", "https://ipfs.io");
            }
        }
        return link;
    }
}
