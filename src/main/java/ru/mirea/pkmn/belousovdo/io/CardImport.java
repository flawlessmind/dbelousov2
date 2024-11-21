package ru.mirea.pkmn.belousovdo.io;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.common.io.Resources;
import ru.mirea.pkmn.*;

/**
 * Utility helper class creates Card instances from a file
 * Classwork 3 (Task 1).
 */
public final class CardImport {

    private static final Logger logger =  Logger.getLogger(PkmnApplication.class.getName());

    /**
     * Deserializes Card objects from byte format
     * @param path String path to the file of .crd extension
     * @return object of class Card
     */
    public static Card deserializeCard(String path) {

        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Card card = (Card) objectInputStream.readObject();
            logger.log(Level.INFO, String.format("Card is successfully deserialized from the file: %s", card));
            return card;
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("%s: Card deserialization failed", e.getMessage()));
            return null;
        }
    }

    /**
     * Parses Card objects from .txt file
     * @param path String path to the .txt file
     * @return object of class Card
     */
    public static Card parseCard(String path) {

        logger.log(Level.INFO, String.format("Parsing from file path: %s", path));

        // Creating an instance of InputStream
        try (InputStream is = new FileInputStream(path)) {

            // Creating an instance of Scanner
            try (Scanner sc = new Scanner(is, StandardCharsets.UTF_8.name())) {

                //Parse pokemon stage
                PokemonStage pokemonStage = parsePokemonStage(sc.nextLine(), PokemonStage.BASIC);
                // Parse pokemon name
                String name = parseString(sc.nextLine(), "defaultName");
                String number = parseString(sc.nextLine(), "0");
                //Parse hp
                int hp = parseInt(sc.nextLine(), 0);
                // Parse pokemon type
                EnergyType pokemonType = parseEnergyType(sc.nextLine(), EnergyType.COLORLESS);
                // Parse parent pokemon
                String ppath =  parseString(sc.nextLine(), " ");
                Card evolvesFrom;

                try {

                    URL resource =  Resources.getResource(ppath);
                    Path path1 = Paths.get(resource.toURI());
                    evolvesFrom = parseCard(path1.toString());

                } catch (IllegalArgumentException e) { // getResource throws IllegalArgumentException
                    evolvesFrom = null;
                }

                // Parse attack skills
                List<AttackSkill> skills = parseAttackSkills(sc.nextLine());
                // Parse weakness type
                EnergyType weaknessType = parseEnergyType(sc.nextLine(), EnergyType.COLORLESS);
                // Parse resistance type
                EnergyType resistanceType = parseEnergyType(sc.nextLine(), EnergyType.COLORLESS);
                // Parse retreat cost
                String retreatCost = parseString(sc.nextLine(), "0");
                // Parse game set
                String gameSet = parseString(sc.nextLine(), "0");
                // Parse regulation mark
                char regulationMark = parseChar(sc.nextLine(), '0');
                // Parse owner
                Student pokemonOwner = parseStudent(sc.nextLine());

                Card card =  new Card(
                        pokemonStage,
                        number,
                        name,
                        hp,
                        pokemonType,
                        evolvesFrom,
                        skills,
                        weaknessType,
                        resistanceType,
                        retreatCost,
                        gameSet,
                        regulationMark,
                        pokemonOwner
                );

               logger.log(Level.INFO, String.format("Instance of Card is created from file: \n %s", card));

                return card;
            }
        } catch (Exception e) {
            // File is not found exception handling
            logger.log(Level.SEVERE, String.format("%s: Parsing card failed", e.getMessage()));
            return null;
        }
    }

    /**
     * Helper function to parse attack skills from the string of format:
     * {cost}/{name}/{damage}, ...
     */
    private static List<AttackSkill> parseAttackSkills(String string) {

        // "{cost}/{name}/{damage}, {cost1}/{name1}/{damage1}, ..." -> "{cost}/{name}/{damage}", "{cost1}/{name1}/{damage1}", ...
        List<String> strings = List.of(string.split(","));

        List<AttackSkill> attackSkills = new ArrayList<>(List.of());

        for (String s : strings) {

            // {cost}/{name}/{damage} -> {cost}, {name}, {damage}
            List<String> params = List.of(s.split("/"));

            String name = "defaultName";
            String description = "defaultDescription";
            String cost = "0";
            int damage = 0;

            try {

                name = parseString(params.get(1).strip(), "defaultName");
                description = parseString(params.get(1).strip(),"defaultDescription" );
                cost = parseString(params.get(0).strip(), "0");
                damage = parseInt(params.get(2).strip(), 0);

            } catch (IndexOutOfBoundsException e) {
                logger.log(Level.WARNING, e.getMessage());
            }

            AttackSkill skill = new AttackSkill(name, description, cost, damage);

            attackSkills.add(skill);
        }

        return attackSkills;
    }

    /**
     * Helper function to parse student from the string of format:
     * {lastName}/{firstName}/{surName}/{group}
     */
    static Student parseStudent(String string) {

        List<String> params = List.of(string.split("/"));

        if (params.size() < 4) return null;

        String firstName = null;
        String surName = null;
        String familyName = null;
        String group = null;

        try {

            firstName = parseString(params.get(1).strip(), null);
            surName = parseString(params.get(2).strip(), null);
            familyName = parseString(params.get(0).strip(), null);
            group = parseString(params.get(3).strip(), null);

        } catch (IndexOutOfBoundsException e) {
            logger.log(Level.WARNING, e.getMessage());
        }

        return new Student(firstName, surName, familyName, group);
    }

    // Method to safely parse PokemonStage with default value
    static PokemonStage parsePokemonStage(String line, PokemonStage defaultStage) {
        try {
            return PokemonStage.valueOf(line.strip());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to parse PokemonStage: " + e.getMessage() + ". Using default: " + defaultStage);
            return defaultStage;
        }
    }

    // Method to safely parse String with default value
    private static String parseString(String line, String defaultValue) {
        if (line == null || line.isEmpty()) {
            logger.log(Level.WARNING, "Empty string found, using default: " + defaultValue);
            return defaultValue;
        }
        return line.strip();
    }

    // Method to safely parse int with default value
    static int parseInt(String line, int defaultValue) {
        try {
            return Integer.parseInt(line.strip());
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING,"Failed to parse int: " + e.getMessage() + ". Using default: " + defaultValue);
            return defaultValue;
        }
    }

    // Method to safely parse EnergyType with default value
    static EnergyType parseEnergyType(String line, EnergyType defaultType) {
        try {
            return EnergyType.valueOf(line.strip());
        } catch (Exception e) {
            logger.log(Level.WARNING,"Failed to parse EnergyType: " + e.getMessage() + ". Using default: " + defaultType);
            return defaultType;
        }
    }

    // Method to safely parse a single char with default value
    static char parseChar(String line, char defaultValue) {
        if (line != null && !line.isEmpty()) {
            return line.strip().charAt(0);
        }
        System.err.println("Failed to parse char, using default: " + defaultValue);
        return defaultValue;
    }
}
