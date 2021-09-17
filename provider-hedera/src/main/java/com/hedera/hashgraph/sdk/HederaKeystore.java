package com.hedera.hashgraph.sdk;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HederaKeystore {
    private final Keystore keystore;

    public HederaKeystore(PrivateKey privateKey) {
        this.keystore = new Keystore(privateKey);
    }

    public HederaKeystore(String keystore,
                          String password) {
        try {
            this.keystore = Keystore.fromStream(new ByteArrayInputStream(keystore.getBytes()), password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PrivateKey getPrivateKey() {
        return this.keystore.getEd25519();
    }

    public String export(String password) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            this.keystore.export(baos, password);
            return baos.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
