package ru.terentyev.d_dating_profiles.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.terentyev.d_dating_profiles.proto.Profile;
import ru.terentyev.d_dating_profiles.services.ProfileService;

import java.util.UUID;

@RequestMapping("/api/v1")
@RestController
public class ProfileController extends AbstractController {

    private ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/deck")
    public Flux<Profile> getNextDeck(Profile requester, boolean showBothGenders, boolean showMaleOnly) {
        return profileService.takeNextDeck(requester, showBothGenders, showMaleOnly);
    }
}
