package ru.mirea.pkmn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mirea.pkmn.belousovdo.utils.ResourceFileLoader;

import java.util.logging.Logger;

@Configuration
public class AppConfig {

    @Bean
    public Logger logger() {

        return Logger.getLogger(PkmnApplication.class.getName());
    }

    @Bean
    public ResourceFileLoader loader(Logger logger) {

        return new ResourceFileLoader(logger);
    }
}
