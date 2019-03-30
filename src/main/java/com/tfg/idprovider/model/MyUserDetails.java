package com.tfg.idprovider.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;


@JsonDeserialize(builder = MyUser.MyUserBuilder.class)
public class MyUser implements UserDetails {

    @Id
    private final ObjectId id;

    private final String username;
    private final String password;
    private final String email;
    private final List<SimpleGrantedAuthority> authorities;
    private final PersonalData personalData;

    private MyUser(ObjectId id, String username, String password, String email, List<SimpleGrantedAuthority> authorities,  PersonalData personalData) {
        this.id = id;

        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.personalData = personalData;
    }

    public static MyUser create(User user) {
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return MyUserBuilder.builder()
                .withId(user.getId())
                .withUsername(user.getUsername())
                .withPassword(user.getPassword())
                .withEmail(user.getEmail())
                .withAuthorities(authorities)
                .withPersonalData(user.getPersonalData())
                .build();
    }

    public ObjectId getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    @JsonPOJOBuilder
    public static class MyUserBuilder{

        private ObjectId id;

        private String username;
        private String password;
        private String email;
        private List<SimpleGrantedAuthority> authorities;
        private PersonalData personalData;

        private MyUserBuilder() {
        }

        public static MyUserBuilder builder(){
            return new MyUserBuilder();
        }

        public MyUserBuilder withId(ObjectId id) {
            this.id = id;
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

        public MyUserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public MyUserBuilder withAuthorities(List<SimpleGrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public MyUserBuilder withPersonalData(PersonalData personalData) {
            this.personalData = personalData;
            return this;
        }

        public MyUser build(){
            return new MyUser(id, username, password, email, authorities, personalData);
        }
    }
}

