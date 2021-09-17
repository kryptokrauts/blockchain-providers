package com.hedera.hashgraph.sdk;

import java.util.Collections;

public class NetworkAddressBookFix {
    public static void initializeEmptyAddressBook() {
        Network.nodeAddressBooks.put(NetworkName.MAINNET, Collections.emptyMap());
        Network.nodeAddressBooks.put(NetworkName.TESTNET, Collections.emptyMap());
        Network.nodeAddressBooks.put(NetworkName.PREVIEWNET, Collections.emptyMap());
    }
}
