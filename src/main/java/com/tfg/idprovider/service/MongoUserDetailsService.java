package com.tfg.idprovider.service;

import com.tfg.idprovider.model.MyUser;
import com.tfg.idprovider.model.User;
import com.tfg.idprovider.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MongoUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public MongoUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        final User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(
                        () ->new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail)
                );

        final MyUser myUser = MyUser.create(user);

        return myUser;
    }

    // Este metodo lo utiliza JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(ObjectId id) {
        final User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found with id : " + id)
                );

        final MyUser myUser = MyUser.create(user);

        return myUser;
    }

}
