package com.mal.movieapp.trailerpogo;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by souidan on 8/26/16.
 */

public  class TrailerDeserializer implements JsonDeserializer<Trailers> {

    @Override
    public Trailers deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {


        JsonObject jsonObject= (JsonObject) json;
        List<Result> result= new ArrayList<Result>();

        Trailers trailers = new Trailers(jsonObject.get("id").getAsInt(),result);
        JsonArray jArray = jsonObject.getAsJsonArray("results");

        for (int i=0; i<jArray.size(); i++) {
            JsonObject jObject = (JsonObject) jArray.get(i);
            //assuming you have the suitable constructor...
            Result oneTrailer= new Result(jObject.get("id").getAsString(),
                    jObject.get("iso_639_1").getAsString(),
                    jObject.get("iso_3166_1").getAsString(),
                    jObject.get("key").getAsString(),
                    jObject.get("name").getAsString(),
                    jObject.get("site").getAsString(),
                    jObject.get("size").getAsInt(),
                    jObject.get("type").getAsString()

            );
            result.add(oneTrailer);

        }
        return trailers;

    }
}