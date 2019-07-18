package com.example.androidtest;

import io.realm.RealmObject;

public class Model extends RealmObject {
    private String photo_604;
    private String id;

    /**TODO попробовать перенести id или url  в другую модель либо через первичный ключ */
    public String getPhoto_604() {
        return photo_604;
    }

    public void setPhoto_604(String photo_604) {
        this.photo_604 = photo_604;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
