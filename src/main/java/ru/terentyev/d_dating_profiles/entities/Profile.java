package ru.terentyev.d_dating_profiles.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

@Document
@Getter
@Setter
public class Profile extends AbstractEntity {

    private String name;
    private Integer age;
    private String description;
    private String email;
    private Boolean male;
    private Purpose purpose;
//    private List<Photo> photos = new ArrayList<>();
    private Set<Hobby> hobbies = new LinkedHashSet<>();
//    private List<Match> matches = new ArrayList<>();
    private ProfileSettings settings;

    @AllArgsConstructor
    @Getter
    public enum Purpose {
        FRIENDSHIP("Дружба"),
        LOVE("Любовь"),
        FREE_LOVE("Интимные отношения"),
        EVERYTHING("Всё сразу"),
        DONT_KNOW("Не знаю");

        private final String description;
    }

    @Document
    @Getter
    @Setter
    public static class ProfileSettings {
        private boolean showBothGenders;
        private boolean showMale;
        private Integer desiredAgeMin;
        private Integer desiredAgeMax;
    }
}
