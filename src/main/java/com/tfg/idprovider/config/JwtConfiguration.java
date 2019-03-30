package com.tfg.idprovider.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

@Configuration
public class JwtConfiguration {

    @Value("${idp.keystore.source}")
    private String keyStoreSource;

    @Value("${idp.keystore.password}")
    private String keyStorePassword;

    @Value("${idp.keystore.signing-key-alias}")
    private String signingKeyAlias;

    @Bean
    public KeyPair keyPair() throws Exception {
        final KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new ClassPathResource(keyStoreSource).getInputStream(), keyStorePassword.toCharArray());
        final Key key = keyStore.getKey(signingKeyAlias, keyStorePassword.toCharArray());
        final Certificate certificate = keyStore.getCertificate(signingKeyAlias);
        return new KeyPair(certificate.getPublicKey(), (PrivateKey) key);
    }

}