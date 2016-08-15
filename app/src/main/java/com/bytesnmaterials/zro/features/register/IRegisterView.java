package com.bytesnmaterials.zro.features.register;

import android.content.Context;

import com.bytesnmaterials.zro.listeners.LoadingListener;
import com.bytesnmaterials.zro.models.UserAuth;
import com.google.firebase.auth.FirebaseAuth;

/**
 * An interface class for register view controller.
 *
 * @author mitesh
 * @version 1.0
 * @since 26/7/16
 */
public interface IRegisterView {

    String getUserFullName();

    String getUserNickName();

    String getUserEmail();

    String getUserPassword();

    String getUserConfirmPassword();

    String getUserCountry();

    String getRegisterDateTime();

    UserAuth getUserFromInputValue();

    LoadingListener getLoadingViewListener();

    FirebaseAuth getFirebaseAuthObj();

    Context getActivityContext();

    void showErrorDialog (String message);

    void showSuccessDialog (String message);
}
