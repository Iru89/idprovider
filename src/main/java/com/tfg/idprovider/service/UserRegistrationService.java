package com.tfg.idprovider.service;

import com.tfg.idprovider.exception.UserAlreadyExistException;
import com.tfg.idprovider.jwt.GenerateKeys;
import com.tfg.idprovider.model.MyUser;
import com.tfg.idprovider.model.MyUserDetails;
import com.tfg.idprovider.model.PersonalData;
import com.tfg.idprovider.model.Role;
import com.tfg.idprovider.model.dto.UserRegistrationDto;
import com.tfg.idprovider.repository.MyUserDetailsRepository;
import com.tfg.idprovider.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

@Service
public class UserRegistrationService {

    private static final int KEY_LENGTH = 1024;

    private PasswordEncoder passwordEncoder;
    private MyUserDetailsRepository myUserDetailsRepository;
    private UserRepository userRepository;

    public UserRegistrationService(PasswordEncoder passwordEncoder, MyUserDetailsRepository myUserDetailsRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.myUserDetailsRepository = myUserDetailsRepository;
        this.userRepository = userRepository;
    }

    public MyUser registerNewUserAccount(UserRegistrationDto accountDto) throws UserAlreadyExistException, NoSuchAlgorithmException {
        if (emailExists(accountDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email adress: " + accountDto.getEmail());
        }else if (usernameExists(accountDto.getUsername())) {
            throw new UserAlreadyExistException("There is an account with that username: " + accountDto.getUsername());
        }

        final MyUserDetails userDetails = MyUserDetails.MyUserDeitailsBuilder.builder()
                .withUsername(accountDto.getUsername())
                .withPassword(passwordEncoder.encode(accountDto.getPassword()))         //password Encriptat amb BCrypt
                .withAuthorities(Collections.singletonList( new SimpleGrantedAuthority(Role.ROLE_USER.name())))    //Role USER
                .withAccountNonExpired(false)
                .withAccountNonLocked(false)
                .withCredentialsNonExpired(false)
                .withEnabled(false)
                .build();

        final MyUserDetails myUserDetails = myUserDetailsRepository.save(userDetails);

        final PersonalData personalData = PersonalData.PersonalDataBuilder.builder()
                .withFirstName(accountDto.getFirstName())
                .withLastName(accountDto.getLastName())
                .withEmail(accountDto.getEmail())
                .build();

        GenerateKeys generateKeys = new GenerateKeys(KEY_LENGTH);
        final KeyPair keyPair = generateKeys.createKeys();

        final MyUser newUser = MyUser.MyUserBuilder.builder()
                .withUserDetailsId(myUserDetails.get_id())
                .withMyUserAccess(userDetails)
                .withKeyPair(keyPair)
                .withPersonalData(personalData)
                .build();

        return userRepository.save(newUser);
    }

    private boolean emailExists(final String email) {
        return myUserDetailsRepository.findByEmail(email) != null;
    }

    private boolean usernameExists(final String username) {
        return myUserDetailsRepository.findByEmail(username) != null;
    }
}
