package com.tfg.idprovider.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@JsonDeserialize(builder = MyUserAccess.MyUserAccessBuilder.class)
public class MyUserAccess implements UserDetails {

    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    private MyUserAccess(String username, String password, List<SimpleGrantedAuthority> authorities, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonPOJOBuilder
    public class MyUserAccessBuilder{
        private String username;
        private String password;
        private List<SimpleGrantedAuthority> authorities;
        private boolean accountNonExpired;
        private boolean accountNonLocked;
        private boolean credentialsNonExpired;
        private boolean enabled;

        private MyUserAccessBuilder() {
        }

        public MyUserAccessBuilder builder(){
            return new MyUserAccessBuilder();
        }

        public MyUserAccessBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public MyUserAccessBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public MyUserAccessBuilder withAuthorities(List<SimpleGrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public MyUserAccessBuilder withAccountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
            return this;
        }

        public MyUserAccessBuilder withAccountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public MyUserAccessBuilder withCredentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public MyUserAccessBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public MyUserAccess build(){
            return new MyUserAccess(username, password, authorities, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
        }
    }
}
