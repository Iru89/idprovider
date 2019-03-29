package com.tfg.idprovider.repository;

import com.tfg.idprovider.model.MyUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<MyUser, String> {

    MyUser findByUsername(String username);
    Optional<MyUser> findByUsernameOrEmail(String username, String email);
    Optional<MyUser> findById(ObjectId id);
    //boolean existsByUsernameOrEmail(String usernameOrEmail);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}