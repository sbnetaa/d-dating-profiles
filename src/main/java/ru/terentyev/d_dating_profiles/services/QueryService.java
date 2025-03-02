package ru.terentyev.d_dating_profiles.services;

import org.springframework.data.mongodb.core.query.Query;
import ru.terentyev.d_dating_profiles.entities.Profile;

public interface QueryService {
    void updateNextDeckQuery(Profile requester);
    Query addGenderCriteria(Query query, Profile requester);
    Query addAgeCriteria(Query query, Profile requester);
    Query addPurposeCriteria(Query query, Profile requester);
}
