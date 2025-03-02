package ru.terentyev.d_dating_profiles.services;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import ru.terentyev.d_dating_profiles.entities.Profile;

@Service
public class QueryServiceImpl implements QueryService {

    @Override
    public void updateNextDeckQuery(Profile requester) {
        Query query = new Query();
        query = addGenderCriteria(query, requester);
        query = addAgeCriteria(query, requester);
        query = addPurposeCriteria(query, requester);
        requester.getSettings().setNextDeckQuery(query);
    }

    @Override
    public Query addGenderCriteria(Query query, Profile requester) {
        Profile.Settings settings = requester.getSettings();
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
        return query;
    }

    @Override
    public Query addAgeCriteria(Query query, Profile requester) {
        Profile.Settings settings = requester.getSettings();

        query.addCriteria(new Criteria().orOperator(
                Criteria.where("desiredAgeMin").isNull(),
                Criteria.where("desiredAgeMin").lte(requester.getAge())));

        query.addCriteria(new Criteria().orOperator(
                Criteria.where("desiredAgeMax").isNull(),
                Criteria.where("desiredAgeMax").gte(requester.getAge())));

        if (settings.getDesiredAgeMin() != null)
            query.addCriteria(Criteria.where("age").gte(settings.getDesiredAgeMin()));
        if (settings.getDesiredAgeMax() != null)
            query.addCriteria(Criteria.where("age").lte(settings.getDesiredAgeMax()));

        return query;
    }

    @Override
    public Query addPurposeCriteria(Query query, Profile requester) {
        Profile.Settings settings = requester.getSettings();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("showWithMatchingPurposeOnly").is(true)
                , Criteria.where("purpose").in(Profile.Purpose.EVERYTHING, requester.getPurpose())
        ));

        if (settings.isShowWithMatchingPurposeOnly())
            query.addCriteria(Criteria.where("purpose").in(requester.getPurpose(), Profile.Purpose.EVERYTHING));

        return query;
    }
}
