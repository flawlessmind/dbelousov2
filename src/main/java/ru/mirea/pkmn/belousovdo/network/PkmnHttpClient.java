package ru.mirea.pkmn.belousovdo.network;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Response;


import retrofit2.Call;
import retrofit2.Callback;


public class PkmnHttpClient {

    private final PokemonTcgAPI tcgAPI;

    @Autowired
    PkmnHttpClient(PokemonTcgAPI tcgAPI) {
        this.tcgAPI = tcgAPI;
    }

    public void getPokemonCard(String name, String number, PokemonCardCallback callback) {

        String requestQuery = "name:\"" + name + "\" number:" + number;

        Call<JsonNode> call = tcgAPI.getPokemon(requestQuery);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    callback.onSuccess((JsonNode) response.body());
                } else {
                    callback.onError(new Exception("Request failed with code: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull Throwable t) {
                callback.onError(t);
            }
        });
    }

    public interface PokemonCardCallback {
        void onSuccess(JsonNode cardData);
        void onError(Throwable error);
    }
}
