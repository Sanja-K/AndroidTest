package com.example.androidtest;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {

    private static final int VK_ID = 7022035;

    private static final String TAG = "MainActivity";
    public  static final int NUM_COLUMNS = 2;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private ArrayMap<String,String> atImage =new ArrayMap<>() ;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static boolean loading=true ;
    private String album_size;

    VkRequestUrlImage vkRequestUrlImage =new VkRequestUrlImage();

    RealmController realmController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(
                  Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Model> results;
        results = realm.where(Model.class)
                .findAll();

        inittRecyclerView(results);

/*
        VkRequestUrlImage vkRequestUrlImage= new VkRequestUrlImage() {*/
/*
            @Override
            public void initImageBitmaps() {
             //   atImage=atributImage;
               // inittImageBitmaps();
            }

            @Override
            public void initRecyclerView() {
              //  inittRecyclerView();
            }*//*

        };
*/

        vkRequestUrlImage.getUrlPhotoClub();

        mSwipeRefreshLayout.setOnRefreshListener(this);
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
               // inittImageBitmaps();
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

    @Override
    public void onRefresh() {
        Log.d(TAG, " loading : " +vkRequestUrlImage.loading);
        if(!vkRequestUrlImage.loading){
            Toast.makeText(getApplicationContext(),"onRefresh",Toast.LENGTH_SHORT);
            vkRequestUrlImage.getUrlPhotoClub();

        }
        mSwipeRefreshLayout.setRefreshing(false);
    }
}