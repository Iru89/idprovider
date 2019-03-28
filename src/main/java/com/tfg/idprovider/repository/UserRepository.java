package com.tfg.idprovider.repository;

import com.tfg.idprovider.model.MyUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<MyUser, String> {

    MyUser findByUsername(String username);
}