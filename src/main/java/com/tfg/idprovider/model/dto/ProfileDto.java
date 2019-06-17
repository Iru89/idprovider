package com.tfg.idprovider.model.dto;

import com.tfg.idprovider.model.PersonalData;
import com.tfg.idprovider.model.Role;

import java.util.List;

public class ProfileDto {

    private final String username;
    private final String email;
    private final List<Role> roles;
    private final PersonalData personalData;

    private ProfileDto(String username, String email, List<Role> roles, PersonalData personalData) {
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.personalData = personalData;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public static class ProfileDtoBuilder{
        private String username;
        private String email;
        private List<Role> roles;
        private PersonalData personalData;

        private ProfileDtoBuilder() {
        }

        public static ProfileDtoBuilder builder(){
            return new ProfileDtoBuilder();
        }


        public ProfileDtoBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public ProfileDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public ProfileDtoBuilder withAuthorities(List<Role> roles) {
            this.roles = roles;
            return this;
        }

        public ProfileDtoBuilder withPersonalData(PersonalData personalData) {
            this.personalData = personalData;
            return this;
        }

        public ProfileDto build(){
            return new ProfileDto(username, email, roles, personalData);
        }
    }
}
