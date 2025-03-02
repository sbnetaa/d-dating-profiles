package ru.terentyev.d_dating_profiles.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
public class Hobby {

    private Integer id;
    @Indexed(unique = true)
    private String name;
    @Indexed(unique = true)
    private String description;
}
