package com.example.androidtest;

import android.util.ArrayMap;
import android.util.Log;

import org.json.JSONArray;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static android.support.constraint.Constraints.TAG;

public class RealmController {

    private Realm realm;

    public RealmController() {
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        realm.setDefaultConfiguration(config);
        this.realm = Realm.getDefaultInstance();

    }

    public void addData(final JSONArray jsonArray){
        realm.executeTransaction(new Realm.Transaction( ) {
            @Override
            public void execute(Realm realm) {

                realm.createAllFromJson(Model.class,jsonArray);
             /*   Model realmObject = realm.createObject(Model.class);
                realmObject.setId_image(id_image);
                realmObject.setUrl_image(url);*/

            }
        });

    }


    public RealmResults<Model> getInfo(final ArrayMap<String,String> arrayMap) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Model> models =realm.where(Model.class).findAll();

                for (Model model : models){

                    arrayMap.put(model.getId(),model.getPhoto_604());

                    Log.d(TAG,"Url_image " +model.getId());
                    Log.d(TAG,"Url_image " +model.getPhoto_604());

                }
            }
        });

        return realm.where(Model.class).findAll();
    }


    public void updateInfo( String id_image, String url) {
       /* realm.beginTransaction();

        Model realmObject = realm.where(Model.class).equalTo("id", id).findFirst();
        realmObject.setId_image(id_image);
        realmObject.setUrl_image(url);

        realm.commitTransaction();*/
    }

    public void removeItemById() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }

}
