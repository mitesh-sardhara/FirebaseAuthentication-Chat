package com.bytesnmaterials.zro.listeners;

import android.app.Activity;
import android.app.ProgressDialog;

import com.bytesnmaterials.zro.R;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jehandad.kamal on 7/3/2016.
 */
public class ProgressDialogListener implements LoadingListener {


    AtomicInteger loaderCount = new AtomicInteger(0);
    ProgressDialog mProgressDialog;
    private Activity activity;

    public ProgressDialogListener(Activity activity) {
        this.activity = activity;
    }


    @Override
    public void onLoadingStarted() {
        int i = loaderCount.incrementAndGet();
//        if (i > 0 && isResumed && mProgressDialog == null) showProgressDialog();
        if (i > 0 && mProgressDialog == null) showProgressDialog();
    }

    @Override
    public void onLoadingFinished() {
        if (loaderCount.decrementAndGet() <= 0) {
            loaderCount.set(0);
            hideProgressDialog();
        }
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mProgressDialog = null;
    }

    protected void showProgressDialog() {
        mProgressDialog = new ProgressDialog(activity, R.style.AppTheme_Dialog);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(activity.getString(R.string.msg_loading));
        mProgressDialog.show();
    }
}
