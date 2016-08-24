package com.mal.movieapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by souidan on 8/13/16.
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<Result> results = new ArrayList<Result>();
    private static LayoutInflater inflater=null;

    public CustomAdapter(Activity mainActivity, List r) {
        // TODO Auto-generated constructor stub
        results=r;
        context=mainActivity;
        //imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.grid_item_movie, null);
        holder.tv=(TextView) rowView.findViewById(R.id.movie_title);
        holder.img=(ImageView) rowView.findViewById(R.id.movie_poster);

        holder.tv.setText(results.get(position).getTitle());
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185"+results.get(position).getPosterPath()).into(holder.img);




        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(context,DetailActivity.class);
                i.putExtra("movie", results.get(position));
                context.startActivity(i);
                Toast.makeText(context, "You Clicked "+results.get(position).getTitle()+getCount()+"\n"+"http://image.tmdb.org/t/p/w400"+results.get(position).getPosterPath(), Toast.LENGTH_LONG).show();
            }
        });

        return rowView;
    }
}
