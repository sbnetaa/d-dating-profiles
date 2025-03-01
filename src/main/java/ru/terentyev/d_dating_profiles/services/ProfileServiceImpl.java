package ru.terentyev.d_dating_profiles.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.terentyev.d_dating_profiles.proto.Profile;
import ru.terentyev.d_dating_profiles.repositories.ProfileRepository;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.profileRepository = profileRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Flux<Profile> takeNextDeck(Profile requester, boolean showBothGender, boolean showMaleOnly) {
        Query query = new Query();
//        ProfileSettings requesterSetting =
        if (!showBothGender) query.addCriteria(Criteria.where("showMaleOnly").is(showMaleOnly));
//        query.addCriteria(Criteria.where("age").gte());
        return null;
    }
}
