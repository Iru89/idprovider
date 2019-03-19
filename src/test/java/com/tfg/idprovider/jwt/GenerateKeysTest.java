package com.tfg.idprovider.jwt;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class GenerateKeysTest {


    private void writeToFile(String path, byte[] key) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(key);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    @Test
    public void test() {
        GenerateKeys gk;
        try {
            gk = new GenerateKeys(1024);
            gk.createKeys();
            writeToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
            System.out.println(gk.getPublicKey().toString());
            writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
            System.out.println(gk.getPrivateKey().toString());
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

/*
    public PrivateKey getPrivate() throws Exception {
        String filename = "KeyPair/privateKey";
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        PKCS8EncodedKeySpec PKCS8spec = new PKCS8EncodedKeySpec(keyBytes);
        System.out.println(PKCS8spec.toString());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(PKCS8spec);
    }


    public PublicKey getPublic() throws Exception {
        String filename = "KeyPair/privateKey";
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        X509EncodedKeySpec X509spec = new X509EncodedKeySpec(keyBytes);
        System.out.println(X509spec.toString());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(X509spec);
    }
    */
}