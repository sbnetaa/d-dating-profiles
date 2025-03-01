package ru.terentyev.d_dating_profiles;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.terentyev.d_dating_profiles.entities.Hobby;
import ru.terentyev.d_dating_profiles.proto.Profile;
import ru.terentyev.d_dating_profiles.proto.Purpose;
import ru.terentyev.d_dating_profiles.repositories.ProfileRepository;

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



    @PostConstruct
    public void populateDatabase(){
        saveExampleUser();
        populateHobbies();
    }


    public void saveExampleUser(){
        Mono<Long> profilesCountMono = reactiveMongoTemplate.count(new Query(), Profile.class);
        profilesCountMono.subscribe((count) -> {
            if (count < 1) {
                Profile profile = Profile.newBuilder()
                        .setAge(30)
                        .setName("Дмитрий")
                        .setEmail("sbnet@bk.ru")
                        .setDescription("Люблю Java и курить :0")
                        .setMale(true)
                        .setPurpose(Purpose.LOVE)
                        .build();
                reactiveMongoTemplate.save(profile);
            }
        });
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
