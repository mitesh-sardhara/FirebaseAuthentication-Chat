package com.bytesnmaterials.zro.features.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.R;
import com.bytesnmaterials.zro.base.BaseActivity;
import com.bytesnmaterials.zro.features.chat.UsersChatActivity;
import com.bytesnmaterials.zro.features.home.HomeActivity;
import com.bytesnmaterials.zro.features.register.RegisterActivity;
import com.bytesnmaterials.zro.listeners.LoadingListener;
import com.bytesnmaterials.zro.models.User;
import com.bytesnmaterials.zro.models.UserAuth;
import com.bytesnmaterials.zro.models.UserInfo;
import com.bytesnmaterials.zro.services.AuthService;
import com.bytesnmaterials.zro.services.SharedPreferenceServices;
import com.bytesnmaterials.zro.services.User.UserService;
import com.bytesnmaterials.zro.util.ZeroDateTimeUtil;
import com.bytesnmaterials.zro.util.ZeroNetUtil;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This class is used as the container of Login screen.
 * It also includes various authentication methods for Email, Google and Facebook Authentications.
 *
 * @author jehandad.kamal
 * @version 1.0
 * @since 7/4/2016
 */
public class LoginActivity extends BaseActivity implements Validator.ValidationListener,
        GoogleApiClient.OnConnectionFailedListener, ILoginView {

    private static final int REQUEST_REGISTER_ACTIVITY = 101;
    private static final int REQUEST_HOME_ACTIVITY = 102;

    private static final int REQUEST_USER_CHAT_ACTIVITY = 103;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @NotEmpty
    @Length(min = 4, max = 30, trim = true)
    @BindView(R.id.edtEmail)
    TextInputEditText edtEmail;

    @NotEmpty
    @BindView(R.id.edtPassword)
    @Password(min = 6, scheme = Password.Scheme.ALPHA)
    TextInputEditText edtPassword;

    @BindView(R.id.btnLoginWithGoogle)
    Button btnLoginWithGoogle;

    @BindView(R.id.btnLoginWithFacebook)
    Button btnLoginWithFacebook;

    @BindView(R.id.txtForgotPassword)
    TextView txtForgotPassword;

    @Inject
    FirebaseAuth mAuth;

    @Inject
    FirebaseDatabase mFirebaseDatabase;

    @Inject
    DatabaseReference mDatabase;

    Validator validator;

    public Context context;

    private boolean isForgotPassword = false;

    private String selectedAuthProvider = "";

    private LoadingListener loadingListener = null;
    /* A flag is used to check whether the user is authenticated with email or not */
    private boolean isLoggedInWithEmail = false;
    /* A flag is used to check whether the user is authenticated with google/facebook or not */
    private boolean isLoggedOtherType = false;

    /* Debug Tag for use logging debug output to LogCat */
    private static final String TAG = LoginActivity.class.getSimpleName();

    /* Listener for Firebase session changes */
    private FirebaseAuth.AuthStateListener mAuthListener;

    /* *************************************
     *              GOOGLE                 *
     ***************************************/
    /* Request code used to invoke sign in user interactions for Google+ */
    public static final int RC_GOOGLE_LOGIN = 1;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* *************************************
     *              FACEBOOK               *
     ***************************************/
    /* The callback manager for Facebook */
    private CallbackManager mFacebookCallbackManager;
    private boolean isLoggedOut = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private boolean isBackPressed = false;

    private String strCountry = "NA";
    private String strNickName = "";
    private String strFullName = "";
    private String strAuthProvider = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getMainComponent().inject(this);
        setSupportActionBar(toolbar);
        context = this;
        initialize();
        validator = new Validator(this);
        validator.setValidationListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * This method is used for initialization process of Firebase Authentication Listener, which notifies users when firebase auth state changes..
     */
    private void initialize() {
        loadingListener = getLoadingListener();

        if (ZeroNetUtil.isConnected(context)) {
            initializeGoogleApi();
            initializeFacebookApi();
        } else {
            showErrorDialog(getString(R.string.error_no_network));
        }


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    isLoggedOut = false;

                    // User is signed in with email
                    if (isLoggedInWithEmail) {
                        isLoggedInWithEmail = false;
                        strAuthProvider = Constants.AUTH_EMAIL;
                        //save logged in user to shared preferences
                        saveUserToPreferences(user);
                        //go to home page once saved.
                        goHomePage();
                    } else if (isLoggedOtherType) { // User is signed in with google or facebook
                        isLoggedOtherType = false;
                        //save user's profile information to firebase
                        saveUserProfileToFirebase(user);
                        //save logged in user to shared preferences
                        saveUserToPreferences(user);

                        goHomePage();
                    }
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                    if (isLoggedOut) {
                        isLoggedOut = false;
                        clearUserFromPreferences();
                        resetView();
                    }

                }
            }
        };

        //check if user is already logged in.
        SharedPreferenceServices preferenceServices = new SharedPreferenceServices();
        preferenceServices.getFlagValue(context, Constants.KEY_IS_LOGGED_IN);
        if (preferenceServices.getFlagValue(context, Constants.KEY_IS_LOGGED_IN)) {
            goHomePage();
        }
    }

    /**
     * This method is used to configure and initialize Google API.
     */
    private void initializeGoogleApi() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();
    }

    /**
     * This method is used to load the facebook api with callback manger regarding login.
     */
    private void initializeFacebookApi() {
        mFacebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                strAuthProvider = Constants.AUTH_FACEBOOK;
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("GraphRequest1", "==>"+response);
                                try {
                                    String firstName = response.getJSONObject().getString("first_name");
                                    String lastName = response.getJSONObject().getString("last_name");
                                    String name = response.getJSONObject().getString("name");
                                    strFullName = firstName+" "+lastName;
                                    strNickName = name;
                                    strCountry = "NA";
                                    firebaseAuthWithFacebook(loginResult.getAccessToken());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,email,last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                showErrorDialog(error.getMessage());
            }
        });
    }

    /**
     * This method is from life cycle of activity. We are registering the firebase auth state listener here.
     */
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * This method is from life cycle of activity. We are removing the firebase auth state listener here.
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isBackPressed = true;
    }

    /**
     * This method will be called when login button is clicked.
     */
    @OnClick(R.id.btnLogin)
    public void clickLogin() {
        if (ZeroNetUtil.isConnected(getBaseContext())) {
            validator.validate();
        } else {
            showErrorDialog(getString(R.string.error_no_network));
        }
    }

    /**
     * This method will be called when register button is clicked.
     */
    @OnClick(R.id.btnRegister)
    public void clickRegister() {
        startActivityForResult(RegisterActivity.newIntent(this), REQUEST_REGISTER_ACTIVITY);
    }

    /**
     * This method will be called when facebook login button is clicked.
     */
    @OnClick(R.id.btnLoginWithFacebook)
    public void clickFacebookLogin() {
        if (ZeroNetUtil.isConnected(context)) {
            LoginManager.getInstance().logInWithReadPermissions(this,
                    Arrays.asList("email", "public_profile"));
        } else {
            showErrorDialog(getString(R.string.error_no_network));
        }

    }

    /**
     * This method will be called when google login button is clicked. Here google sign in intent need to be start using GoogleSignInApi.
     */
    @OnClick(R.id.btnLoginWithGoogle)
    public void clickGoogleLogin() {
        if (ZeroNetUtil.isConnected(context)) {
            loadingListener.onLoadingStarted();
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);
        } else {
            showErrorDialog(getString(R.string.error_no_network));
        }
    }

    /**
     * This method will be called when forgot password link is clicked.
     */
    @OnClick(R.id.txtForgotPassword)
    public void clickForgotPassword() {
        isForgotPassword = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Forgot Password?");
        builder.setMessage(getString(R.string.message_enter_email_forgot_password));
        View viewInflated = LayoutInflater.from(context).inflate(R.layout.dialog_forgot_password, null, false);
        final TextInputEditText input = (TextInputEditText) viewInflated.findViewById(R.id.edtInputEmailForgotPassword);
        builder.setView(viewInflated);
        builder.setCancelable(false);
        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setNegativeButton(android.R.string.cancel, null);

        final AlertDialog mAlertDialog = builder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        if (input.getText().toString().trim().length() == 0) {
                            input.setError(getString(R.string.error_invalid_email));
                        }else{
                            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                            if (input.getText().toString().trim().matches(emailPattern)) {
                                sendNewPassword(input.getText().toString().trim());
                                mAlertDialog.dismiss();
                            }
                            else {
                                input.setError(getString(R.string.error_invalid_email));
                            }
                        }
                    }
                });
            }
        });
        mAlertDialog.show();
    }

    /**
     * This method is used to call ResetPassword function of AuthService.
     */
    public void sendNewPassword(String email){
        getLoadingListener().onLoadingStarted();
        AuthService authservice = new AuthService();
        authservice.ResetPassword(this, email);
    }

    /**
     * This method will be notified when validation are successful.
     */
    @Override
    public void onValidationSucceeded() {
        login();
    }

    /**
     * This method will be notified when validation are failed.
     */
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * This method fires when any startActivityForResult finishes. The requestCode maps to
     * the value passed into startActivityForResult.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Map<String, String> options = new HashMap<String, String>();
        if (requestCode == REQUEST_HOME_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                isLoggedOut = true;

                AuthService authService = new AuthService();
                authService.LogOut(mAuth);
            } else {
                finish();
            }
        } else if (requestCode == REQUEST_USER_CHAT_ACTIVITY) {
            //temp
            isLoggedOut = true;

            if (resultCode == RESULT_OK) {
                isLoggedOut = true;

                AuthService authService = new AuthService();
                authService.LogOut(mAuth);
            } else {
                finish();
            }
        } else if (requestCode != REQUEST_REGISTER_ACTIVITY) {
            if (requestCode == RC_GOOGLE_LOGIN) {

                strAuthProvider = Constants.AUTH_GOOGLE;

                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();
                    strFullName = account.getDisplayName();
                    strNickName = account.getGivenName();
                    strCountry = "NA";
                    firebaseAuthWithGoogle(account);
                } else {
                    if(!isBackPressed){
                        loadingListener.onLoadingFinished();
                        // Google Sign In failed, update UI appropriately
                        showErrorDialog("Google Sign In failed");
                    }else{
                        isBackPressed = false;
                    }

                }
            } else {
            /* Otherwise, it's probably the request by the Facebook login button, keep track of the session */
                mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /**
     * The email address from email input field.
     *
     * @return The email address from email input field.
     */
    @Override
    public String getEmail() {
        return edtEmail.getText().toString();
    }

    /**
     * The password from password input field.
     *
     * @return The password from password input field.
     */
    @Override
    public String getPassword() {
        return edtPassword.getText().toString();
    }

    /**
     * The progress dialog loading listener
     *
     * @return The progress dialog loading listener.
     */
    @Override
    public LoadingListener getLoadingViewListener() {
        return getLoadingListener();
    }

    /**
     * The FirebaseAuth instance
     *
     * @return The FirebaseAuth instance
     */
    @Override
    public FirebaseAuth getFirebaseAuthObj() {
        return mAuth;
    }

    /**
     * The Firebase DatabaseReference instance
     *
     * @return The Firebase DatabaseReference instance
     */
    @Override
    public DatabaseReference getDatabaseReference() {
        return mDatabase;
    }

    /**
     * This method is used to show error dialog.
     *
     * @param message : An error message need to be shown with alert.
     */
    @Override
    public void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * This method is used to show success dialog.
     *
     * @param message : An success message need to be shown with alert.
     */
    @Override
    public void showSuccessDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * The context of activity.
     *
     * @return Context of activity.
     */
    @Override
    public Context getActivityContext() {
        return context;
    }

    /**
     * The method is used to reset the input fields.
     */
    @Override
    public void resetView() {
        isLoggedInWithEmail = false;
        isLoggedOtherType = false;
        selectedAuthProvider = "";
        edtEmail.setText("");
        edtPassword.setText("");
    }

    /**
     * The method is used to fetch user from firebase database using email address.
     *
     * @return An instance of UserAuth.
     */
    @Override
    public UserAuth getUserFromFirebase(String uid) {
        UserAuth user = (new UserService(context)).GetUserFromUid(uid);
        return user;
    }

    @Override
    public String getSelectedAuthProvider() {
        return selectedAuthProvider;
    }


    /**
     * This method is used to redirect user to Home page once authenticated successfully.
     */
    @Override
    public void goHomePage() {
        //startActivityForResult(HomeActivity.newIntent(this), REQUEST_HOME_ACTIVITY);
        startActivityForResult(UsersChatActivity.newIntent(this), REQUEST_USER_CHAT_ACTIVITY);
    }

    /**
     * This method will get notified when connection with google api is failed.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        showErrorDialog(connectionResult.getErrorMessage());
    }

    /**
     * This method is used to save user's profile information to firebase database when authenticated by google or facebook.
     *
     * @param firebaseUser : An instance of currently logged in firebase user.
     */
    private void saveUserProfileToFirebase(FirebaseUser firebaseUser) {
        UserService userService = new UserService(this);
        userService.SaveUserProfile(firebaseUser, getUser(firebaseUser));
    }

    /**
     * This method is used to save logged in user to shared prefrences
     */
    private void saveUserToPreferences(FirebaseUser firebaseUser) {

        SharedPreferenceServices preferenceServices = new SharedPreferenceServices();
        preferenceServices.saveLoggedInUser(context, getUser(firebaseUser));
        preferenceServices.saveFlag(context, Constants.KEY_IS_LOGGED_IN, true);
        preferenceServices.saveText(context, Constants.SELECTED_AUTH_PROVIDER, selectedAuthProvider);
        preferenceServices.saveText(context, Constants.KEY_LOGGED_IN_USER_ID, firebaseUser.getUid());
    }

    private UserAuth getUser(FirebaseUser firebaseUser){
        UserAuth userAuth = new UserAuth();
        userAuth.UserEmail = firebaseUser.getEmail();
        userAuth.UserDisplayName = strNickName;
        userAuth.AuthProvider = strAuthProvider;
        userAuth.Uid = firebaseUser.getUid();
        userAuth.UserFullName = strFullName;
        userAuth.LocationCountry = strCountry;
        userAuth.DateTime = ZeroDateTimeUtil.getUTCDateTimeAsString();
        userAuth.Status = Constants.STATUS_VERIFIED;
        return userAuth;
    }

    /**
     * This method is used to clear logged in user to shared prefrences
     */
    private void clearUserFromPreferences() {
        SharedPreferenceServices preferenceServices = new SharedPreferenceServices();
        preferenceServices.clearLoggedInUserToPreference(context);
        preferenceServices.saveFlag(context, Constants.KEY_IS_LOGGED_IN, false);

        if(preferenceServices.getText(context, Constants.SELECTED_AUTH_PROVIDER)
                .equals(Constants.AUTH_FACEBOOK)) {
            LoginManager.getInstance().logOut();
        }

        preferenceServices.saveText(context, Constants.SELECTED_AUTH_PROVIDER, "");
    }

    /**
     * This method is used to authenticate user with email and password.
     */
    private void login() {
        isLoggedInWithEmail = true;
        isLoggedOtherType = false;
        selectedAuthProvider = Constants.AUTH_EMAIL;
        AuthService authService = new AuthService();
        authService.Login(this);
    }

    /**
     * This method is used to authenticate user with Facebook Access token.
     *
     * @param token : An access token which is fetched from facebook to access facebook user profile.
     */
    private void firebaseAuthWithFacebook(AccessToken token) {
        isLoggedInWithEmail = false;
        isLoggedOtherType = true;
        selectedAuthProvider = Constants.AUTH_FACEBOOK;
        AuthService authService = new AuthService();
        authService.Login(this, token);
    }

    /**
     * This method is used to authenticate user with Google account.
     *
     * @param acct : An google account token which is used to access user's information with the google account.
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        isLoggedInWithEmail = false;
        isLoggedOtherType = true;
        selectedAuthProvider = Constants.AUTH_GOOGLE;
        AuthService authService = new AuthService();
        authService.Login(this, acct);
    }

}
