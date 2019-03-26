package com.tfg.idprovider.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.bson.types.ObjectId;

import java.security.KeyPair;


@JsonDeserialize(builder = MyUser.MyUserBuilder.class)
public class MyUser {

    private final ObjectId _id;
    private final ObjectId userDetailsId;

    private final MyUserDetails myUserDetails;
    private final KeyPair keyPair;
    private final PersonalData personalData;

    public MyUser(ObjectId _id, ObjectId userDetailsId, MyUserDetails myUserDetails, KeyPair keyPair, PersonalData personalData) {
        this._id = _id;
        this.userDetailsId = userDetailsId;
        this.myUserDetails = myUserDetails;
        this.keyPair = keyPair;
        this.personalData = personalData;
    }

    public ObjectId get_id() {
        return _id;
    }

    public ObjectId getUserDetailsId() {
        return userDetailsId;
    }

    public MyUserDetails getMyUserDetails() {
        return myUserDetails;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    @JsonPOJOBuilder
    public static class MyUserBuilder{

        private ObjectId _id;
        private ObjectId userDetailsId;
        private MyUserDetails myUserDetails;
        private KeyPair keyPair;
        private PersonalData personalData;

        private MyUserBuilder() {
        }

        public static MyUserBuilder builder(){
            return new MyUserBuilder();
        }

        public MyUserBuilder with_id(ObjectId _id) {
            this._id = _id;
            return this;
        }

        public MyUserBuilder withUserDetailsId(ObjectId userDetailsId) {
            this.userDetailsId = userDetailsId;
            return this;
        }

        public MyUserBuilder withMyUserAccess(MyUserDetails myUserDetails) {
            this.myUserDetails = myUserDetails;
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
            return new MyUser(_id, userDetailsId, myUserDetails, keyPair, personalData);
        }
    }
}

