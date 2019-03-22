package com.tfg.idprovider.config;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.tfg.idprovider.repository")
public class SimpleMongoConfiguration {


    @Bean
    public MongoClient mongo() throws Exception{
        return new MongoClient();
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception{
        return new MongoTemplate(mongo(), "test");
    }
}