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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;


public class StaggeredRecyclerViewAdapter extends RealmRecyclerViewAdapter<Model, StaggeredRecyclerViewAdapter.ViewHolder> {
    private  static  final String Tag ="StaggeredRecyclerViewAd";
    private Realm realm;
    private Context mContext;

    public StaggeredRecyclerViewAdapter(RealmResults<Model> list,Context context) {
        super(list,true,true);
        this.mContext = context;
        realm = Realm.getDefaultInstance ();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_item, parent, false);
        Log.d(Tag, "onCreateViewHolder" );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        Model temp = getItem(position);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Log.d(Tag,"temp.getPhoto_604() : " + temp.getPhoto_604());
        Log.d(Tag,"temp.getId() : " + temp.getId());
        Glide.with(mContext)
                .load(temp.getPhoto_604())
                .apply(requestOptions)
                .into(holder.image);

        holder.name.setText(temp.getId());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Log.d(Tag,"onClick : clicked on: " + mNames.get(position));
               // Toast.makeText(mContext, temp.getId(), Toast.LENGTH_SHORT).show();
            }
        });
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
