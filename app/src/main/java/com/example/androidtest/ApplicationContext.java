package com.example.androidtest;

import com.vk.sdk.VKSdk;
import io.realm.Realm;

public class ApplicationContext extends android.app.Application {
    //private static final int VK_ID = 7022035;
  //  public static final String VK_API_VERSION = "1.6.5";
   // private static String sTokenKey = "568be899568be899568be899ee56e0cd4a5568b568be8990b8f1319705eba6da6ccadbe";

    /*VKAccessTokenTracker tokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                Intent intent = new Intent(ApplicationContext.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };*/


    @Override
    public void onCreate() {
        super.onCreate();

        //VKSdk.initialize(sdkListener, VK_APP_ID);
     //   VKSdk.initialize(sdkListener, VK_ID,VKAccessToken.tokenFromSharedPreferences(this,sTokenKey));
        Realm.init(this);
        VKSdk.initialize(this);

    }
}
