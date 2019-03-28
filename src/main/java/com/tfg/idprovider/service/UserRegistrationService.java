package com.tfg.idprovider.service;

import com.tfg.idprovider.model.MyUser;
import com.tfg.idprovider.model.PersonalData;
import com.tfg.idprovider.model.Role;
import com.tfg.idprovider.model.dto.UserRegistrationDto;
import com.tfg.idprovider.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserRegistrationService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public UserRegistrationService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public ResponseEntity registerNewUserAccount(UserRegistrationDto accountDto) {

        if (usernameExists(accountDto.getUsername())) {
            return ResponseEntity.badRequest().body("There is an account with that username: " + accountDto.getUsername());
        }

        final PersonalData personalData = getPersonalData(accountDto);

        final MyUser registeredUser = saveNewMyUser(accountDto, personalData);

        return ResponseEntity.ok().body("User " + registeredUser.getId() + " registered");
    }

    private MyUser saveNewMyUser(UserRegistrationDto accountDto, PersonalData personalData) {
        final MyUser newUser = MyUser.MyUserBuilder.builder()
                .withId(new ObjectId())
                .withUsername(accountDto.getUsername())
                .withPassword(passwordEncoder.encode(accountDto.getPassword()))         //password Encriptat amb BCrypt
                .withEmail(accountDto.getEmail())
                .withAuthorities(Collections.singletonList(new SimpleGrantedAuthority(Role.ROLE_USER.name())))    //Role USER
                .withAccountNonExpired(false)
                .withAccountNonLocked(false)
                .withCredentialsNonExpired(false)
                .withEnabled(false)
                .withPersonalData(personalData)
                .build();

        userRepository.save(newUser);

        return newUser;
    }

    private PersonalData getPersonalData(UserRegistrationDto accountDto) {
        return PersonalData.PersonalDataBuilder.builder()
                    .withFirstName(accountDto.getFirstName())
                    .withLastName(accountDto.getLastName())
                    .build();
    }
    private boolean usernameExists(final String username) {
        MyUser myUser = userRepository.findByUsername(username);
        return myUser != null;
    }
}
