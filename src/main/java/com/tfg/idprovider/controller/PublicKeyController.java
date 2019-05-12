package com.tfg.idprovider.controller;

import com.tfg.idprovider.model.dto.PublicKeyDto;
import com.tfg.idprovider.model.dto.PublicKeyDto.PublicKeyDtoBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Base64;

@RestController
@RequestMapping("/api")
public class PublicKeyController {

    private KeyPair keyPair;

    public PublicKeyController(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @RequestMapping(value = "/publicKey", method = RequestMethod.GET)
    public ResponseEntity getPublicKey(){
        PublicKey publicKey = keyPair.getPublic();
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        PublicKeyDto publicKeyDto = PublicKeyDtoBuilder
                .builder()
                .withPublicKeyBase64(publicKeyBase64)
                .build();
        return ResponseEntity.ok().body(publicKeyDto);
    }
}
