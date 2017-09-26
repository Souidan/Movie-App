package com.mal.movieapp.activities;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mal.movieapp.R;


public class DetailActivity extends AppCompatActivity {


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();
        Bundle extras = getIntent().getExtras();


        if (savedInstanceState == null) {

            DetailFragment mDetailsFragment = new DetailFragment();
            //Pass the "extras" Bundle that contains the selected "name" to the fragment
            mDetailsFragment.setArguments(extras);
            getFragmentManager().beginTransaction().add(R.id.containerDetail, mDetailsFragment).commit();

        }


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


}








