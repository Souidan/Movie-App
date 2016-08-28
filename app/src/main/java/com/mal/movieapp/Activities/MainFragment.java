package com.mal.movieapp.Activities;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mal.movieapp.Adapter.CustomAdapter;
import com.mal.movieapp.Database.Movie;
import com.mal.movieapp.Movie_Pogo.MovieModel;
import com.mal.movieapp.R;
import com.mal.movieapp.Movie_Pogo.Result;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private CustomAdapter adapter;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    List<Result> arrlist = new ArrayList<Result>();
    View v;

    GridView gv;
    Realm realm;

    StringRequest stringRequest;
    public final String LOG_TAG = MainFragment.class.getSimpleName();
    RequestQueue requestQueue;

    Menu menu;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();

        requestQueue = Volley.newRequestQueue(getActivity());






        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        String type = prefs.getString("type", "popular");
        System.out.println(type +"typee");
        if (type.equals("fav")) {

            RealmResults<Movie> result = realm.where(Movie.class).findAll();
            result = result.sort("movie_id");

            Movie[] array=(result.toArray(new Movie [result.size()]));


            for(int i=0;i<array.length;i++) {
                Result rslt= new Result(array[i].poster_path,null,array[i].overview,array[i].date,null,array[i].movie_id,null
                ,null,array[i].title,array[i].backdrop,null,null,null,array[i].rating);
                arrlist.add(rslt);

            }






        } else {
            if (restoredText != null) {
                type = prefs.getString("type", "popular");//"No name defined" is the default value.

            }

            Uri.Builder builder = new Uri.Builder();
            Uri builtUri = builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(type)
                    .appendQueryParameter("api_key", "5ad0957e90e39700ef64ee586d98e080")
                    .build();
            try {
                URL url2 = new URL(builtUri.toString());
                Log.v(LOG_TAG, url2.toString()+" 124");

                String url = url2.toString();
                stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String res) {


                        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                        MovieModel movieItem = gson.fromJson(res, MovieModel.class);
                        System.out.println(res);

                        System.out.println(movieItem.getResults().get(3).getTitle()+"rrrrrrrrrrrr");

                        MainFragment.this.arrlist=movieItem.getResults();


                        adapter = new CustomAdapter(getActivity(), arrlist);

                        gv.setAdapter(adapter);




                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                        Log.e(LOG_TAG, err.getMessage() + "   153 ERRRRROORRRR");

                    }
                });

                requestQueue.add(stringRequest);


            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        setHasOptionsMenu(true);
        v=inflater.inflate(R.layout.fragment_main, container, false);
        gv=(GridView)v.findViewById(R.id.gridview_movie) ;

        System.out.println(arrlist.size()+"Sizeee");
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        String type = prefs.getString("type", "popular");
        System.out.println(type +"typee");
        if (type.equals("fav")) {

            adapter = new CustomAdapter(getActivity(), arrlist);

            gv.setAdapter(adapter);
        }


        return v;
        }





    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        this.menu=menu;
        inflater.inflate(R.menu.main,menu);
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        String type = prefs.getString("type", "popular");


        if(type.equals("popular")){

            System.out.println(type+"  inside pop");

            menu.findItem(R.id.sort_pop).setChecked(true);
           // menu.findItem(R.id.sort_rate).setChecked(false);

        }else{

            if(type.equals("top_rated")) {

                System.out.println(type+"  inside top rated");

               // menu.findItem(R.id.sort_pop).setChecked(false);
                menu.findItem(R.id.sort_rate).setChecked(true);
            }

        }



    }
    @Override
    public void onResume(){

        super.onResume();

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        String type = prefs.getString("type", "popular");

        if(type.equals("fav")){

            arrlist.clear();

            RealmResults<Movie> result = realm.where(Movie.class).findAll();
            result = result.sort("movie_id");

            Movie[] array=(Movie [])result.toArray(new Movie [result.size()]);
            Result rslt;
            for(int i=0;i<array.length;i++) {
                rslt= new Result(array[i].poster_path,null,array[i].overview,array[i].date,null,array[i].movie_id,null
                        ,null,array[i].title,array[i].backdrop,null,null,null,array[i].rating);
                arrlist.add(rslt);
            }

            adapter.notifyDataSetChanged();




        }

    }


    @Override




    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        item.setChecked(!item.isChecked());

        if(id==R.id.sort_pop){
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("type", "popular");
            editor.commit();


            SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String restoredText = prefs.getString("text", null);
            String type = prefs.getString("type", "popular");
            if (restoredText != null) {
                type = prefs.getString("type", "popular");//"No name defined" is the default value.

            }

            Uri.Builder builder = new Uri.Builder();
            Uri builtUri = builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(type)
                    .appendQueryParameter("api_key", "5ad0957e90e39700ef64ee586d98e080")
                    .build();
            try {
                URL url2 = new URL(builtUri.toString());
                Log.v(LOG_TAG, url2.toString()+" 258");

                String url = url2.toString();
                stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String res) {


                        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                        MovieModel movieItem = gson.fromJson(res, MovieModel.class);
                        try {
                            arrlist.clear();
                            for (int i = 0; i < movieItem.getResults().size(); i++) {
                                Result movie = movieItem.getResults().get(i);
                                arrlist.add(movie);

                            }

                            adapter.notifyDataSetChanged();
                        }
                        catch(NullPointerException e2){

                            Toast.makeText(getActivity(), "ERROR: Could Not Repopulate View \nPlease Recreate View By Rotating The Screen \nOr Re-Starting The App!!", Toast.LENGTH_LONG).show();

                        }


                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                        Log.e(LOG_TAG, err.getMessage() + "   ERRRRROORRRR 293");

                    }
                });

                requestQueue.add(stringRequest);
            }



             catch (MalformedURLException e1) {
                e1.printStackTrace();
            }





        }else {
            if (id == R.id.sort_rate) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("type", "top_rated");

                editor.commit();


                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String restoredText = prefs.getString("text", null);
                String type = prefs.getString("type", "popular");
                if (restoredText != null) {
                    type = prefs.getString("type", "popular");//"No name defined" is the default value.

                }

                Uri.Builder builder = new Uri.Builder();
                Uri builtUri = builder.scheme("https")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(type)
                        .appendQueryParameter("api_key", "5ad0957e90e39700ef64ee586d98e080")
                        .build();
                try {
                    URL url2 = new URL(builtUri.toString());
                    Log.v(LOG_TAG, url2.toString()+" 337");

                    String url = url2.toString();
                    stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String res) {


                            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                            MovieModel movieItem = gson.fromJson(res, MovieModel.class);

                            try {

                                arrlist.clear();
                                for (int i = 0; i < movieItem.getResults().size(); i++) {
                                    Result movie = movieItem.getResults().get(i);
                                    arrlist.add(movie);

                                }

                                adapter.notifyDataSetChanged();



                            } catch (NullPointerException e2) {

                                Toast.makeText(getActivity(), "ERROR: Could Not Repopulate View \nPlease Recreate View By Rotating The Screen \nOr Re-Starting The App!!", Toast.LENGTH_LONG).show();

                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError err) {
                            Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                            Log.e(LOG_TAG, err.getMessage() + "   373 ERRRRROORRRR");

                        }
                    });

                    requestQueue.add(stringRequest);


                }

                               catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }


            }else{
                if(id==R.id.sort_fav){
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("type", "fav");

                    editor.commit();

                    arrlist.clear();

                    RealmResults<Movie> result = realm.where(Movie.class).findAll();
                    result = result.sort("movie_id");

                    Movie[] array=(Movie [])result.toArray(new Movie [result.size()]);
                    Result rslt;
                    for(int i=0;i<array.length;i++) {
                        rslt= new Result(array[i].poster_path,null,array[i].overview,array[i].date,null,array[i].movie_id,null
                                ,null,array[i].title,array[i].backdrop,null,null,null,array[i].rating);
                        arrlist.add(rslt);
                    }

                    if(array.length==0){

                        Toast.makeText(getActivity(), "No Favorites To Show", Toast.LENGTH_SHORT).show();


                    }

                        adapter.notifyDataSetChanged();







                }

            }

            }





        return super.onOptionsItemSelected(item);


    }




}
