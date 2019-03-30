package com.tfg.idprovider.model.dto;

public class JwtAuthenticationDto {

    private final String accessToken;
    private final String TokenType;

    private JwtAuthenticationDto(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        TokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return TokenType;
    }

    public static class JwtAuthenticationDtoBuilder{
        private String accessToken;

        private JwtAuthenticationDtoBuilder() {
        }

        public static JwtAuthenticationDtoBuilder builder(){
            return new JwtAuthenticationDtoBuilder();
        }

        public JwtAuthenticationDtoBuilder withAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public JwtAuthenticationDto build(){
            return new JwtAuthenticationDto(accessToken, "Bearer");
        }
    }
}
