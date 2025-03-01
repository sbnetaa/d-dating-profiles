package ru.terentyev.d_dating_profiles.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileRepository {

    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    public ProfileRepository(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }
}
