package com.tfg.idprovider.service;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.algorithms.Algorithm;

import com.tfg.idprovider.exception.UserAlreadyExistException;
import com.tfg.idprovider.jwt.GenerateKeys;
import com.tfg.idprovider.jwt.JSONWebToken;
import com.tfg.idprovider.model.MyUser;
import com.tfg.idprovider.model.MyUser.MyUserBuilder;
import com.tfg.idprovider.model.dto.UserLogInDto;
import com.tfg.idprovider.model.dto.UserRegistrationDto;
import com.tfg.idprovider.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    public static final int KEY_LENGTH = 512;

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private MongoUserDetailsService mongoUserDetailsService;

    private GenerateKeys generateKeys;
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;


    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, MongoUserDetailsService mongoUserDetailsService) throws NoSuchAlgorithmException {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mongoUserDetailsService = mongoUserDetailsService;
        this.generateKeys = new GenerateKeys(KEY_LENGTH);
        generateKeys.createKeys();
        this.publicKey = (RSAPublicKey) generateKeys.getPublicKey();
        this.privateKey = (RSAPrivateKey) generateKeys.getPrivateKey();
    }

    public MyUser registerNewUserAccount(UserRegistrationDto accountDto) throws UserAlreadyExistException {
        if (emailExists(accountDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email adress: " + accountDto.getEmail());
        }else if (usernameExists(accountDto.getUsername())) {
            throw new UserAlreadyExistException("There is an account with that username: " + accountDto.getUsername());
        }

        final MyUser user = MyUserBuilder.builder()
                .withUsername(accountDto.getUsername())
                .withPassword(passwordEncoder.encode(accountDto.getPassword()))         //password encriptat amb BCrypt
                .withEmail(accountDto.getEmail())
                .withFirstName(accountDto.getFirstName())
                .withLastName(accountDto.getLastName())
                .withAuthorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER") ))   //Definir varis ROLES
                .build();

        return userRepository.save(user);
    }

    public ResponseEntity logIn(UserLogInDto user) {
        try {
            UserDetails userDetails = mongoUserDetailsService.loadUserByUsername(user.getUsername());
            Algorithm algorithm = getAlgorithm();
            if (userDetails.getPassword().equals(user.getPassword())) {
                Map<String, Object> headers = getHeaderClaims();
                String token = JSONWebToken.createToken(dateExpires(), algorithm, headers);
                return ResponseEntity.ok().body(token);
            }
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            //Si falla la creacio de les claus publica privada retornem un HttpStatus 500
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
        }catch (JWTCreationException e) {
            e.printStackTrace();
            //Si falla la creacio del JWT retornem un HttpStatus 503
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
        //Si el username no coincideix amb el password retornem un HttpStatus 401
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }

    private boolean usernameExists(final String username) {
        return userRepository.findByEmail(username) != null;
    }

    private Date dateExpires() {
        return Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
    }

    private Algorithm getAlgorithm() {
        return Algorithm.RSA512(publicKey, privateKey);
    }

    private Map<String, Object> getHeaderClaims() {
        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("alg", "RSA512");
        headerClaims.put("typ", "JWT");
        return headerClaims;
    }
}
