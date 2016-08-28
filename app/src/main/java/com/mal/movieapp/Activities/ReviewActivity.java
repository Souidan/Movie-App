package com.mal.movieapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.florent37.picassopalette.PicassoPalette;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mal.movieapp.Adapter.ReviewAdapter;
import com.mal.movieapp.R;
import com.mal.movieapp.Review_Pogo.Reviews;
import com.mal.movieapp.Trailer_Pogo.Result;
import com.squareup.picasso.Picasso;

import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class ReviewActivity extends AppCompatActivity {


    private TextView stickyView;
    private ListView listView;
    private ImageView heroImageView;
    private View stickyViewSpacer;
    ImageButton backbtn;
    private int MAX_ROWS = 20;
    com.mal.movieapp.Movie_Pogo.Result movie;


    RecyclerView recyclerView;
    StringRequest stringRequest;
    public final String LOG_TAG = MainFragment.class.getSimpleName();
    List<com.mal.movieapp.Review_Pogo.Result> reviewarrlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions


        } else{
            // do something for phones running an SDK before lollipop
        }

        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        getSupportActionBar().hide();
        Intent i = getIntent();

        this.movie = (com.mal.movieapp.Movie_Pogo.Result) i.getSerializableExtra("movie");


        /* Initialise list view, hero image, and sticky view */
        listView = (ListView) findViewById(R.id.listView);
        backbtn =(ImageButton) findViewById(R.id.imageButton2) ;
        heroImageView = (ImageView) findViewById(R.id.heroImageView);
        stickyView = (TextView) findViewById(R.id.stickyView);
        /* Inflate list header layout */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.list_header_layout, null);
        stickyViewSpacer = listHeader.findViewById(R.id.stickyViewPlaceholder);
        /* Add list view header */
        listView.addHeaderView(listHeader);
        /* Handle list View scroll events */
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /* Check if the first item is already reached to top.*/
                if (listView.getFirstVisiblePosition() == 0) {
                    View firstChild = listView.getChildAt(0);
                    int topY = 0;
                    if (firstChild != null) {
                        topY = firstChild.getTop();
                    }
                    int heroTopY = stickyViewSpacer.getTop();
                    stickyView.setY(Math.max(0, heroTopY + topY));
                    /* Set the image to scroll half of the amount that of ListView */
                    heroImageView.setY(topY * 0.5f);
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Picasso.with(this).load("http://image.tmdb.org/t/p/w370" + movie.getBackdropPath()).placeholder(R.drawable.progress_animation).into(heroImageView,
                PicassoPalette.with("http://image.tmdb.org/t/p/w370" + movie.getPosterPath(), heroImageView)
                        .use(PicassoPalette.Profile.VIBRANT_DARK)
                        .intoBackground(findViewById(R.id.reviewsBckg)).intoBackground(findViewById(R.id.stickyView))
        );



        RequestQueue requestQueue = Volley.newRequestQueue(this);


        // Construct the URL for the OpenWeatherMap query
        // Possible parameters are available at OWM's forecast API page, at
        // http://openweathermap.org/API#forecast


        Uri.Builder builderReview = new Uri.Builder();
        Uri builtUriReview = builderReview.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movie.getId()+ "")
                .appendPath("reviews")
                .appendQueryParameter("api_key", "5ad0957e90e39700ef64ee586d98e080")
                .build();
        try {
            URL url2 = new URL(builtUriReview.toString());

            String url = url2.toString();
            stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String res) {


                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();


                    Reviews reviewsItem = gson.fromJson(res, Reviews.class);

                    reviewarrlist = reviewsItem.getResults();

                    System.out.println(reviewarrlist.size());

                    if(reviewarrlist.size()==0){
                        Toast.makeText(ReviewActivity.this, "No Movie Reviews Have Been Added To "+movie.getTitle(), Toast.LENGTH_LONG).show();

                    }


                    ReviewAdapter reviewsadapter = new ReviewAdapter(ReviewActivity.this,reviewarrlist);
                    listView.setAdapter(reviewsadapter);




                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError err) {
                    Toast.makeText(ReviewActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, err.getMessage() + "   ERRRRROORRRR");

                }
            });

            requestQueue.add(stringRequest);


        } catch (MalformedURLException e1) {

            e1.printStackTrace();
        }








    }
}
