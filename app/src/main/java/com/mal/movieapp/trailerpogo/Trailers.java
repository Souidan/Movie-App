
package com.mal.movieapp.trailerpogo; ;

import com.google.gson.annotations.SerializedName;

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




