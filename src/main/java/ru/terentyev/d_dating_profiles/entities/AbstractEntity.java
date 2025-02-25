package ru.terentyev.d_dating_profiles.entities;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
@Setter
public class AbstractEntity {

    @Id
    private UUID id;
}
