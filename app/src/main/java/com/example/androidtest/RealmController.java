package com.example.androidtest;

import android.util.ArrayMap;
import android.util.Log;

import org.json.JSONArray;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.support.constraint.Constraints.TAG;

public class RealmController {

    private Realm realm;

    public RealmController() {

        this.realm = Realm.getDefaultInstance();

    }

    public void addData(final JSONArray jsonArray){
        realm.executeTransaction(new Realm.Transaction( ) {
            @Override
            public void execute(Realm realm) {

                realm.createAllFromJson(Model.class,jsonArray);

            }
        });

    }


    public void updateInfo( final JSONArray jsonArray) {

realm.executeTransaction(new Realm.Transaction() {
    @Override
    public void execute(Realm realm) {

        realm.createOrUpdateAllFromJson(Model.class, jsonArray);
        //realm.copyToRealmOrUpdate((Iterable<RealmModel>) jsonArray);

        Log.d(TAG,"updateInfo " );

    }
});
       // Model realmObject = realm.where(Model.class).equalTo("id", id_image).findFirst();

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


    public void removeItemById() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }

}
