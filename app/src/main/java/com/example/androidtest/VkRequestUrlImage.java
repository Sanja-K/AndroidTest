package com.example.androidtest;

import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.vk.sdk.VKUIHelper.getApplicationContext;


public abstract class VkRequestUrlImage  {


    private static final int VK_ID = 7022035;

    private static final String TAG = "MainActivity";
    public  static final int NUM_COLUMNS = 2;

    private static ArrayList<String> mNames = new ArrayList<>();
    private static ArrayList<String> mImageUrls = new ArrayList<>();

    private ArrayList<String> Urls = new ArrayList<>();

    public static ArrayMap<String,String> atributImage =new ArrayMap<>() ;

    private String vk_service_token = "568be899568be899568be899ee56e0cd4a5568b568be8990b8f1319705eba6da6ccadbe";

    private String album_id ="233402700";
    private String vk_club_id = "-24410762";
    private String count_image_vk = "10";
    private String version_vk_api = "5.95";

    private String version_vk_api_ = "5.101";

    public static boolean loading=true ;
    private String album_size;

    public static final String APP_PREFERENCES= "url_images";



    private VKRequest VKParametersRequst(String owner_id, String album_ids, String album_id,
                                         String count_sample, String version_vk_api, String method_api){

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
                Toast.makeText(getApplicationContext(), "POST Error : "+ error, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                Log.d(TAG, "POST attemptFailed");
                Toast.makeText(getApplicationContext(), "POST attemptFailed: ", Toast.LENGTH_SHORT).show();

            }
        });

    }

/*    protected void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
        for(int i=0 ; i<atributImage.size() && i<10;i++){

            mImageUrls.add(atributImage.valueAt(i));
            mNames.add(atributImage.keyAt(i));

            atributImage.removeAt(i);

        }
        Log.d(TAG," Attrimut Image :" +atributImage);
    }*/

    public abstract void initImageBitmaps();

    public abstract void initRecyclerView();

  //  public abstract void initRecyclerView(VkRequestUrlImage vkRequestUrlImage);
}
