package com.tfg.idprovider.repository;

import com.tfg.idprovider.model.MyUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<MyUser, String> {

    MyUser findBy_id(ObjectId _id);
    MyUser findByUsername(String username);
    MyUser findByEmail(String email);
    MyUser save(MyUser user);
    List<MyUser> deleteBy_id(ObjectId _id);
    List<MyUser> findAll();
}