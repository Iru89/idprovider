package com.tfg.idprovider.jwt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class GenerateKeys {

    private KeyPairGenerator keyPairGenerator;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public GenerateKeys(int keyLength) throws NoSuchAlgorithmException {
        this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        this.keyPairGenerator.initialize(keyLength);
    }

    public void createKeys() throws IOException {
        KeyPair keyPair = this.keyPairGenerator.generateKeyPair();

        this.privateKey = keyPair.getPrivate();
        writeToFile("KeyPair/privateKey", getPrivateKey().getEncoded());

        this.publicKey = keyPair.getPublic();
        writeToFile("KeyPair/publicKey", getPublicKey().getEncoded());
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    private void writeToFile(String path, byte[] key) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(key);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public PrivateKey getPrivateKeyFromFile() throws Exception {
        String filename = "KeyPair/privateKey";
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        PKCS8EncodedKeySpec PKCS8spec = new PKCS8EncodedKeySpec(keyBytes);
//        System.out.println(PKCS8spec.toString());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(PKCS8spec);
    }


    public PublicKey getPublicKeyFromFile() throws Exception {
        String filename = "KeyPair/privateKey";
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        X509EncodedKeySpec X509spec = new X509EncodedKeySpec(keyBytes);
//        System.out.println(X509spec.toString());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(X509spec);
    }

}