package com.tfg.idprovider.service;

import com.tfg.idprovider.model.MyUser;
import com.tfg.idprovider.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MongoUserDetailsService implements UserDetailsService {


    private UserRepository userRepository;

    public MongoUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Aqui utilitzo la classe MyUser definit per mi, tot i que al final retornare un User de org.springframework.security.core.userdetails.User
        MyUser myUser = userRepository.findByUsername(username);

        if(myUser == null) {
            throw new UsernameNotFoundException("MyUser not found");
        }


        return new User(myUser.getUsername(), myUser.getPassword(), myUser.getAuthorities());
    }
}
