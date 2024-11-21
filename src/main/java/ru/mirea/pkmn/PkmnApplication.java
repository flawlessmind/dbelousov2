package ru.mirea.pkmn;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.mirea.pkmn.belousovdo.entity.CardEntity;
import ru.mirea.pkmn.belousovdo.entity.StudentEntity;
import ru.mirea.pkmn.belousovdo.io.CardExport;
import ru.mirea.pkmn.belousovdo.io.CardImport;
import ru.mirea.pkmn.belousovdo.network.PkmnHttpClient;
import ru.mirea.pkmn.belousovdo.repository.PkmnRepository;
import ru.mirea.pkmn.belousovdo.utils.ResourceFileLoader;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


@SpringBootApplication
public class PkmnApplication {

    static ApplicationContext context;

    public static void main(String[] args) {

        context = SpringApplication.run(PkmnApplication.class, args);

        PkmnHttpClient pkmnHttpClient = context.getBean(PkmnHttpClient.class);

        PkmnRepository dbService = context.getBean(PkmnRepository.class);

        testNetwork(pkmnHttpClient);

        testDatabase(dbService);
    }

    public static void testNetwork(PkmnHttpClient client) {

        Logger logger = context.getBean(Logger.class); // Create logger

        ResourceFileLoader loader = context.getBean(ResourceFileLoader.class);

        logger.setLevel(Level.FINE);

        Card cardFile = CardImport.parseCard(loader.getResourcePath("my_card.txt"));

        PkmnHttpClient.PokemonCardCallback callback = new PkmnHttpClient.PokemonCardCallback() {
            @Override
            public void onSuccess(JsonNode cardData) {

                logger.log(Level.INFO,"Card data: " + cardData); // Log data

                List<JsonNode> attacks = cardData.findValues("attacks"); // Find attacks section

                int ind = 0; // Index is needed to iterate over attack skills

                for (final JsonNode objNode : attacks) {
                    JsonNode text = objNode.findValue("text");

                    logger.log(Level.INFO, "Attack description: " + text.toString().replace('"', ' ').strip() );

                    assert cardFile != null;
                 //   cardFile.setSkillDescription(ind, text.toString());
                    ind++;
                }

                String EXPORT_PATH = "export.crd";

                CardExport.serializeCard(cardFile, EXPORT_PATH);

                CardImport.deserializeCard(loader.getResourcePath(EXPORT_PATH));

            }

            @Override
            public void onError(Throwable error) {
                logger.log(Level.SEVERE,"Failed to fetch card: " + error.getMessage());
            }
        };

        client.getPokemonCard("Mewtwo", "130", callback);
    }

    public static void testDatabase(PkmnRepository service) {

        Logger logger = context.getBean(Logger.class); // Create logger

        ResourceFileLoader loader = context.getBean(ResourceFileLoader.class);

        Card card = CardImport.parseCard(loader.getResourcePath("my_card_test.txt"));

        service.saveCard(card.toEntity());

        StudentEntity student = service.getStudent("Belousov Daniil Olegovich");

        logger.log(Level.INFO, student.toString());

        UUID uuid = UUID.fromString("05be7b93-17fa-3459-a0f4-c62f7628796d");
        CardEntity dCardEntity = service.getCard(uuid);

        logger.log(Level.INFO, dCardEntity.toString());
    }
}
