package ru.terentyev.d_dating_profiles.services;

import reactor.core.publisher.Flux;
import ru.terentyev.d_dating_profiles.proto.Profile;

import java.util.UUID;

public interface ProfileService {
    Flux<Profile> takeNextDeck(Profile requester, boolean showBothGender, boolean showMaleOnly);
}
