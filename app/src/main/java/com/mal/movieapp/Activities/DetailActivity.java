package com.mal.movieapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mal.movieapp.Adapter.CustomAdapter;
import com.mal.movieapp.Adapter.ReviewAdapter;
import com.mal.movieapp.Adapter.TrailerAdapter;
import com.mal.movieapp.Database.Movie;
import com.mal.movieapp.Movie_Pogo.MovieModel;
import com.mal.movieapp.R;
import com.mal.movieapp.Movie_Pogo.Result;
import com.mal.movieapp.Review_Pogo.Reviews;
import com.mal.movieapp.Trailer_Pogo.TrailerDeserializer;
import com.mal.movieapp.Trailer_Pogo.Trailers;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class DetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{


    Result movie;
    ImageView bckgrnd;
    ImageView moviePoster;
    TextView movieTitle;
    TextView ratingBar;
    TextView movie_date;
    TextView movieOverview;
    Realm realm;

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
    List<com.mal.movieapp.Trailer_Pogo.Result> arrlist;


    TrailerAdapter adapter;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();

        realm = Realm.getDefaultInstance();

        likeButton= (LikeButton)findViewById(R.id.star_button);

        Intent i = getIntent();
        this.movie = (Result) i.getSerializableExtra("movie");
        recyclerView=(RecyclerView) findViewById(R.id.listview_trailer);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);



        this.setTitle(movie.getTitle());
        this.getSupportActionBar().setIcon(R.drawable.icon);
        bckgrnd = (ImageView) findViewById(R.id.movie_bckgrnd);
        moviePoster = (ImageView) findViewById(R.id.movie_posterDetail);
        movieTitle = (TextView) findViewById(R.id.movie_ttl);
        movieOverview = (TextView) findViewById(R.id.movie_overview);
        movie_date = (TextView) findViewById(R.id.movie_date);
        ratingBar = (TextView) findViewById(R.id.movie_rating);
        imgBtn=(ImageButton) findViewById(R.id.reviewsBtn);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(DetailActivity.this,ReviewActivity.class);
                i.putExtra("movie", movie);
                DetailActivity.this.startActivity(i);

            }
        });


        System.out.println(movie.getReleaseDate() + " Date");
        System.out.println(movie.getOverview() + " overview");
        double d = movie.getVoteAverage();
        System.out.println((float) d + " avg");
        movieTitle.setText(movie.getTitle());

        movie_date.setText(movie.getReleaseDate());
        movieOverview.setText(movie.getOverview());

        ratingBar.setText(movie.getVoteAverage() + "/10");
        Picasso.with(this).load("http://image.tmdb.org/t/p/w370" + movie.getPosterPath()).into(moviePoster);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w370" + movie.getBackdropPath()).placeholder(R.drawable.progress_animation).into(bckgrnd);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w370" + movie.getPosterPath()).placeholder(R.drawable.progress_animation).into(moviePoster,
                PicassoPalette.with("http://image.tmdb.org/t/p/w370" + movie.getPosterPath(), moviePoster)
                        .use(PicassoPalette.Profile.VIBRANT_DARK)
                        .intoBackground(findViewById(R.id.main_framelayout_title)).
                        intoBackground(findViewById(R.id.main_toolbar))

                        .intoBackground(findViewById(R.id.scroller))
        );


        RequestQueue requestQueue = Volley.newRequestQueue(this);


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

                    adapter = new TrailerAdapter(DetailActivity.this,arrlist);
                    recyclerView.setAdapter(adapter);




                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError err) {
                    Toast.makeText(DetailActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, err.getMessage() + "   ERRRRROORRRR");

                }
            });

            requestQueue.add(stringRequest);


        } catch (MalformedURLException e1) {

            e1.printStackTrace();
        }



        RealmResults<Movie> result2 = realm.where(Movie.class)
                .equalTo("movie_id", movie.getId())
                .findAll();

        if(result2.size()>0){
            likeButton.setLiked(true);
        }else{
            likeButton.setLiked(false);
        }


            bindActivity();

        mAppBarLayout.addOnOffsetChangedListener(this);
            mTitle.setText(movie.getTitle());
        mToolbar.inflateMenu(R.menu.menu_main);
            startAlphaAnimation(mTitle, 0, View.INVISIBLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);



        likeButton.setOnLikeListener(new OnLikeListener() {


            @Override
            public void liked(LikeButton likeButton) {



                       realm.beginTransaction();


                        Movie movieRealm = realm.createObject(Movie.class);
                        movieRealm.movie_id=movie.getId();
                        movieRealm.title=movie.getTitle();
                        movieRealm.poster_path=movie.getPosterPath();
                        movieRealm.overview=movie.getOverview();
                        movieRealm.date=movie.getReleaseDate();
                        movieRealm.rating=movie.getVoteAverage();
                        movieRealm.backdrop=movie.getBackdropPath();

                        realm.commitTransaction();





                Toast.makeText(DetailActivity.this, "Movie Added To Favorites", Toast.LENGTH_SHORT).show();




            }

            @Override
            public void unLiked(LikeButton likeButton) {

                    try {
                        RealmResults<Movie> results = realm.where(Movie.class).equalTo("movie_id", movie.getId()).findAll();

                       realm.beginTransaction();
                        results.deleteAllFromRealm();
                        Toast.makeText(DetailActivity.this, "Not a favorite", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Log.e("Realm Error", "error" ,e);
                    }finally {
                        realm.commitTransaction();
                    }




            }
        });


            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //onBackPressed()
                    finish();
                }
            });
        }


    private void bindActivity() {
        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (RelativeLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }






    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(
                listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;

        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(
                        desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}








