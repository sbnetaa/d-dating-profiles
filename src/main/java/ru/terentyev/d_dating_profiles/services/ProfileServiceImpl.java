package ru.terentyev.d_dating_profiles.services;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.terentyev.d_dating_profiles.entities.Profile;
import ru.terentyev.d_dating_profiles.repositories.ProfileRepository;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final QueryService queryService;

    public ProfileServiceImpl(ProfileRepository profileRepository, ReactiveMongoTemplate reactiveMongoTemplate
            , QueryService queryService) {
        this.profileRepository = profileRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.queryService = queryService;
    }

    @Override
    public Flux<Profile> takeNextDeck(Profile requester) {
        Query query = new Query();
        query = queryService.addGenderCriteria(query, requester);
        query = queryService.addAgeCriteria(query, requester);
        query = queryService.addPurposeCriteria(query, requester);
        return null;
    }

}
