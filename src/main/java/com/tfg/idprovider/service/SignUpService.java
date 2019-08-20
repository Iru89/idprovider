package com.tfg.idprovider.service;

import com.tfg.idprovider.model.PersonalData;
import com.tfg.idprovider.model.PersonalData.PersonalDataBuilder;
import com.tfg.idprovider.model.Role;
import com.tfg.idprovider.model.User;
import com.tfg.idprovider.model.User.UserBuilder;
import com.tfg.idprovider.model.dto.UserSignUpDto;
import com.tfg.idprovider.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SignUpService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public SignUpService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /*Permet fer el login amb el username o el email*/
    public ResponseEntity registerNewUserAccount(UserSignUpDto userSignUpDto) {

        if(userRepository.existsByUsername(userSignUpDto.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        if(userRepository.existsByEmail(userSignUpDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email Address already in use!");
        }

        final PersonalData personalData = getPersonalData(userSignUpDto);

        final User registeredUser = saveNewUser(userSignUpDto, personalData);

        return ResponseEntity.ok().body("User registered successfully with id: " + registeredUser.getId());
    }

    private User saveNewUser(UserSignUpDto accountDto, PersonalData personalData) {
        final User newUser = UserBuilder.builder()
                .withId(new ObjectId())
                .withjwtRefreshId(new ObjectId())
                .withUsername(accountDto.getUsername())
                .withPassword(passwordEncoder.encode(accountDto.getPassword()))         //password encriptat amb BCrypt
                .withEmail(accountDto.getEmail())
                .withAuthorities(Collections.singletonList(Role.ROLE_USER))
                .withPersonalData(personalData)
                .build();

        User saveUser = userRepository.save(newUser);

        return saveUser;
    }

    private PersonalData getPersonalData(UserSignUpDto accountDto) {
        final PersonalData  personalData = PersonalDataBuilder.builder()
                    .withFirstName(accountDto.getFirstName())
                    .withLastName(accountDto.getLastName())
                    .build();

        return personalData;
    }
}
