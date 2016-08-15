package com.bytesnmaterials.zro;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
/*import com.bytesnmaterials.zro.services.User.UserService;
import com.bytesnmaterials.zro.services.ZeroSuccessService;*/


/**
 * Created by mahya on 11/16/2015.
 */
public class ZeroBaseActionBarActivity extends AppCompatActivity {
    protected MaterialDialog mProgressDialog;

    //region Success Result Handling
    /*protected void Succeeded (int code, Boolean ToDisplay){
        ZeroSuccessService successReport = ZeroSuccessService.fromCode(code);

        if (ToDisplay){
            DisplayInfo(successReport.getMessage());
        }
    }*/
    //endregion

    //region display messages
    protected void showToast(String messsage , int duration){
        if (messsage == null) return;

        if (duration == 0)
            duration = Toast.LENGTH_SHORT;

        Toast.makeText(this, messsage, duration).show();
    }

    protected void DisplayError(String ErrorMessage){
        if (ErrorMessage == null) return;
        if (ErrorMessage.equals(""))
            ErrorMessage = "Error on hte process. Please try again.";
        showToast(ErrorMessage, Toast.LENGTH_LONG);
    }

    protected void DisplayInfo(String InfoMessage){
        if (InfoMessage == null) return;
        if (InfoMessage.equals(""))
            InfoMessage = "Done Successfully.";
        showToast(InfoMessage ,Toast.LENGTH_LONG );
    }

//    protected void showErrorDialog(String message) {
//        new AlertDialog.Builder(this)
//                .setTitle("Error")
//                .setMessage(message)
//                .setPositiveButton(android.R.string.ok, null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
//    }

    //endregion

    //region get information
    /*protected String getLoggedUserKey() {
        UserService userService = new UserService(this);
        return userService.getLoggedUserKey();
    }

    protected String getLoggedUserName(){
        UserService userService = new UserService(this);
        return userService.getLoggedUserPermenantName();
    }

    protected String getLoggedUserDisplayName(){
        UserService userService = new UserService(this);
        return userService.getLoggedUserDisplayName();
    }*/
    //endregion

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

    //region Closing Activity
    protected void closeActivity(){
        finish();
    }
    //.
}
