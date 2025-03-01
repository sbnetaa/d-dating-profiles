package ru.terentyev.d_dating_profiles.repositories;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.terentyev.d_dating_profiles.proto.Profile;

import java.util.UUID;

@Repository
public interface IProfileRepository extends ReactiveMongoRepository<Profile, UUID> {

    @Query("{ 'age': { $gte: ?0 }, 'city': ?1 }") // TODO radius
    Flux<Profile> findByAgeGreaterThanEqualAndCity(Profile requester);
}
