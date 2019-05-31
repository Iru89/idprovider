package com.tfg.idprovider.model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;


public class MyUserDetails implements UserDetails {

    @Id
    private final ObjectId id;
    private final ObjectId jwtRefreshId;

    private final String username;
    private final String password;
    private final String email;
    private final List<SimpleGrantedAuthority> authorities;
    private final PersonalData personalData;

    private MyUserDetails(ObjectId id, ObjectId jwtRefreshId, String username, String password, String email, List<SimpleGrantedAuthority> authorities, PersonalData personalData) {
        this.id = id;
        this.jwtRefreshId = jwtRefreshId;

        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.personalData = personalData;
    }

    public static MyUserDetails create(User user) {
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new MyUserDetails(
                user.getId(),
                user.getJwtRefreshId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                authorities,
                user.getPersonalData());

    }

    public ObjectId getId() {
        return id;
    }

    public ObjectId getJwtRefreshId() {
        return jwtRefreshId;
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

}

