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
    private final AuthService authService;

    public ProfileServiceImpl(ProfileRepository profileRepository, ReactiveMongoTemplate reactiveMongoTemplate
            , QueryService queryService, AuthService authService) {
        this.profileRepository = profileRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.queryService = queryService;
        this.authService = authService;
    }

    @Override
    public Flux<Profile> takeNextDeck() {
        Query query = new Query();
        Profile requester = authService.getCurrentUser();
        query = queryService.addGenderCriteria(query, requester);
        query = queryService.addAgeCriteria(query, requester);
        query = queryService.addPurposeCriteria(query, requester);
        return reactiveMongoTemplate.find(query, Profile.class);
    }
}
