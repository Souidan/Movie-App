
package com.mal.movieapp.Trailer_Pogo; ;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.mal.movieapp.Trailer_Pogo.Result;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Trailers {

    @SerializedName("id")
    private int mid;

    private List<Result> results = new ArrayList<Result>();

    /**
     * @return The id
     */

    public Trailers(int id, List<Result> results) {
        // this.id = id;
        this.results = results;
    }

    public int getId() {
        return mid;
    }

    /**
     * @param id The id
     */

    public void setId(int id) {
        this.mid = id;
    }

    /**
     * @return The results
     */

    public List<Result> getResults() {
        return results;
    }

    /**
     * @param results The results
     */

    public void setResults(List<Result> results) {
        this.results = results;
    }
}




