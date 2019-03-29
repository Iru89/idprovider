package com.tfg.idprovider.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = UserLogInDto.UserLogInDtoBuilder.class)
public class UserLogInDto {

    private final String usernameOrEmail;
    private final String password;

    private UserLogInDto(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    @JsonPOJOBuilder
    public static class UserLogInDtoBuilder{

        private String usernameOrEmail;
        private String password;

        private UserLogInDtoBuilder() {
        }

        public static UserLogInDtoBuilder builder(){
            return new UserLogInDtoBuilder();
        }

        public UserLogInDtoBuilder withUsernameOrEmail(String usernameOrEmail) {
            this.usernameOrEmail = usernameOrEmail;
            return this;
        }

        public UserLogInDtoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserLogInDto build(){
            return new UserLogInDto(usernameOrEmail, password);
        }
    }

}

