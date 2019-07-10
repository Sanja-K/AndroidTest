package com.example.androidtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class StaggeredRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredRecyclerViewAdapter.ViewHolder> {

    private  static  final String Tag ="StaggeredRecyclerViewAd";

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;

    public StaggeredRecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls) {
        this.mNames = names;
        this.mImageUrls = imageUrls;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(Tag, "onBindViewHolder : called");

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Log.d(Tag,"mImageUrls.get(position) : " + position);


        Glide.with(mContext)
                .load(mImageUrls.get(position))
                .apply(requestOptions)
                .into(holder.image);

        holder.name.setText(mNames.get(position));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Tag,"onClick : clicked on: " + mNames.get(position));
                Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.image_view_widget);
            this.name = itemView.findViewById(R.id.name_widget);
        }
    }


}
