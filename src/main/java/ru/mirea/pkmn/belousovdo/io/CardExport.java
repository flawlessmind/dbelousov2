package ru.mirea.pkmn.belousovdo.io;

import ru.mirea.pkmn.Card;
import ru.mirea.pkmn.PkmnApplication;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility helper class exports Card instances to .crd files
 * Classwork 3 (Task 1).
 */
public class CardExport {

    private static final Logger logger =  Logger.getLogger(PkmnApplication.class.getName());

    /**
     * Serializes Card objects into byte format
     * @param card Card object to be serialized
     * @param fileName String path to the file of .crd extension
     */
    public static void serializeCard(Card card, String fileName) {
        try {
            File outputFile = new File("output/" + fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(card);
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("%s: Card serializing failed", e.getMessage()));
            return;
        }
        logger.log(Level.INFO, String.format("Object is successfully serialized into file %s", "output/" + fileName));
    }
}
