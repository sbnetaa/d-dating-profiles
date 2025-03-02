package ru.terentyev.d_dating_profiles.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.terentyev.d_dating_profiles.entities.Profile;
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
    public Flux<Profile> takeNextDeck(Profile requester) {
        Query query = createNextDeckQuery(requester);
        return null;
    }

    private Query createNextDeckQuery(Profile requester) {
        Query query = new Query();
        Profile.ProfileSettings settings = requester.getSettings();
        Criteria showBothGendersCriteria = Criteria.where("showBothGenders").is(true);
        Criteria showRequestedDesiredGenderCriteria = Criteria.where("showMale").is(requester.getMale());
        if (settings.isShowBothGenders()) {
            query.addCriteria(new Criteria().orOperator(showBothGendersCriteria
                    , showRequestedDesiredGenderCriteria));
        } else {
            query.addCriteria(new Criteria().andOperator(
                    Criteria.where("male").is(settings.isShowMale())
                    , new Criteria().orOperator(
                            showBothGendersCriteria
                            , showRequestedDesiredGenderCriteria
                            )
            ));
        }

        if (settings.getDesiredAgeMin() != null)
            query.addCriteria(Criteria.where("age").gte(settings.getDesiredAgeMin()));
        if (settings.getDesiredAgeMax() != null)
            query.addCriteria(Criteria.where("age").lte(settings.getDesiredAgeMax()));
        if (settings.isShowWithMatchingPurposeOnly())
            query.addCriteria(Criteria.where("purpose").in(requester.getPurpose(), Profile.Purpose.EVERYTHING));
        return query;
    }
}
