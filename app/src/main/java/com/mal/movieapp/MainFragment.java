package com.mal.movieapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ShareActionProvider;
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
import com.mal.movieapp.R;

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
    List<Result> arrlist;
    View v;

    GridView gv;

    StringRequest stringRequest;
    public final String LOG_TAG = MainFragment.class.getSimpleName();


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        setHasOptionsMenu(true);
        v=inflater.inflate(R.layout.fragment_main, container, false);
        gv=(GridView) v.findViewById(R.id.gridview_movie);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());


            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are available at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

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
            Log.v(LOG_TAG, url2.toString());

            String url=url2.toString();
            stringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {

                @Override
                public void onResponse(String res) {


                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                    MovieModel movieItem = gson.fromJson(res, MovieModel.class);

                    arrlist= movieItem.getResults();

                    adapter=new CustomAdapter(getActivity(),arrlist);

                    gv.setAdapter(adapter);



                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError err) {
                    Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG,err.getMessage() +"   ERRRRROORRRR");

                }
            });

            requestQueue.add(stringRequest);



        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }finally {
            return v;
        }




    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main,menu);
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        String type = prefs.getString("type", "popular");


        System.out.println(type+"  kkkkkkkkkkkkkkkkkk");


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




    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        item.setChecked(!item.isChecked());

        if(id==R.id.sort_pop){
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("type", "popular");
            editor.commit();
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());


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
                Log.v(LOG_TAG, url2.toString());

                String url=url2.toString();
                stringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String res) {


                        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                        MovieModel movieItem = gson.fromJson(res, MovieModel.class);

                        arrlist.clear();
                       for(int i=0;i<movieItem.getResults().size();i++){
                           Result movie= movieItem.getResults().get(i);
                           arrlist.add(movie);

                        }
                        Toast.makeText(getActivity(),movieItem.getResults().get(0).getTitle()+"ello",Toast.LENGTH_LONG);

                        adapter.notifyDataSetChanged();




                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                        Log.e(LOG_TAG,err.getMessage() +"   ERRRRROORRRR");

                    }
                });

                requestQueue.add(stringRequest);



            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }





        }else {
            if (id == R.id.sort_rate) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("type", "top_rated");

                editor.commit();
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());


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
                    Log.v(LOG_TAG, url2.toString());

                    String url = url2.toString();
                    stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String res) {


                            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                            MovieModel movieItem = gson.fromJson(res, MovieModel.class);


                            arrlist.clear();
                            for(int i=0;i<movieItem.getResults().size();i++){
                                Result movie= movieItem.getResults().get(i);
                                arrlist.add(movie);

                            }
                            Toast.makeText(getActivity(), movieItem.getResults().get(0).getTitle() + "ello", Toast.LENGTH_LONG);

                            adapter.notifyDataSetChanged();


                        }

                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError err) {
                            Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                            Log.e(LOG_TAG, err.getMessage() + "   ERRRRROORRRR");

                        }
                    });

                    requestQueue.add(stringRequest);


                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }


            }
        }




        return super.onOptionsItemSelected(item);


    }




}
