package com.tfg.idprovider.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.bson.types.ObjectId;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;


@JsonDeserialize(builder = MyUser.MyUserBuilder.class)
public class MyUser {

    private final ObjectId _id;

    private final String username;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final List<SimpleGrantedAuthority> authorities;

    private MyUser(ObjectId _id, String username, String password,String firstName, String lastName, String email, List<SimpleGrantedAuthority> authorities) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.authorities = authorities;
    }

    public ObjectId get_id() {
        return _id;
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

    public List<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonPOJOBuilder
    public static class MyUserBuilder{

        private ObjectId _id;

        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String email;
        private List<SimpleGrantedAuthority> authorities;

        private MyUserBuilder() {
        }

        public static MyUserBuilder builder(){
            return new MyUserBuilder();
        }

        public MyUserBuilder with_id(ObjectId _id) {
            this._id = _id;
            return this;
        }

        public MyUserBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public MyUserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public MyUserBuilder withFirstName(String firstName){
            this.firstName = firstName;
            return this;
        }

        public MyUserBuilder withLastName(String lastName){
            this.lastName = lastName;
            return this;
        }

        public MyUserBuilder withEmail(String email){
            this.email = email;
            return this;
        }

        public MyUserBuilder withAuthorities(List<SimpleGrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public MyUser build(){
            return new MyUser(_id, username, password, firstName, lastName, email,  authorities);
        }


    }
}

