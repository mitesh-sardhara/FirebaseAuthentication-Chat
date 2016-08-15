package com.bytesnmaterials.zro.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bytesnmaterials.zro.ZeroApplication;
import com.bytesnmaterials.zro.listeners.LoadingListener;
import com.bytesnmaterials.zro.listeners.ProgressDialogListener;
import com.bytesnmaterials.zro.modules.MainComponent;

/**
 * Created by jehandad.kamal on 6/30/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialogListener mLoadingListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingListener = new ProgressDialogListener(this);
    }

    protected LoadingListener getLoadingListener() {
        return mLoadingListener;
    }

    protected ZeroApplication getZeroApp() {
        return (ZeroApplication) getApplication();
    }

    protected MainComponent getMainComponent() {
        return getZeroApp().getMainComponent();
    }
}
