package ru.mirea.pkmn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mirea.pkmn.polupanpolina.entity.CardEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Class indicates the given pokemon card.
 * Classwork 3 (UML diagrams).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card implements Serializable {

    private PokemonStage pokemonStage;

    private String number;

    private String name;

    private int hp;

    private EnergyType pokemonType;

    private Card evolvesFrom;

    private List<AttackSkill> skills;

    private EnergyType weaknessType;

    private EnergyType resistanceType;

    private String retreatCost;

    private String gameSet;

    private char regulationMark;

    private Student pokemonOwner;


    public CardEntity toEntity() {

        // Create and return the entity object
        return new CardEntity(
                null,
                name,
                (short) hp,
                evolvesFrom != null ? evolvesFrom.toEntity() : null,
                gameSet,
                pokemonOwner != null ? pokemonOwner.toEntity() : null,
                pokemonStage.name(),
                retreatCost,
                weaknessType.name(),
                resistanceType.name(),
                skills,
                pokemonType.name(),
                regulationMark,
                number
        );
    }
}
