package ru.terentyev.d_dating_profiles.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.terentyev.d_dating_profiles.proto.*;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "profiles")
public class Profile extends AbstractEntity{

    private String name;
    private int age;
    private String description;
    private boolean male;
    private Set<Hobby> hobbies;
    private Set<Photo> photos;
    private Set<Match> matches;

//    public enum Hobby {
//        TENNIS("Теннис"),
//        BILLIARDS("Бильярд"),
//        IT("Информационные технологии"),
//        MUSIC("Музыка"),
//        TRAVELING("Путешествия");
//
//        private Hobby(String description) {
//        }
//    }
}
