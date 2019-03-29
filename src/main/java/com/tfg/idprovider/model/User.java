package com.tfg.idprovider.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

public class User {

    @Id
    private ObjectId id;

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private List<Role> roles;
    private PersonalData personalData;

    private User(ObjectId id, String username, String password, String firstName, String lastName, String email, List<Role> roles, PersonalData personalData) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
        this.personalData = personalData;
    }

    public ObjectId getId() {
        return id;
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

    public List<Role> getRoles() {
        return roles;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public static class UserBuilder{
        private  ObjectId id;
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String email;
        private List<Role> roles;
        private PersonalData personalData;

        private UserBuilder() {
        }

        public static UserBuilder builder(){
            return new UserBuilder();
        }

        public UserBuilder withId(ObjectId id) {
            this.id = id;
            return this;
        }

        public UserBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder withAuthorities(List<Role> roles) {
            this.roles = roles;
            return this;
        }

        public UserBuilder withPersonalData(PersonalData personalData) {
            this.personalData = personalData;
            return this;
        }

        public User build(){
            return new User(id, username, password, firstName, lastName, email, roles, personalData);
        }
    }
}
