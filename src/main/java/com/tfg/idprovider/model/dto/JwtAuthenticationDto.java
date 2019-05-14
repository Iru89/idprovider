package com.tfg.idprovider.model.dto;

public class JwtAuthenticationDto {

    private final String accessToken;
    private final String refreshToken;
    private final String tokenType;

    private JwtAuthenticationDto(String accessToken, String refreshToken, String tokenType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public static class JwtAuthenticationDtoBuilder{
        private String accessToken;
        private String refreshToken;

        private JwtAuthenticationDtoBuilder() {
        }

        public static JwtAuthenticationDtoBuilder builder(){
            return new JwtAuthenticationDtoBuilder();
        }

        public JwtAuthenticationDtoBuilder withAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public JwtAuthenticationDtoBuilder withRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public JwtAuthenticationDto build(){
            return new JwtAuthenticationDto(accessToken, refreshToken,"Bearer");
        }
    }
}
