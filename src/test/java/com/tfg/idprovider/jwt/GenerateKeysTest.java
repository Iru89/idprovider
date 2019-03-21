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
        GenerateKeys generateKeys;
        try {
            generateKeys = new GenerateKeys(1024);
            generateKeys.createKeys();
            writeToFile("KeyPair/publicKey", generateKeys.getPublicKey().getEncoded());
            System.out.println(generateKeys.getPublicKey().toString());
            writeToFile("KeyPair/privateKey", generateKeys.getPrivateKey().getEncoded());
            System.out.println(generateKeys.getPrivateKey().toString());
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}