package com.tfg.idprovider.service;

import com.tfg.idprovider.model.PersonalData;
import com.tfg.idprovider.model.Role;
import com.tfg.idprovider.model.User;
import com.tfg.idprovider.model.dto.UserSignUpDto;
import com.tfg.idprovider.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SignUpService {

//    @Autowired
//    RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public SignUpService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /*Permite hacer el login con el username o con el email*/
    public ResponseEntity registerNewUserAccount(UserSignUpDto userSignUpDto) {

        if(userRepository.existsByUsername(userSignUpDto.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        if(userRepository.existsByEmail(userSignUpDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email Address already in use!");
        }

        final PersonalData personalData = getPersonalData(userSignUpDto);

//        Role userRole = roleRepository.findByName(Role.ROLE_USER)
//                .orElseThrow(
//                      () -> new AppException("User Role not set.")
//                );
//        user.setRoles(Collections.singleton(userRole));

        final User registeredUser = saveNewUser(userSignUpDto, personalData);

        return ResponseEntity.ok().body("User registered successfully with id: " + registeredUser.getId());
    }

    private User saveNewUser(UserSignUpDto accountDto, PersonalData personalData) {
        final User newUser = User.UserBuilder.builder()
                .withId(new ObjectId())
                .withUsername(accountDto.getUsername())
                .withPassword(passwordEncoder.encode(accountDto.getPassword()))         //password Encriptat amb BCrypt
                .withEmail(accountDto.getEmail())
                .withAuthorities(Collections.singletonList(Role.ROLE_USER))
                .withPersonalData(personalData)
                .build();

        userRepository.save(newUser);

        return newUser;
    }

    private PersonalData getPersonalData(UserSignUpDto accountDto) {
        return PersonalData.PersonalDataBuilder.builder()
                    .withFirstName(accountDto.getFirstName())
                    .withLastName(accountDto.getLastName())
                    .build();
    }
}
