package com.mal.movieapp.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.mal.movieapp.adapter.ReviewAdapter;
import com.mal.movieapp.R;
import com.mal.movieapp.reviewpogo.Result;
import com.mal.movieapp.reviewpogo.Reviews;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {

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

    List<Result> reviewarrlist;


    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_review, container, false);


        Intent i = getActivity().getIntent();

        this.movie = (com.mal.movieapp.moviepogo.Result) i.getSerializableExtra("movie");


        /* Initialise list view, hero image, and sticky view */
        listView = (ListView) v.findViewById(R.id.listView);
        backbtn = (ImageButton) v.findViewById(R.id.imageButton2);
        heroImageView = (ImageView) v.findViewById(R.id.heroImageView);
        stickyView = (TextView) v.findViewById(R.id.stickyView);
        /* Inflate list header layout */
        View listHeader = inflater.inflate(R.layout.list_header_layout, null);
        stickyViewSpacer = listHeader.findViewById(R.id.stickyViewPlaceholder);
        pDialog = new ProgressDialog(getActivity());
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

                getActivity().finish();
            }
        });

        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w370" + movie.getBackdropPath()).placeholder(R.drawable.progress_animation).into(heroImageView,
                PicassoPalette.with("http://image.tmdb.org/t/p/w370" + movie.getPosterPath(), heroImageView)
                        .use(PicassoPalette.Profile.VIBRANT_DARK)
                        .intoBackground(v.findViewById(R.id.reviewsBckg)).intoBackground(v.findViewById(R.id.stickyView))
        );


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());


        // Construct the URL for the OpenWeatherMap query
        // Possible parameters are available at OWM's forecast API page, at
        // http://openweathermap.org/API#forecast


        Uri.Builder builderReview = new Uri.Builder();
        Uri builtUriReview = builderReview.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movie.getId() + "")
                .appendPath("reviews")
                .appendQueryParameter("api_key", "5ad0957e90e39700ef64ee586d98e080")
                .build();
        try {
            URL url2 = new URL(builtUriReview.toString());

            String url = url2.toString();
            stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                ProgressDialog getpBar() {
                    return pDialog;
                }

                void setpBar(ProgressDialog pbar) {
                    pDialog = pbar;
                }


                @Override
                public void onResponse(String res) {


                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();


                    Reviews reviewsItem = gson.fromJson(res, Reviews.class);

                    reviewarrlist = reviewsItem.getResults();

                    System.out.println(reviewarrlist.size());

                    if (reviewarrlist.size() == 0) {
                        Toast.makeText(getActivity(), "No Movie Reviews Have Been Added To " + movie.getTitle(), Toast.LENGTH_LONG).show();

                    }


                    ReviewAdapter reviewsadapter = new ReviewAdapter(getActivity(), reviewarrlist);
                    listView.setAdapter(reviewsadapter);
                    if (pDialog != null) {
                        pDialog.dismiss();
                        pDialog = null;
                    }


                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError err) {
                    Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, err.getMessage() + "   ERRRRROORRRR");
                    if (pDialog != null) {
                        pDialog.dismiss();
                        pDialog = null;
                    }

                }
            });

            requestQueue.add(stringRequest);


        } catch (MalformedURLException e1) {

            e1.printStackTrace();
        }
        return v;

    }


}
