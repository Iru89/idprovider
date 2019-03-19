package com.tfg.idprovider.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = UserLogInDto.UserLogInDtoBuilder.class)
public class UserLogInDto {

    private final String username;
    private final String password;

    private UserLogInDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @JsonPOJOBuilder
    public static class UserLogInDtoBuilder{

        private String username;
        private String password;

        private UserLogInDtoBuilder() {
        }

        public static UserLogInDtoBuilder builder(){
            return new UserLogInDtoBuilder();
        }

        public UserLogInDtoBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserLogInDtoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserLogInDto build(){
            return new UserLogInDto(username, password);
        }
    }

}

