package com.tfg.idprovider.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.bson.types.ObjectId;

@JsonDeserialize(builder = UserIdDto.UserIdDtoBuilder.class)
public class UserIdDto {

    private final ObjectId id;

    private UserIdDto(ObjectId id) {
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }

    @JsonPOJOBuilder
    public static class UserIdDtoBuilder{
        private ObjectId id;

        private UserIdDtoBuilder() {
        }

        public UserIdDtoBuilder builder(){
            return new UserIdDtoBuilder();
        }

        public UserIdDtoBuilder withId(ObjectId id) {
            this.id = id;
            return this;
        }

        public UserIdDto build(){
            return new UserIdDto(id);
        }
    }
}
