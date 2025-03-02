package ru.terentyev.d_dating_profiles.services;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import ru.terentyev.d_dating_profiles.entities.Profile;

@Service
public class AuthService {

    private final ReactiveMongoTemplate reactiveMongoTemplate;


    public AuthService(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    public Profile getCurrentUser(){
        return reactiveMongoTemplate.findOne(Query.query(Criteria.where("name")
                .is("Дмитрий")), Profile.class).block();
    }
}
