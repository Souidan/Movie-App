package com.mal.movieapp.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.florent37.picassopalette.PicassoPalette;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mal.movieapp.adapter.TrailerAdapter;
import com.mal.movieapp.database.Movie;
import com.mal.movieapp.database.MovieOperations;
import com.mal.movieapp.moviepogo.Result;
import com.mal.movieapp.R;
import com.mal.movieapp.trailerpogo.TrailerDeserializer;
import com.mal.movieapp.trailerpogo.Trailers;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    Result movie;
    ImageView bckgrnd;
    ImageView moviePoster;
    TextView movieTitle;
    TextView ratingBar;
    TextView movieDate;
    TextView movieOverview;
    private MovieOperations movieOps;
    List<Movie> movies;



    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private RelativeLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    ImageButton imgBtn;

    LikeButton likeButton;



    RecyclerView recyclerView;
    StringRequest stringRequest;
    public final String LOG_TAG = MainFragment.class.getSimpleName();
    List<com.mal.movieapp.trailerpogo.Result> arrlist;


    TrailerAdapter adapter;


    public DetailFragment() {
        // Required empty public constructor
    }







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }











    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_detail, container, false);


        likeButton= (LikeButton)v.findViewById(R.id.star_button);

        this.movie = (Result)getArguments().getSerializable("movie");
        recyclerView=(RecyclerView) v.findViewById(R.id.listview_trailer);
        mToolbar        = (Toolbar) v.findViewById(R.id.main_toolbar);
        mTitle          = (TextView) v.findViewById(R.id.main_textview_title);
        mTitleContainer = (RelativeLayout) v.findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) v.findViewById(R.id.main_appbar);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);


        getActivity().setTitle(movie.getTitle());
        bckgrnd = (ImageView) v.findViewById(R.id.movie_bckgrnd);
        moviePoster = (ImageView) v.findViewById(R.id.movie_posterDetail);
        movieTitle = (TextView) v.findViewById(R.id.movie_ttl);
        movieOverview = (TextView) v.findViewById(R.id.movie_overview);
        movieDate = (TextView) v.findViewById(R.id.movie_date);
        ratingBar = (TextView) v.findViewById(R.id.movie_rating);
        imgBtn=(ImageButton) v.findViewById(R.id.reviewsBtn);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(),ReviewActivity.class);
                i.putExtra("movie", movie);
               getActivity().startActivity(i);

            }
        });


        System.out.println(movie.getReleaseDate() + " Date");
        System.out.println(movie.getOverview() + " overview");
        double d = movie.getVoteAverage();
        System.out.println((float) d + " avg");
        if (getActivity().isTaskRoot()) {
            mToolbar.setNavigationIcon(null);

        }

        movieTitle.setText(movie.getTitle());
        mTitle.setText("");


        movieDate.setText(movie.getReleaseDate());
        movieOverview.setText(movie.getOverview());

        ratingBar.setText(movie.getVoteAverage() + "/10");
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w370" + movie.getPosterPath()).into(moviePoster);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w370" + movie.getBackdropPath()).placeholder(R.drawable.progress_animation).into(bckgrnd);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w370" + movie.getPosterPath()).placeholder(R.drawable.progress_animation).into(moviePoster,
                PicassoPalette.with("http://image.tmdb.org/t/p/w370" + movie.getPosterPath(), moviePoster)
                        .use(PicassoPalette.Profile.VIBRANT_DARK)
                        .intoBackground(v.findViewById(R.id.main_framelayout_title)).
                        intoBackground(v.findViewById(R.id.main_toolbar))

                        .intoBackground(v.findViewById(R.id.scroller))
        );


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());


        // Construct the URL for the OpenWeatherMap query
        // Possible parameters are available at OWM's forecast API page, at
        // http://openweathermap.org/API#forecast


        Uri.Builder builder = new Uri.Builder();
        Uri builtUri = builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movie.getId() + "")
                .appendPath("videos")
                .appendQueryParameter("api_key", "5ad0957e90e39700ef64ee586d98e080")
                .build();
        try {
            URL url2 = new URL(builtUri.toString());
            Log.v(LOG_TAG, url2.toString());

            String url = url2.toString();
            stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String res) {


                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Trailers.class, new TrailerDeserializer());

                    Gson gson = gsonBuilder.create();

                    Trailers trailersItem = gson.fromJson(res, Trailers.class);

                    arrlist = trailersItem.getResults();

                    System.out.println(arrlist.size());

                    adapter = new TrailerAdapter(getActivity(),arrlist);
                    recyclerView.setAdapter(adapter);




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




        movieOps = new MovieOperations(getActivity());

        Movie m2 = movieOps.getMovie(movie.getId());


        if(m2!=null){
            likeButton.setLiked(true);
        }else{
            likeButton.setLiked(false);
        }






        likeButton.setOnLikeListener(new OnLikeListener() {


            @Override
            public void liked(LikeButton likeButton) {




                Movie m2 = new Movie();


                m2.movieId =movie.getId();
                m2.title=movie.getTitle();
                m2.posterPath =movie.getPosterPath();
                m2.overview=movie.getOverview();
                m2.date=movie.getReleaseDate();
                m2.rating=movie.getVoteAverage();
                m2.backdrop=movie.getBackdropPath();


                movieOps.addMovie(m2);










                Toast.makeText(getActivity(), "Movie Added To Favorites", Toast.LENGTH_SHORT).show();




            }

            @Override
            public void unLiked(LikeButton likeButton) {




                    movieOps.removeMovie(movieOps.getMovie(movie.getId()));
                    Toast.makeText(getActivity(), "Movie Removed From Favorites", Toast.LENGTH_SHORT).show();





            }
        });


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed()

                    // do something }
                    getActivity().finish();


            }
        });

        return v;
    }




}


