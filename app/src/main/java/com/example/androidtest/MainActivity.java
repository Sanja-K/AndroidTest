package com.example.androidtest;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity  {

    private static final int VK_ID = 7022035;

    private static final String TAG = "MainActivity";
    public  static final int NUM_COLUMNS = 2;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> Urls = new ArrayList<>();

    private ArrayMap<String,String> atImage =new ArrayMap<>() ;

    private String vk_service_token = "568be899568be899568be899ee56e0cd4a5568b568be8990b8f1319705eba6da6ccadbe";

    private String album_id ="233402700";
    private String vk_club_id = "-24410762";
    private String count_image_vk = "10";
    private String version_vk_api = "5.95";

    private String version_vk_api_ = "5.101";

    public static boolean loading=true ;
    private String album_size;

    public static VkRequestUrlImage vkRequestUrlImage;


    public static final String APP_PREFERENCES= "data_images";
    public static final String APP_PREFERENCES_URL="Url";
    public static final String APP_PREFERENCES_ID="Id";

    SharedPreferences mSettings;

    RealmController realmController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        OnShowSettings();
        inittImageBitmaps();
        inittRecyclerView();

        VkRequestUrlImage vkRequestUrlImage= new VkRequestUrlImage() {
            @Override
            public void initImageBitmaps() {
                atImage=atributImage;
                inittImageBitmaps();
            }

            @Override
            public void initRecyclerView() {
                inittRecyclerView();
            }
        };






        vkRequestUrlImage.getUrlPhotoClub();
    }


    @Override
    protected void onPause() {
        super.onPause();
        OnPutSettings();
    }

    public void inittRecyclerView(){

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        final StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter =
                new StaggeredRecyclerViewAdapter(MainActivity.this, mNames, mImageUrls);

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


    public void OnPutSettings(){
        SharedPreferences.Editor editor = mSettings.edit();
        /**TODO поэксперементировать с записью в preferences поробовать String вместо ArraySet*/
        editor.putStringSet(APP_PREFERENCES_ID, new ArraySet<>(mNames));
        editor.putStringSet(APP_PREFERENCES_URL, new ArraySet<>(mImageUrls));
        editor.apply();
    }

    public void OnShowSettings(){
        ArrayList<String> Urls;
        ArrayList<String> Ids;

        Set<String> Url = mSettings.getStringSet(APP_PREFERENCES_URL, new ArraySet<String>());
        Set<String> Id =  mSettings.getStringSet(APP_PREFERENCES_ID, new ArraySet<String>() );

        Urls= new ArrayList(Url);
        Ids= new ArrayList<>(Id) ;

        for(int i =0; i<Urls.size()&& Urls.size()==Ids.size();i++) {
            atImage.put(Ids.get(i),Urls.get(i)); }

        Log.d(TAG, "mImageUrls OnShowSettings :"+ mImageUrls);
        Log.d(TAG, "mNames OnShowSettings :"+ mNames);

    }
   /* private VKRequest VKParametersRequst(String owner_id,String album_ids, String album_id,
                                         String count_sample,String version_vk_api, String method_api){

        VKParameters vkParameters = VKParameters.from("owner_id", owner_id,album_ids, album_id,
                "access_token", vk_service_token,"count",count_sample, "v", version_vk_api);
        VKRequest vkRequest = new VKRequest(method_api,vkParameters);
        vkRequest.setPreferredLang("ru");


        return vkRequest;
    }


    private void parseJsonVk(VKResponse response, String field_name){

        JSONObject jsonObject = response.json;
        Log.d(TAG, "Post jsonObject: "+ field_name+ " " + jsonObject);

        String owner = null;
        String user_id = null;

        try {

            JSONArray jsonArray = new JSONArray(((JSONObject) jsonObject.get("response")).getString("items"));

            for( int i=0; i<jsonArray.length();i++){
                JSONObject json_obj=jsonArray.getJSONObject(i);
                owner = json_obj.getString(field_name);

                if(!field_name.equals("size")){
                    user_id=json_obj.getString("id");
                    atributImage.put(user_id,owner);

                }else{ album_size =owner; }

                Log.d(TAG, "Post owner " + i + "  " + owner );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


 public void getUrlPhotoClub(){


        VKRequest vkRequest_size_album =  VKParametersRequst(vk_club_id,"album_ids",album_id,"1",
              version_vk_api_,"photos.getAlbums");

     vkRequest_size_album.executeWithListener(new VKRequest.VKRequestListener() {

         @Override
         public void onComplete(VKResponse response) {
             super.onComplete(response);
             JSONObject jsonObject = response.json;
             Log.d(TAG, "Post jsonObject: vkRequest1 " + jsonObject);

             parseJsonVk(response,"size");
         }
     });


     VKRequest vkRequest_uri_image =  VKParametersRequst(vk_club_id,"album_id",album_id,
             album_size,version_vk_api,"photos.get");

     vkRequest_uri_image.executeAfterRequest(vkRequest_size_album, new VKRequest.VKRequestListener() {
         @Override
         public void onComplete(VKResponse response) {
             super.onComplete(response);

             parseJsonVk(response,"photo_604");
             initImageBitmaps();
             initRecyclerView();
         }


         @Override
         public void onError(VKError error) {
            Log.d(TAG, "POST Error"+ error);
             Toast.makeText(MainActivity.this, "POST Error : "+ error, Toast.LENGTH_SHORT).show();
         }
         @Override
         public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
             Log.d(TAG, "POST attemptFailed");
             Toast.makeText(MainActivity.this, "POST attemptFailed: ", Toast.LENGTH_SHORT).show();

         }
     });

 }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
        for(int i=0 ; i<atributImage.size() && i<10;i++){

            mImageUrls.add(atributImage.valueAt(i));
            mNames.add(atributImage.keyAt(i));

            atributImage.removeAt(i);

        }
        Log.d(TAG," Attrimut Image :" +atributImage);
    }
*/
 /*   public void initRecyclerView(){

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);

        final StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter =
                new StaggeredRecyclerViewAdapter(this, mNames, mImageUrls);

        final RecyclerView.LayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);

        recyclerView.addOnScrollListener(new EndlessScrollListener(loading) {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!recyclerView.canScrollVertically(1)){

                }
            }

            @Override
            public void loadMoreItem() {
                vkRequestUrlImage.initImageBitmaps(mImageUrls,mNames);
            staggeredRecyclerViewAdapter.notifyDataSetChanged();
            loading = true;
            }
        } );
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);

    }*/
}