package com.tfg.idprovider.model.dto;

public class PublicKeyDto {

    private final String publicKeyBase64;

    private PublicKeyDto(String publicKeyBase64) {
        this.publicKeyBase64 = publicKeyBase64;
    }

    public String getPublicKeyBase64() {
        return publicKeyBase64;
    }

    public static class PublicKeyDtoBuilder{
        private String publicKeyBase64;

        private PublicKeyDtoBuilder() {
        }

        public static PublicKeyDtoBuilder builder(){
            return new PublicKeyDtoBuilder();
        }

        public PublicKeyDtoBuilder withPublicKeyBase64(String publicKeyBase64) {
            this.publicKeyBase64 = publicKeyBase64;
            return this;
        }

        public PublicKeyDto build(){
            return new PublicKeyDto(publicKeyBase64);
        }
    }
}
