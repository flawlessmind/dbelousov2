package ru.mirea.pkmn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mirea.pkmn.belousovdo.entity.StudentEntity;

import java.io.Serializable;

/**
 * Stores the student's data.
 * Classwork 3 (UML diagrams).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student implements Serializable {

    private String firstName;

    private String surName;

    private String familyName;

    private String group;

    public StudentEntity toEntity() {

        // Map fields to the entity
        return new StudentEntity(
                null,    // id
                this.firstName, // firstName
                this.surName,   // patronicName (mapped from surName)
                this.familyName,// familyName
                this.group      // group
        );
    }
}
