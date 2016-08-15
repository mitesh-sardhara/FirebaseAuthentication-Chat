package com.bytesnmaterials.zro;

import android.app.Application;
import android.content.Context;
import android.location.Location;

import com.bumptech.glide.request.target.ViewTarget;
import com.bytesnmaterials.zro.modules.AppModule;
import com.bytesnmaterials.zro.modules.DaggerMainComponent;
import com.bytesnmaterials.zro.modules.MainComponent;
import com.facebook.FacebookSdk;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;

/**
 * Created by mahya on 11/2/2015.
 */
public class ZeroApplication extends Application {
    private Location lastReceivedLocation;
    private Location lastReceivedOriginalLocation;
    private MainComponent mainComponent;

    public MainComponent getMainComponent() {
        return mainComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this);
        Iconify.with(new FontAwesomeModule())
                .with(new MaterialModule())
                .with(new MaterialCommunityModule());

        ViewTarget.setTagId(R.id.glide_tag);
        createComponent();
    }

    protected void createComponent() {
        mainComponent = DaggerMainComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }

    public Location getLastReceivedLocation() {
        return lastReceivedLocation;
    }

    public void setLastReceivedLocation(Location location) {
        lastReceivedLocation = location;
    }

    public Location getLastReceivedOriginalLocation() {
        return lastReceivedOriginalLocation;
    }

    public void setLastReceivedOriginalLocation(Location location) {
        lastReceivedOriginalLocation = location;
    }
}
