package com.bytesnmaterials.zro;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by mahya on 11/9/2015.
 */
public class ZeroBaseActivity extends Activity {

    protected MaterialDialog mProgressDialog;

    protected void showToast(String messsage , int duration){
        if (duration == 0)
            duration = Toast.LENGTH_SHORT;

        Toast.makeText(this, messsage, duration).show();
    }

    protected void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /*protected String getLoggedUserKey()
    {
        UserService userService = new UserService(this);
        return userService.getLoggedUserKey();
    }

    protected String getLoggedUserName()
    {
        UserService userService = new UserService(this);
        return userService.getLoggedUserPermenantName();
    }

    protected String getLoggedUserDisplayName()
    {
        UserService userService = new UserService(this);
        return userService.getLoggedUserDisplayName();
    }*/

    //region progress dialog
    protected void SetUpProgressDialog(@StringRes Integer  title, @StringRes Integer content) {
        mProgressDialog = new MaterialDialog.Builder(this)
                .title(title != null ? title : R.string.loading)
                .content(content != null ? content : R.string.loading)
                .progress(true, 0)
                .show();
    }

    protected void HideProgressDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing() ) {
            //mProgressDialog.hide();
            mProgressDialog.dismiss();
        }
    }
    //endregion

}
