package com.tfg.idprovider.service;

import com.tfg.idprovider.model.MyUser;
import com.tfg.idprovider.model.PersonalData;
import com.tfg.idprovider.model.Role;
import com.tfg.idprovider.model.dto.UserSignUpDto;
import com.tfg.idprovider.repository.UserRepository;
import com.tfg.idprovider.security.JwtTokenProvider;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SignUpService {

//    @Autowired
//    RoleRepository roleRepository;

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public SignUpService(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public ResponseEntity registerNewUserAccount(UserSignUpDto accountDto) {

        if(userRepository.existsByUsername(accountDto.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        if(userRepository.existsByEmail(accountDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email Address already in use!");
        }

        final PersonalData personalData = getPersonalData(accountDto);

//        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
//                .orElseThrow(() -> new AppException("User Role not set."));
//        user.setRoles(Collections.singleton(userRole));

        final MyUser registeredUser = saveNewMyUser(accountDto, personalData);

        return ResponseEntity.ok().body("User registered successfully");
    }

    private MyUser saveNewMyUser(UserSignUpDto accountDto, PersonalData personalData) {
        final MyUser newUser = MyUser.MyUserBuilder.builder()
                .withId(new ObjectId())
                .withUsername(accountDto.getUsername())
                .withPassword(passwordEncoder.encode(accountDto.getPassword()))         //password Encriptat amb BCrypt
                .withEmail(accountDto.getEmail())
                .withAuthorities(Collections.singletonList(new SimpleGrantedAuthority(Role.ROLE_USER.name())))    //Role USER
                .withAccountNonExpired(true)
                .withAccountNonLocked(true)
                .withCredentialsNonExpired(true)
                .withEnabled(true)
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
    private boolean usernameExists(final String username) {
        MyUser myUser = userRepository.findByUsername(username);
        return myUser != null;
    }
}
