package com.example.androidtest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity  {

    private static final int VK_ID = 7022035;

    private static final String TAG = "MainActivity";
    public  static final int NUM_COLUMNS = 2;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private ArrayMap<String,String> atImage =new ArrayMap<>() ;


    public static boolean loading=true ;
    private String album_size;

    public static VkRequestUrlImage vkRequestUrlImage;

    RealmController realmController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Model> results;

        results = realm.where(Model.class)
                .findAll();

        inittRecyclerView(results);

        VkRequestUrlImage vkRequestUrlImage= new VkRequestUrlImage() {
            @Override
            public void initImageBitmaps() {
             //   atImage=atributImage;
               // inittImageBitmaps();
            }

            @Override
            public void initRecyclerView() {
              //  inittRecyclerView();
            }
        };

       // vkRequestUrlImage.getUrlPhotoClub();
    }


    @Override
    protected void onPause() {
        super.onPause();
       // OnPutSettings();
    }

    public void inittRecyclerView( RealmResults results){

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        final StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter =
                new StaggeredRecyclerViewAdapter( results, this );

        RecyclerView.LayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);


        recyclerView.addOnScrollListener(new EndlessScrollListener(loading) {

            @Override
            public void loadMoreItem() {

                Log.d(TAG,"loadMoreItem : " + mImageUrls + " "+ mNames);
                inittImageBitmaps();
                staggeredRecyclerViewAdapter.notifyDataSetChanged();
                loading = true;

            }
        } );

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
    }

    protected void inittImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
        for(int i=0 ; i<atImage.size() && i<10;i++){

            mImageUrls.add(atImage.valueAt(i));
            mNames.add(atImage.keyAt(i));

            atImage.removeAt(i);

        }
        Log.d(TAG," Attrimut Image :" +atImage);
    }
}