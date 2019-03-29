package com.tfg.idprovider.repository;

import com.tfg.idprovider.model.MyUser;
import com.tfg.idprovider.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    MyUser findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findById(ObjectId id);
    //boolean existsByUsernameOrEmail(String usernameOrEmail);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}