package com.tfg.idprovider.service;

import com.tfg.idprovider.model.MyUserDetails;
import com.tfg.idprovider.repository.MyUserDetailsRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MongoUserDetailsService implements UserDetailsService {


    private MyUserDetailsRepository myUserDetailsRepository;

    public MongoUserDetailsService(MyUserDetailsRepository myUserDetailsRepository) {
        this.myUserDetailsRepository = myUserDetailsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUserDetails userDetails = myUserDetailsRepository.findByUsername(username);

        if(userDetails == null) {
            throw new UsernameNotFoundException("The user with username "+ username +" can not be found");
        }

        return userDetails;
    }

    /*private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role: roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getPrivileges().stream()
                    .map(p -> new SimpleGrantedAuthority(p.getName()))
                    .forEach(authorities::add);
        }

        return authorities;
    }*/
}
