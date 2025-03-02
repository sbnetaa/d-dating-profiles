package ru.terentyev.d_dating_profiles.services;

import reactor.core.publisher.Flux;
import ru.terentyev.d_dating_profiles.entities.Profile;

public interface ProfileService {
    Flux<Profile> takeNextDeck(Profile requester);
}
