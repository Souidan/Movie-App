package com.mal.movieapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mal.movieapp.trailerpogo.Result;
import com.mal.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by souidan on 8/25/16.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.Holder> {

    Context context;
    List<Result> results = new ArrayList<Result>();
    private static LayoutInflater inflater = null;


    public TrailerAdapter(Activity activity, List r) {
        // TODO Auto-generated constructor stub
        results = r;
        context = activity;
        System.out.println("chck A");
       /* inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/

    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return results.size();
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        Result ci = results.get(i);
        holder.txt.setText(ci.getName());
        Picasso.with(context).load("http://img.youtube.com/vi/" + ci.getKey() + "/0.jpg").into(holder.img);
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txt;

        public Holder(View v) {
            super(v);
            img = (ImageView) v.findViewById(R.id.mediaPreview);
            txt = (TextView) v.findViewById(R.id.videoDetail);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + results.get(pos).getKey())));


                }
            });

        }
    }


   /* @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
       Holder holder=new Holder();
        View rowView;
        System.out.println("chck B");

        rowView = inflater.inflate(R.layout.list_item_trailer, null);
        holder.img=(ImageView) rowView.findViewById(R.id.mediaPreview);
        holder.txt=(TextView)rowView.findViewById(R.id.videoDetail) ;
        holder.txt.setText(results.get(position).getName());

        Picasso.with(context).load("http://img.youtube.com/vi/"+results.get(position).getKey()+"/0.jpg").into(holder.img);




        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+results.get(position).getKey())));
            }
        });

        return rowView;
    }*/

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_item_trailer, viewGroup, false);

        return new Holder(itemView);
    }
}
