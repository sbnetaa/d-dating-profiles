package ru.terentyev.d_dating_profiles;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.RandomStringUtils;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
public class DataLoader {

    private final int PROFILES_TO_GENERATE = 100;
    private final ProfileRepository profileRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final Random random;
    private Set<Hobby> hobbies = new HashSet<>();

    public DataLoader(ProfileRepository profileRepository, ReactiveMongoTemplate reactiveMongoTemplate
            , Random random) {
        this.profileRepository = profileRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.random = random;
    }


    @Async
    @PostConstruct
    public void populateDatabase() {
        populateHobbies();
        saveTestProfiles();
    }

    public void saveTestProfiles(){
        Mono<Long> profilesCountMono = reactiveMongoTemplate.count(new Query(), Profile.class);
        profilesCountMono.subscribe((count) -> {
            if (count >= PROFILES_TO_GENERATE) {
                return;
            }
            saveFirstProfile();
            generateProfiles(PROFILES_TO_GENERATE - count.intValue() - 1).forEach(reactiveMongoTemplate::save);
        });
}


    public void saveFirstProfile() {
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

    private List<Profile> generateProfiles(int count){
        if (count <= 0) {
            return Collections.emptyList();
        }
        List<Profile> profiles = new ArrayList<>();
        for (int i = PROFILES_TO_GENERATE - count; i <= PROFILES_TO_GENERATE; i++) {
            Profile profile = new Profile();
            profile.setName("Пользователь" + count);
            profile.setAge(random.nextInt(18, 100));
            profile.setMale(random.nextBoolean());
            Profile.Purpose[] purposes = Profile.Purpose.values();
            int randomIndex = random.nextInt(0, Profile.Purpose.values().length);
            profile.setPurpose(purposes[randomIndex]);
            profile.setEmail("test" + count + "@mail.com");
            Hobby[] hobbiesArray = hobbies.toArray(new Hobby[0]);
            profile.setHobbies(Set.of( // 5 рандомных хобби
                    hobbiesArray[random.nextInt(0, hobbies.size())],
                    hobbiesArray[random.nextInt(0, hobbies.size())],
                    hobbiesArray[random.nextInt(0, hobbies.size())],
                    hobbiesArray[random.nextInt(0, hobbies.size())],
                    hobbiesArray[random.nextInt(0, hobbies.size())]
            ));
            profile.setAboutMe(RandomStringUtils.insecure().next(20));

        }
        return profiles;
    }


    private Profile getProfile() {
        Profile firstUser = new Profile();
        firstUser.setName("Дмитрий");
        firstUser.setAge(30);
        firstUser.setEmail("sbnet@bk.ru");
        firstUser.setMale(true);
        firstUser.setAboutMe("Люблю свою работу и рок.");
        firstUser.setPurpose(Profile.Purpose.LOVE);
        Profile.Settings settings = new Profile.Settings();
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
        this.hobbies = hobbies;
    }
}
