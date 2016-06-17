package com.abhijitmitkar.realmdemo;

import android.app.Application;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by Abhijit on 15-06-2016.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // The Realm file will be located in Context.getFilesDir() with name "default.realm"
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .schemaVersion(1)
                .migration(new RealmMigration() {
                    @Override
                    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                        RealmSchema realmSchema = realm.getSchema();

                        // From version 0 to 1
                        if(oldVersion == 0) {
                            realmSchema.get(Note.CLASS)
                                    .addField(Note.NOTE_ID, long.class, FieldAttribute.PRIMARY_KEY);
                        }
                    }
                })
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
