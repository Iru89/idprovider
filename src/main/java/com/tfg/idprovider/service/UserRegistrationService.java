package com.tfg.idprovider.service;

import com.tfg.idprovider.exception.UserAlreadyExistException;
import com.tfg.idprovider.model.MyUser;
import com.tfg.idprovider.model.dto.UserRegistrationDto;
import com.tfg.idprovider.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserRegistrationService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public UserRegistrationService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public MyUser registerNewUserAccount(UserRegistrationDto accountDto) throws UserAlreadyExistException {
        if (emailExists(accountDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email adress: " + accountDto.getEmail());
        }else if (usernameExists(accountDto.getUsername())) {
            throw new UserAlreadyExistException("There is an account with that username: " + accountDto.getUsername());
        }

        final MyUser user = MyUser.MyUserBuilder.builder()
                .withUsername(accountDto.getUsername())
                .withPassword(passwordEncoder.encode(accountDto.getPassword()))         //password encriptat amb BCrypt
                .withEmail(accountDto.getEmail())
                .withFirstName(accountDto.getFirstName())
                .withLastName(accountDto.getLastName())
                .withAuthorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER") ))   //Definir varis ROLES
                .build();

        return userRepository.save(user);
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }

    private boolean usernameExists(final String username) {
        return userRepository.findByEmail(username) != null;
    }
}
