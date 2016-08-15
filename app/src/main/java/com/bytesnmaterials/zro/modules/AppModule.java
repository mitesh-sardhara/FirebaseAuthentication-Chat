package com.bytesnmaterials.zro.modules;

import android.content.Context;

import com.bytesnmaterials.zro.BuildConfig;
import com.bytesnmaterials.zro.ZeroApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    ZeroApplication mApplication;

    public AppModule(ZeroApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    ZeroApplication providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    FirebaseAuth providesFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    FirebaseDatabase providesFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Provides
    @Singleton
    DatabaseReference providesFirebaseDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    /*@Provides
    @Singleton
    Query providesUserQuery(Firebase firbase) {
        return new Firebase(BuildConfig.ENDPOINT);
    }*/

}