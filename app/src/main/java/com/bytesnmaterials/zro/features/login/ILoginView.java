package com.bytesnmaterials.zro.features.login;

import android.content.Context;

import com.bytesnmaterials.zro.listeners.LoadingListener;
import com.bytesnmaterials.zro.models.UserAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

/**
 * An interface class for login view controller.
 *
 * @author mitesh
 * @version 1.0
 * @since 26/7/16
 */
public interface ILoginView {

    String getEmail();

    String getPassword();

    void goHomePage();

    LoadingListener getLoadingViewListener();

    FirebaseAuth getFirebaseAuthObj();

    DatabaseReference getDatabaseReference();

    void showErrorDialog(String message);

    void showSuccessDialog(String message);

    Context getActivityContext();

    void resetView();

    UserAuth getUserFromFirebase(String uid);

    String getSelectedAuthProvider();

}
