package ru.mirea.pkmn.belousovdo.repository;

import ru.mirea.pkmn.belousovdo.entity.CardEntity;
import ru.mirea.pkmn.belousovdo.entity.StudentEntity;

import java.util.UUID;

public interface PkmnRepository {

    CardEntity getCard(String name);

    CardEntity getCard(UUID uuid);

    StudentEntity getStudent(String fullName);

    StudentEntity getStudent(UUID uuid);

    void saveCard(CardEntity card);

    void saveStudent(StudentEntity student);
}
