package com.mal.movieapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.mal.movieapp.helper.MovieListener;
import com.mal.movieapp.moviepogo.Result;
import com.mal.movieapp.R;

public class MainActivity extends AppCompatActivity implements MovieListener {


    boolean twoPanel;


    public final String LOG_TAG = MainFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().show();

        setContentView(R.layout.activity_main);

        FrameLayout frameLayoutPanel2 = (FrameLayout) findViewById(R.id.panel2);

        if (frameLayoutPanel2 == null) {

            twoPanel = false;

        } else {
            twoPanel = true;
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }

        Log.d(LOG_TAG, "On Create");
    }

    @Override
    public void setSelectedMovie(Result movie) {

        if (twoPanel) {
            DetailFragment detailsFragment = new DetailFragment();
            Bundle extras = new Bundle();
            extras.putSerializable("movie", movie);
            detailsFragment.setArguments(extras);
            getFragmentManager().beginTransaction().replace(R.id.panel2, detailsFragment).commit();

        } else {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra("movie", movie);
            startActivity(i);
        }


    }


}
