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


public class VkRequestUrlImage  {


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

    ArrayList<String> urll =new ArrayList<>();
    ArrayList<String> Idim = new ArrayList<>();

    Model model= new Model();
    RealmController realmController = new RealmController();

    private VKRequest VKParametersRequst(String owner_id, String album_ids, String album_id,
                                         String count_sample, String version_vk_api, String method_api){

        VKParameters vkParameters = VKParameters.from("owner_id", owner_id,album_ids, album_id,
                "access_token", vk_service_token,"count",count_sample, "v", version_vk_api);
        VKRequest vkRequest = new VKRequest(method_api,vkParameters);
        vkRequest.setPreferredLang("ru");

        return vkRequest;
    }


    private void parseJsonVk(VKResponse response){
    JSONObject jsonObject = response.json;

        try {
        JSONArray jsonArray = new JSONArray(((JSONObject) jsonObject.get("response")).getString("items"));
        Log.d(TAG,"jsonArray  1221 " + jsonArray );

        realmController.updateInfo(jsonArray);

        //realmController.addData(jsonArray);
    } catch (JSONException e) {
        e.printStackTrace();
    }
            Log.d(TAG,"realmController " + realmController.getInfo(atributImage) ) ;
}

    private void parseJsonVk(VKResponse response, String field_name){

        JSONObject jsonObject = response.json;
        try {
            album_size = jsonObject.getString(field_name);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUrlPhotoClub(){

        loading=true;

        VKRequest vkRequest_size_album =  VKParametersRequst(vk_club_id,"album_ids",album_id,"1",
                version_vk_api_,"photos.getAlbums");

        vkRequest_size_album.executeWithListener(new VKRequest.VKRequestListener() {

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                parseJsonVk(response,"size");
            }
        });


        VKRequest vkRequest_uri_image =  VKParametersRequst(vk_club_id,"album_id",album_id,
                album_size,version_vk_api,"photos.get");

        vkRequest_uri_image.executeAfterRequest(vkRequest_size_album, new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                parseJsonVk(response);

               // initImageBitmaps();
               // initRecyclerView();
               // loading=false;
            }


            @Override
            public void onError(VKError error) {

                Log.d(TAG, "POST Error"+ error);
                Toast.makeText(getApplicationContext(), "POST Error : "+ error, Toast.LENGTH_SHORT).show();
               // loading=false;
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

                Log.d(TAG, "POST attemptFailed");
                Toast.makeText(getApplicationContext(), "POST attemptFailed: ", Toast.LENGTH_SHORT).show();
              //  loading=false;
            }
        });

            loading =false;
    }


  // public abstract void initImageBitmaps();
  //  public abstract void initRecyclerView();

}
