package com.mal.movieapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{


    Result movie;
    ImageView bckgrnd;
    ImageView moviePoster;
    TextView movieTitle;
    TextView ratingBar;
    TextView movie_date;
    TextView movieOverview;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private RelativeLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();



        Intent i = getIntent();
        this.movie = (Result) i.getSerializableExtra("movie");

        this.setTitle(movie.getTitle());
        this.getSupportActionBar().setIcon(R.drawable.icon);
        bckgrnd = (ImageView) findViewById(R.id.movie_bckgrnd);
        moviePoster = (ImageView) findViewById(R.id.movie_posterDetail);
        movieTitle = (TextView) findViewById(R.id.movie_ttl);
        movieOverview = (TextView) findViewById(R.id.movie_overview);
        movie_date = (TextView) findViewById(R.id.movie_date);
        ratingBar = (TextView) findViewById(R.id.movie_rating);


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
        bindActivity();
        mAppBarLayout.addOnOffsetChangedListener(this);
        mTitle.setText(movie.getTitle());
        mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


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
}








