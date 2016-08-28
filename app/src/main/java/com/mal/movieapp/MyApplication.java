package com.mal.movieapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by souidan on 8/27/16.
 */

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        // The Realm file will be located in Context.getFilesDir() with name "default.realm"
        RealmConfiguration config = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build()     ;
        Realm.setDefaultConfiguration(config);
    }
}

