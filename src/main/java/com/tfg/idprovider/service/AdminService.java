package com.tfg.idprovider.service;

import com.tfg.idprovider.model.User;
import com.tfg.idprovider.model.dto.ProfileDto;
import com.tfg.idprovider.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Secured("ROLE_ADMIN")
    public ResponseEntity listUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok().body(users);
    }

    public ResponseEntity getMyProfile(String username) {

        final User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username : " + username)
        );

        ProfileDto profile = ProfileDto.ProfileDtoBuilder
                .builder()
                .withUsername(user.getUsername())
                .withEmail(user.getEmail())
                .withAuthorities(user.getRoles())
                .withPersonalData(user.getPersonalData())
                .build();

        return ResponseEntity.ok().body(profile);

    }
}
