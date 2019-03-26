package com.tfg.idprovider.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.bson.types.ObjectId;

import java.security.KeyPair;


@JsonDeserialize(builder = MyUser.MyUserBuilder.class)
public class MyUser {

    private final ObjectId _id;

    private final MyUserAccess myUserAccess;
    private final KeyPair keyPair;
    private final PersonalData personalData;

    public MyUser(ObjectId _id, MyUserAccess myUserAccess, KeyPair keyPair, PersonalData personalData) {
        this._id = _id;
        this.myUserAccess = myUserAccess;
        this.keyPair = keyPair;
        this.personalData = personalData;
    }

    public ObjectId get_id() {
        return _id;
    }

    public MyUserAccess getMyUserAccess() {
        return myUserAccess;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    @JsonPOJOBuilder
    public class MyUserBuilder{

        private ObjectId _id;
        private MyUserAccess myUserAccess;
        private KeyPair keyPair;
        private PersonalData personalData;

        private MyUserBuilder() {
        }

        public MyUserBuilder builder(){
            return new MyUserBuilder();
        }

        public MyUserBuilder with_id(ObjectId _id) {
            this._id = _id;
            return this;
        }

        public MyUserBuilder withMyUserAccess(MyUserAccess myUserAccess) {
            this.myUserAccess = myUserAccess;
            return this;
        }

        public MyUserBuilder withKeyPair(KeyPair keyPair) {
            this.keyPair = keyPair;
            return this;
        }

        public MyUserBuilder withPersonalData(PersonalData personalData) {
            this.personalData = personalData;
            return this;
        }

        public MyUser build(){
            return new MyUser(_id, myUserAccess, keyPair, personalData);
        }
    }
}

