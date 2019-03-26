package com.tfg.idprovider.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@JsonDeserialize(builder = MyUserDetails.MyUserDeitailsBuilder.class)
public class MyUserDetails implements UserDetails {

    private final ObjectId _id;

    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    private MyUserDetails(ObjectId _id, String username, String password, List<SimpleGrantedAuthority> authorities, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public ObjectId get_id() {
        return _id;
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
    public static class MyUserDeitailsBuilder {
        private ObjectId _id;
        private String username;
        private String password;
        private List<SimpleGrantedAuthority> authorities;
        private boolean accountNonExpired;
        private boolean accountNonLocked;
        private boolean credentialsNonExpired;
        private boolean enabled;

        private MyUserDeitailsBuilder() {
        }

        public static MyUserDeitailsBuilder builder(){
            return new MyUserDeitailsBuilder();
        }

        public MyUserDeitailsBuilder with_id(ObjectId _id) {
            this._id = _id;
            return this;
        }

        public MyUserDeitailsBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public MyUserDeitailsBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public MyUserDeitailsBuilder withAuthorities(List<SimpleGrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public MyUserDeitailsBuilder withAccountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
            return this;
        }

        public MyUserDeitailsBuilder withAccountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public MyUserDeitailsBuilder withCredentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public MyUserDeitailsBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public MyUserDetails build(){
            return new MyUserDetails(_id, username, password, authorities, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
        }
    }
}
