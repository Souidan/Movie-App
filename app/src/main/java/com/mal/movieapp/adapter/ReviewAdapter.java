package com.mal.movieapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mal.movieapp.R;
import com.mal.movieapp.reviewpogo.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by souidan on 8/26/16.
 */


public class ReviewAdapter extends BaseAdapter {

    Context context;
    List<Result> results = new ArrayList<Result>();
    private static LayoutInflater inflater = null;


    public ReviewAdapter(Activity activity, List r) {
        // TODO Auto-generated constructor stub
        results = r;
        context = activity;
        System.out.println("chck A");
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ReviewAdapter.Holder holder = new ReviewAdapter.Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.list_item_review, null);

        holder.author = (TextView) rowView.findViewById(R.id.author);
        holder.content = (TextView) rowView.findViewById(R.id.content);
        holder.url = (TextView) rowView.findViewById(R.id.url);

        holder.author.setText(results.get(position).getAuthor() + " Says: ");
        holder.content.setText(results.get(position).getContent());
        holder.url.setText(results.get(position).getUrl());

        return rowView;
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView author;
        TextView content;
        TextView url;

    }


}
