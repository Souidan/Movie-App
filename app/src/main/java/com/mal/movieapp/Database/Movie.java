package com.mal.movieapp.Database;


import java.io.Serializable;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by souidan on 8/27/16.
 */


public class Movie extends RealmObject implements Serializable {

    public String title;

    public String overview;

    public double rating;

    public int movie_id;

    public String date;

    public String poster_path;

    public String backdrop;




    public Movie() {
        super();
    }

    public Movie(String title, String overview,double rating,int movie_id,String date,String poster_path,String backdrop) {
        super();
        this.title = title;
        this.overview = overview;
        this.rating = rating;
        this.movie_id = movie_id;
        this.date = date;
        this.poster_path=poster_path;
        this.backdrop=backdrop;

    }


    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}

