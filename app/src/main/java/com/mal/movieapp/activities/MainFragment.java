package com.mal.movieapp.activities;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.mal.movieapp.adapter.CustomAdapter;
import com.mal.movieapp.database.Movie;
import com.mal.movieapp.database.MovieOperations;
import com.mal.movieapp.moviepogo.MovieModel;
import com.mal.movieapp.R;
import com.mal.movieapp.moviepogo.Result;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private CustomAdapter adapter;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    List<Result> arrlist = new ArrayList<Result>();
    View v;
    int pagePOP = 1;
    int pageRate = 1;
    int index = 0;

    GridView gv;
    String type;

    private MovieOperations movieOps;
    List<Movie> movies;

    private ProgressDialog pDialog;


    StringRequest stringRequest;
    public final String LOG_TAG = MainFragment.class.getSimpleName();
    RequestQueue requestQueue;

    Menu menu;
    private SwipeRefreshLayout swipeContainer;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestQueue = Volley.newRequestQueue(getActivity());


        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        type = prefs.getString("type", "popular");
        System.out.println(type + "typee");
        if (type.equals("fav")) {
            movieOps = new MovieOperations(getActivity());
            movieOps.open();
            movies = movieOps.getAllMovies();
            movieOps.close();


            Movie[] array = (movies.toArray(new Movie[movies.size()]));


            for (int i = 0; i < array.length; i++) {
                Result rslt = new Result(array[i].posterPath, null, array[i].overview, array[i].date, null, array[i].movieId, null
                        , null, array[i].title, array[i].backdrop, null, null, null, array[i].rating);
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
                Log.v(LOG_TAG, url2.toString() + " 124");

                String url = url2.toString();
                stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String res) {


                        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                        MovieModel movieItem = gson.fromJson(res, MovieModel.class);
                        System.out.println(res);

                        System.out.println(movieItem.getResults().get(3).getTitle() + "rrrrrrrrrrrr");

                        MainFragment.this.arrlist = movieItem.getResults();


                        adapter = new CustomAdapter(getActivity(), arrlist);

                        gv.setAdapter(adapter);


                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                        Log.e(LOG_TAG, err.getMessage() + "   153 ERRRRROORRRR");
                        adapter = new CustomAdapter(getActivity(), arrlist);

                        gv.setAdapter(adapter);


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
        v = inflater.inflate(R.layout.fragment_main, container, false);
        gv = (GridView) v.findViewById(R.id.gridview_movie);
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        type = prefs.getString("type", "popular");

       /* gv.setOnScrollListener(new EndlessScrollListener() {


                                   int i=0;

                                   @Override
                                   public boolean onLoadMore(int page, int totalItemsCount) {
                                       // Triggered only when new data needs to be appended to the list
                                       // Add whatever code is needed to append new items to your AdapterView

                                       SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                       String restoredText = prefs.getString("text", null);
                                       type = prefs.getString("type", "popular");

                                       System.out.println(page + " in scroller");

                                       if (type.equals("popular")|| type.equals("top_rated")) {

                                           System.out.println(pagePOP+"popular" +" in scroller");
                                           System.out.println(pageRate+"top_rated" +" in scroller");


                                           if(type.equals("popular"))
                                               page=pagePOP;
                                           else
                                           page=pageRate;

                                           Uri.Builder builder = new Uri.Builder();
                                           Uri builtUri = builder.scheme("https")
                                                   .authority("api.themoviedb.org")
                                                   .appendPath("3")
                                                   .appendPath("movie")
                                                   .appendPath(type)
                                                   .appendQueryParameter("api_key", "5ad0957e90e39700ef64ee586d98e080")
                                                   .appendQueryParameter("page", page + "")
                                                   .build();
                                           try {
                                               URL url2 = new URL(builtUri.toString());
                                               Log.v(LOG_TAG, url2.toString() + " 258");

                                               String url = url2.toString();
                                               stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                                                   @Override
                                                   public void onResponse(String res) {


                                                       Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                                                       MovieModel movieItem = gson.fromJson(res, MovieModel.class);
                                                       try {



                                                           for (int i = 0; i < movieItem.getResults().size(); i++) {
                                                               Result movie = movieItem.getResults().get(i);
                                                               arrlist.add(movie);
                                                               System.out.println(movie.getTitle());

                                                           }


                                                           adapter.notifyDataSetChanged();

                                                       } catch (NullPointerException e2) {


                                                           Toast.makeText(getActivity(), e2.getMessage() + " on options", Toast.LENGTH_LONG).show();

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
                                               if(type.equals("popular"))
                                                   pagePOP++;
                                               else
                                                   pageRate++;
                                           } catch (MalformedURLException e1) {
                                               e1.printStackTrace();
                                           }

                                           // or customLoadMoreDataFromApi(totalItemsCount);

                                       }
                                       return true; // ONLY if more data is actually being loaded; false otherwise.

                                   }
                               }
            );



*/


        System.out.println(arrlist.size() + "Sizeee");

        System.out.println(type + "typee");
        if (type.equals("fav")) {

            adapter = new CustomAdapter(getActivity(), arrlist);

            gv.setAdapter(adapter);
        }

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {

                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String restoredText = prefs.getString("text", null);
                type = prefs.getString("type", "popular");
                System.out.println(type + "helllloooo");
                if (type.equals("fav")) {
                    swipeContainer.setRefreshing(false);

                }

                if (type.equals("popular") || type.equals("top_rated")) {

                    System.out.println("in refresh");

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
                        Log.v(LOG_TAG, url2.toString() + " 258");

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
                                        System.out.println(movie.getTitle());

                                    }
                                    swipeContainer.setRefreshing(false);


                                    adapter.notifyDataSetChanged();
                                } catch (NullPointerException e2) {
                                    swipeContainer.setRefreshing(false);


                                    Toast.makeText(getActivity(), e2.getMessage() + " on options", Toast.LENGTH_LONG).show();

                                }


                            }

                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError err) {
                                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                                Log.e(LOG_TAG, err.getMessage() + "   ERRRRROORRRR 293");
                                swipeContainer.setRefreshing(false);


                            }
                        });

                        requestQueue.add(stringRequest);
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    }


                }


                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_red_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_red_light);


        return v;
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        this.menu = menu;
        inflater.inflate(R.menu.main, menu);
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        type = prefs.getString("type", "popular");


        if (type.equals("popular")) {

            System.out.println(type + "  inside pop");

            menu.findItem(R.id.sort_pop).setChecked(true);
            // menu.findItem(R.id.sort_rate).setChecked(false);

        } else {

            if (type.equals("top_rated")) {

                System.out.println(type + "  inside top rated");

                // menu.findItem(R.id.sort_pop).setChecked(false);
                menu.findItem(R.id.sort_rate).setChecked(true);
            }

        }


    }

    @Override
    public void onResume() {

        super.onResume();
        gv.smoothScrollToPosition(index);


        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        type = prefs.getString("type", "popular");

        if (type.equals("fav")) {

            arrlist.clear();

            movieOps = new MovieOperations(getActivity());
            movieOps.open();
            movies = movieOps.getAllMovies();
            movieOps.close();


            Movie[] array = (movies.toArray(new Movie[movies.size()]));
            Result rslt;
            for (int i = 0; i < array.length; i++) {
                rslt = new Result(array[i].posterPath, null, array[i].overview, array[i].date, null, array[i].movieId, null
                        , null, array[i].title, array[i].backdrop, null, null, null, array[i].rating);
                arrlist.add(rslt);
            }

            adapter.notifyDataSetChanged();


        } else {

        }

    }


    @Override


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        item.setChecked(!item.isChecked());

        if (id == R.id.sort_pop) {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("type", "popular");
            editor.commit();


            SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String restoredText = prefs.getString("text", null);
            type = prefs.getString("type", "popular");
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
                Log.v(LOG_TAG, url2.toString() + " 258");

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

                            Toast.makeText(getActivity(), e2.getMessage() + " on options", Toast.LENGTH_LONG).show();

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
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }


        } else {
            if (id == R.id.sort_rate) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("type", "top_rated");

                editor.commit();


                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String restoredText = prefs.getString("text", null);
                type = prefs.getString("type", "popular");
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
                    Log.v(LOG_TAG, url2.toString() + " 337");

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


                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }


            } else {
                if (id == R.id.sort_fav) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("type", "fav");

                    editor.commit();

                    arrlist.clear();

                    movieOps = new MovieOperations(getActivity());
                    movieOps.open();
                    movies = movieOps.getAllMovies();
                    movieOps.close();

                    Movie[] array = (movies.toArray(new Movie[movies.size()]));
                    Result rslt;
                    for (int i = 0; i < array.length; i++) {
                        rslt = new Result(array[i].posterPath, null, array[i].overview, array[i].date, null, array[i].movieId, null
                                , null, array[i].title, array[i].backdrop, null, null, null, array[i].rating);
                        arrlist.add(rslt);
                    }

                    if (array.length == 0) {

                        Toast.makeText(getActivity(), "No Favorites To Show", Toast.LENGTH_SHORT).show();


                    }
                    adapter.notifyDataSetChanged();


                }

            }

        }


        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("onSave");
        if (gv == null)
            index = 0;
        else {

            index = gv.getFirstVisiblePosition();
            System.out.println(gv.getFirstVisiblePosition() + "POSITIONNN");
        }
    }
}
