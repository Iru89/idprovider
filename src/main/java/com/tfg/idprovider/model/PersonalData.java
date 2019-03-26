package com.tfg.idprovider.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = PersonalData.PersonalDataBuilder.class)
public class PersonalData {

    private final String firstName;
    private final String lastName;
    private final String email;

    private PersonalData(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @JsonPOJOBuilder
    public class PersonalDataBuilder{
        private String firstName;
        private String lastName;
        private String email;

        private PersonalDataBuilder() {
        }

        public PersonalDataBuilder builder(){
            return new PersonalDataBuilder();
        }

        public PersonalDataBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public PersonalDataBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public PersonalDataBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public PersonalData build(){
            return new PersonalData(firstName, lastName, email);
        }
    }
}
