package com.tfg.idprovider.repository;

import com.tfg.idprovider.model.MyUserDetails;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MyUserDetailsRepository extends MongoRepository<MyUserDetails, String> {
    MyUserDetails findBy_id(ObjectId _id);
    MyUserDetails findByUsername(String username);
    MyUserDetails findByEmail(String email);
    MyUserDetails save(MyUserDetails myUserDetails);
    List<MyUserDetails> deleteBy_id(ObjectId _id);
}
