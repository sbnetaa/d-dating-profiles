package ru.terentyev.d_dating_profiles;

import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.terentyev.d_dating_profiles.entities.Hobby;
import ru.terentyev.d_dating_profiles.entities.Profile;
import ru.terentyev.d_dating_profiles.repositories.ProfileRepository;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class DataLoader {

    private final ProfileRepository profileRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    public DataLoader(ProfileRepository profileRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.profileRepository = profileRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }


    @Async
    @PostConstruct
    public void populateDatabase(){
        populateHobbies();
        saveExampleUser();
    }

    public void saveExampleUser() {
        Mono<Profile> profilesCountMono = reactiveMongoTemplate.findOne(new Query(), Profile.class);
        profilesCountMono.subscribe((count) -> profilesCountMono.hasElement().subscribe(response -> {
            if (!response) {
                return;
            }
            Mono<Profile> createProfileMono = Mono.fromCallable(this::getProfile);
            Flux<Hobby> hobbiesFlux = reactiveMongoTemplate.find(
                    Query.query(Criteria.where("name").in((Object) new String[]{"IT", "Теннис"})), Hobby.class);
            Mono.zip(createProfileMono, hobbiesFlux.collectList()).subscribe(tuple -> {
                tuple.getT1().setHobbies(new HashSet<>(tuple.getT2()));
                reactiveMongoTemplate.save(tuple.getT1());
            });
        }));
    }


    @NotNull
    private Profile getProfile() {
        Profile firstUser = new Profile();
        firstUser.setName("Дмитрий");
        firstUser.setAge(30);
        firstUser.setEmail("sbnet@bk.ru");
        firstUser.setMale(true);
        firstUser.setAboutMe("Люблю свою работу и рок.");
        firstUser.setPurpose(Profile.Purpose.LOVE);
        Profile.ProfileSettings settings = new Profile.ProfileSettings();
        settings.setDesiredAgeMax(50);
        settings.setDesiredAgeMin(20);
        settings.setShowBothGenders(false);
        settings.setShowMale(false);
        firstUser.setSettings(settings);
        return firstUser;
    }


    public void populateHobbies(){
        Set<Hobby> hobbies = new LinkedHashSet<>();
        hobbies.add(new Hobby(1, "Теннис", "Большой теннис"));
        hobbies.add(new Hobby(2, "Бильярд", "Бильярд"));
        hobbies.add(new Hobby(3, "IT", "Информационные технологии"));
        hobbies.add(new Hobby(4, "Музыка", "Музыка"));
        hobbies.add(new Hobby(5, "Путешествия", "Большой теннис"));
        hobbies.forEach(reactiveMongoTemplate::save);
    }
}
