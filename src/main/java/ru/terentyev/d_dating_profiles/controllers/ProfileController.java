package ru.terentyev.d_dating_profiles.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.terentyev.d_dating_profiles.entities.Profile;
import ru.terentyev.d_dating_profiles.services.ProfileService;

@RequestMapping("/api/v1")
@RestController
public class ProfileController extends AbstractController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/nextDeck")
    public Flux<Profile> getNextDeck() {
        return profileService.takeNextDeck();
    }
}
