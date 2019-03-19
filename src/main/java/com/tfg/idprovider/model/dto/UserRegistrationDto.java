package com.tfg.idprovider.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = UserRegistrationDto.UserRegistrationDtoBuilder.class)
public class UserRegistrationDto {

    private final String username;
    private final String password;

    private final String firstName;
    private final String lastName;
    private final String email;

    public UserRegistrationDto(String username, String password, String firstName, String lastName, String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @JsonPOJOBuilder
    public class UserRegistrationDtoBuilder{

        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String email;

        private UserRegistrationDtoBuilder() {
        }

        public UserRegistrationDtoBuilder builder(){
            return new UserRegistrationDtoBuilder();
        }

        public UserRegistrationDtoBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserRegistrationDtoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserRegistrationDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserRegistrationDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserRegistrationDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserRegistrationDto build(){
            return new UserRegistrationDto(username, password, firstName, lastName, email);
        }
    }

}
