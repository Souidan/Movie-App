package com.mal.movieapp.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.toolbox.StringRequest;
import com.mal.movieapp.R;

import android.widget.TextView;

import java.util.List;


public class ReviewActivity extends AppCompatActivity {


    private TextView stickyView;
    private ListView listView;
    private ImageView heroImageView;
    private View stickyViewSpacer;
    ImageButton backbtn;
    private int MAX_ROWS = 20;
    com.mal.movieapp.moviepogo.Result movie;


    RecyclerView recyclerView;
    StringRequest stringRequest;
    public final String LOG_TAG = MainFragment.class.getSimpleName();
    ProgressDialog pDialog;

    List<com.mal.movieapp.reviewpogo.Result> reviewarrlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        getSupportActionBar().hide();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.containerReviews, new ReviewFragment())
                    .commit();
        }


    }
}
