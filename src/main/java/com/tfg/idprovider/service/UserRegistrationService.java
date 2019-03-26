package com.tfg.idprovider.service;

import com.tfg.idprovider.jwt.GenerateKeys;
import com.tfg.idprovider.model.MyUser;
import com.tfg.idprovider.model.MyUserDetails;
import com.tfg.idprovider.model.PersonalData;
import com.tfg.idprovider.model.Role;
import com.tfg.idprovider.model.dto.UserRegistrationDto;
import com.tfg.idprovider.repository.MyUserDetailsRepository;
import com.tfg.idprovider.repository.UserRepository;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity registerNewUserAccount(UserRegistrationDto accountDto) {
        if (emailExists(accountDto.getEmail())) {
            return ResponseEntity.badRequest().body("There is an account with that email adress: " + accountDto.getEmail());
        }else if (usernameExists(accountDto.getUsername())) {
            return ResponseEntity.badRequest().body("There is an account with that username: " + accountDto.getUsername());
        }

        final MyUserDetails userDetails = getMyUserDetails(accountDto);

        final MyUserDetails myUserDetails = myUserDetailsRepository.save(userDetails);

        final PersonalData personalData = getPersonalData(accountDto);

        GenerateKeys generateKeys = null;
        try {
            generateKeys = new GenerateKeys(KEY_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            ResponseEntity.badRequest().body(e.toString());
        }

        final KeyPair keyPair = generateKeys.createKeys();

        final MyUser registeredUser = getMyUser(userDetails, myUserDetails, personalData, keyPair);

        return ResponseEntity.ok().body(registeredUser);
    }

    private MyUser getMyUser(MyUserDetails userDetails, MyUserDetails myUserDetails, PersonalData personalData, KeyPair keyPair) {
        final MyUser newUser = MyUser.MyUserBuilder.builder()
                .withUserDetailsId(myUserDetails.get_id())
                .withMyUserAccess(userDetails)
                .withKeyPair(keyPair)
                .withPersonalData(personalData)
                .build();

        return userRepository.save(newUser);
    }

    private PersonalData getPersonalData(UserRegistrationDto accountDto) {
        return PersonalData.PersonalDataBuilder.builder()
                    .withFirstName(accountDto.getFirstName())
                    .withLastName(accountDto.getLastName())
                    .withEmail(accountDto.getEmail())
                    .build();
    }

    private MyUserDetails getMyUserDetails(UserRegistrationDto accountDto) {
        return MyUserDetails.MyUserDeitailsBuilder.builder()
                    .withUsername(accountDto.getUsername())
                    .withPassword(passwordEncoder.encode(accountDto.getPassword()))         //password Encriptat amb BCrypt
                    .withAuthorities(Collections.singletonList( new SimpleGrantedAuthority(Role.ROLE_USER.name())))    //Role USER
                    .withAccountNonExpired(false)
                    .withAccountNonLocked(false)
                    .withCredentialsNonExpired(false)
                    .withEnabled(false)
                    .build();
    }

    private boolean emailExists(final String email) {
        return myUserDetailsRepository.findByEmail(email) != null;
    }

    private boolean usernameExists(final String username) {
        return myUserDetailsRepository.findByEmail(username) != null;
    }
}
