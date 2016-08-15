package com.bytesnmaterials.zro.services;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.features.login.ILoginView;
import com.bytesnmaterials.zro.features.login.LoginActivity;
import com.bytesnmaterials.zro.features.register.IRegisterView;
import com.bytesnmaterials.zro.features.register.RegisterActivity;
import com.bytesnmaterials.zro.listeners.LoadingListener;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;

/**
 * This class contains various authentication methods for Login and Register.
 *
 * @author mitesh
 * @version 1.0
 * @since 22/7/16
 */
public class AuthService extends BaseDataService{

    public AuthService() {
        super();
    }

    /**
     * This method is used to authenticate user with email and password.
     * @param loginView : An instance of interface ILoginView
     */
    public void Login(final ILoginView loginView){
        loginView.getLoadingViewListener().onLoadingStarted();
        loginView.getFirebaseAuthObj()
                .signInWithEmailAndPassword(loginView.getEmail(), loginView.getPassword())
                .addOnCompleteListener((Activity) loginView.getActivityContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("signInWithEmail", "onComplete:" + task.isSuccessful());
                        loginView.getLoadingViewListener().onLoadingFinished();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("signInWithEmail", task.getException());
                            if(task.getException().getMessage().equals(ZeroErrorService.USER_PASSWORD_INVALID)){
                                AddError(ZeroErrorService.USER_PASSWORD_INVALID, true);
                            }else{
                                AddError(ZeroErrorService.USER_NOT_FOUND_FOR_EMAIL, true);
                            }
                            loginView.showErrorDialog(task.getException().getMessage());
                        }
                    }
                });
    }

    /**
     * This method is used to authenticate user with facebook access token.
     * @param loginView : An instance of interface ILoginView
     * @param token : Access token from facebook to access user profile.
     */
    public void Login(final ILoginView loginView, AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        loginView.getFirebaseAuthObj().signInWithCredential(credential)
                .addOnCompleteListener((Activity) loginView.getActivityContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("signInWithCredential", "onComplete:" + task.isSuccessful());
                        loginView.getLoadingViewListener().onLoadingFinished();
                        if (!task.isSuccessful()) {
                            Log.e("signInWithCredential", "signInWithCredential", task.getException());
                            AddError(ZeroErrorService.USER_SIGNIN_WITH_FACEBOOK_ERROR, true);
                            loginView.showErrorDialog(task.getException().getMessage());
                        }

                    }
                });
    }

    /**
     * This method is used to authenticate user with google account.
     * @param loginView : An instance of interface ILoginView
     * @param acct : An instance of GoogleSignInAccount, which is used to authenticate user with google account.
     */
    public void Login(final ILoginView loginView, GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        loginView.getFirebaseAuthObj().signInWithCredential(credential)
                .addOnCompleteListener((Activity) loginView.getActivityContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("signInWithCredential", "onComplete:" + task.isSuccessful());
                        loginView.getLoadingViewListener().onLoadingFinished();
                        if (!task.isSuccessful()) {
                            Log.e("signInWithCredential", "signInWithCredential", task.getException());
                            AddError(ZeroErrorService.USER_SIGNIN_WITH_GOOGLE_ERROR, true);
                            loginView.showErrorDialog(task.getException().getMessage());
                        }
                    }
                });
    }

    /**
     * This method is used to register user with email and password. Once the user is registered, will be signed in automatically.
     * @param registerView : An instance of interface IRegisterView
     */
    public void Register(final IRegisterView registerView){
        registerView.getFirebaseAuthObj()
                .createUserWithEmailAndPassword(registerView.getUserEmail(), registerView.getUserPassword())
                .addOnCompleteListener((Activity) registerView.getActivityContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("CreateUser", "CreateUserWithEmailPassword:onComplete:" + task.isSuccessful());
                        registerView.getLoadingViewListener().onLoadingFinished();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.e("CreateUser", "CreateUserWithEmailPassword Exception: "+ task.getException());
                            registerView.showErrorDialog(task.getException().getMessage());
                            AddError(ZeroErrorService.USER_MULTIPLE_FOUND_FOR_EMAIL, true);
                        }else{
                            //Save User To Firebase Databse
                            AddSuccess(ZeroSuccessService.USER_REGISTERED_SUCCESSFULLY);
                        }
                    }
                });
    }

    /**
     * This method is used to logout the user from firebase.
     * @param ref : An instance of FirebaseAuth, which is used to authenticate users with firebase.
     */
    public void LogOut(FirebaseAuth ref){
        ref.signOut();
    }


    /**
     * This method is used to reset the password of firebase account.
     * @param loginView : An instance of interface ILoginView
     * @param email : An email address to which link of password reset should be sent.
     */
    public void ResetPassword(final ILoginView loginView, String email){
        loginView.getFirebaseAuthObj().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loginView.getLoadingViewListener().onLoadingFinished();
                        if (task.isSuccessful()) {
                            Log.d("ResetPassword", "Email sent.");
                            loginView.showSuccessDialog("Email with link to reset the password sent successfully.");
                        }else{
                            AddError(ZeroErrorService.UNABLE_TO_SEND_RESET_LINK, true);
                            loginView.showErrorDialog(task.getException().getMessage());
                        }
                    }
                });
    }

    public void fetchProviderForEmail(final ILoginView loginView, String email){
        loginView.getFirebaseAuthObj().fetchProvidersForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("fetchProvider", "-->"+task.getResult().getProviders().get(0));
                            if(task.getResult().getProviders().get(0)
                                    .equalsIgnoreCase(loginView.getSelectedAuthProvider())){
                                //account exist with same provider
                            }else{
                                //account exist with different provider
                            }
                        }else{
                            //account not exist
                        }
                    }
                });
    }

}
