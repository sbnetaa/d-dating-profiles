package ru.terentyev.d_dating_profiles.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public abstract class AbstractEntity {
    @Id
    protected UUID id;
}
