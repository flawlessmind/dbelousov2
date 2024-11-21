package ru.mirea.pkmn.belousovdo.network;

import com.fasterxml.jackson.databind.json.JsonMapper;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class NetworkConfig {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Bean
    public Retrofit retrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("https://api.pokemontcg.io")
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(new JsonMapper()))
                .build();
    }

    @Bean
    public PokemonTcgAPI pokemonTcgAPI(Retrofit retrofit) {
        return retrofit.create(PokemonTcgAPI.class);
    }

    @Bean
    public PkmnHttpClient pkmnHttpClient(PokemonTcgAPI pokemonTcgAPI) {
        return new PkmnHttpClient(pokemonTcgAPI);
    }
}