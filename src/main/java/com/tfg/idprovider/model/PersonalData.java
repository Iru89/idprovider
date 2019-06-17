package com.tfg.idprovider.model;

public class PersonalData {

    private final String firstName;
    private final String lastName;

    private PersonalData(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public static class PersonalDataBuilder{
        private String firstName;
        private String lastName;

        private PersonalDataBuilder() {
        }

        public static PersonalDataBuilder builder(){
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

        public PersonalData build(){
            return new PersonalData(firstName, lastName);
        }
    }
}
