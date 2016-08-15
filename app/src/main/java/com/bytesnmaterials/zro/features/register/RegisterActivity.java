package com.bytesnmaterials.zro.features.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.R;
import com.bytesnmaterials.zro.base.BaseActivity;
import com.bytesnmaterials.zro.listeners.LoadingListener;
import com.bytesnmaterials.zro.models.UserAuth;
import com.bytesnmaterials.zro.repositories.GeoBaseRepository;
import com.bytesnmaterials.zro.repositories.UserRepository;
import com.bytesnmaterials.zro.services.AuthService;
import com.bytesnmaterials.zro.services.User.UserService;
import com.bytesnmaterials.zro.util.ZeroDateTimeUtil;
import com.bytesnmaterials.zro.util.ZeroNetUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This class is used as the container of Registration screen.
 * This contains the functionality regarding creating user account and save profile information to firebase.
 *
 * @author jehandad.kamal
 * @version 1.0
 * @since 7/4/2016
 */
public class RegisterActivity extends BaseActivity
        implements Validator.ValidationListener, IRegisterView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @NotEmpty
    @Length(min = 4, max = 30, trim = true)
    @BindView(R.id.edtUsername)
    TextInputEditText edtUsername;

    @NotEmpty
    @Length(min = 4, max = 30, trim = true)
    @BindView(R.id.edtFullName)
    TextInputEditText edtFullname;

    @NotEmpty
    @Length(trim = true)
    @BindView(R.id.edtEmail)
    @Email
    TextInputEditText edtEmail;

    @NotEmpty
    @BindView(R.id.edtPassword)
    @Password(min = 6, scheme = Password.Scheme.ALPHA)
    TextInputEditText edtPassword;

    @NotEmpty
    @BindView(R.id.edtRepeatPassword)
    @ConfirmPassword
    TextInputEditText edtRepeatPassword;

    @NotEmpty
    @BindView(R.id.edtCountry)
    public AutoCompleteTextView edtCountry;

    @Inject
    FirebaseAuth mAuth;

    @Inject
    FirebaseDatabase mFirebaseDatabase;

    @Inject
    DatabaseReference mDatabase;

    private AlertDialog alertDialog = null;

    /* Debug Tag for use logging debug output to LogCat */
    private static final String TAG = RegisterActivity.class.getSimpleName();

    /* Listener for Firebase session changes */
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Validator validator;

    public Context context;

    private AuthService authService;

    /* A flag is used to check with auth listener when new user created with email and password */
    public boolean isUserCreated = false;

    private LoadingListener loadingListener= null;

    public static Intent newIntent(Activity act) {
        return new Intent(act, RegisterActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        getMainComponent().inject(this);
        setSupportActionBar(toolbar);
        context = this;
        validator = new Validator(this);
        validator.setValidationListener(this);

        initialize();
    }

    /**
     * This method is used for initialization process of Firebase Authentication Listener, which notifies users when firebase auth state changes..
     */
    private void initialize() {
        loadingListener = getLoadingListener();
        authService = new AuthService();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(isUserCreated){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        Log.e(TAG, "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                        // Save user's other profile information to firebase
                        saveUserProfile(firebaseUser);
                        Log.e(TAG, "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                    } else {
                        // User is signed out
                        Log.e(TAG, "onAuthStateChanged:signed_out");
                        showSuccessDialog(getString(R.string.message_user_registered));
                    }
                }
            }
        };

        // An array adapter for Autocomplete Text view used as input for country.
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.countries_array));
        edtCountry.setAdapter(adapter);

        // An initializations of GeoBaseRepository which is used to perform network operation to fetch country from device's ip.
        GeoBaseRepository repository = new GeoBaseRepository(this);
        // Calling a method to get country from device ip.
        repository.getCountryFromIp(loadingListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @OnClick(R.id.btnRegister)
    public void clickRegister() {
        if(ZeroNetUtil.isConnected(getBaseContext())){
            validator.validate();
        }else{
            showErrorDialog(getString(R.string.error_no_network));
        }
    }

    /**
     * This method will be notified when validation are successful.
     */
    @Override
    public void onValidationSucceeded() {
        createUser();
    }

    /**
     * This method is used to create user with email and password for firebase authentication.
     */
    private void createUser() {
        isUserCreated = true;
        loadingListener.onLoadingStarted();
        authService.Register(this);
    }

    /**
     * This method is notified when validation fails.
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
     * Used to show errors to users.
     *
     * @param message : An error message.
     */
    @Override
    public void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Used to show success dialog, when used is registered with firebase successfully.
     *
     * @param message : A success message when user is created successfully.
     */
    @Override
    public void showSuccessDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        finish();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * This method is used to save the profile of created firebase user to firebase database.
     * This method also sends new password link to user's email.
     *
     * @param firebaseUser : A newly created firebase user with email and password.
     */
    private void saveUserProfile(FirebaseUser firebaseUser){

        UserAuth userAuthtemp = getUserFromInputValue();

        UserService userService = new UserService(this);
        userService.SaveUserProfile(firebaseUser, userAuthtemp);

        authService.LogOut(mAuth);
    }

    /**
     * The full name from input field.
     * @return The full name from input field.
     */
    @Override
    public String getUserFullName() {
        return edtFullname.getText().toString();
    }

    /**
     * The nick name from input field.
     *
     * @return The nick name from input field.
     */
    @Override
    public String getUserNickName() {
        return edtUsername.getText().toString();
    }

    /**
     * The user's email name from input field.
     *
     * @return The user's email name from input field.
     */
    @Override
    public String getUserEmail() {
        return edtEmail.getText().toString();
    }

    /**
     * The password input field.
     *
     * @return The password input field.
     */
    @Override
    public String getUserPassword() {
        return edtPassword.getText().toString();
    }

    /**
     * The password from confirm password input field.
     *
     * @return The password from confirm password input field.
     */
    @Override
    public String getUserConfirmPassword() {
        return edtRepeatPassword.getText().toString();
    }

    /**
     * The country from input field.
     *
     * @return The country from input field.
     */
    @Override
    public String getUserCountry() {
        return edtCountry.getText().toString();
    }

    /**
     * The registration date from input field.
     *
     * @return The registration date from input field.
     */
    @Override
    public String getRegisterDateTime() {
        return ZeroDateTimeUtil.getUTCDateTimeAsString();
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
     * The instance of UserAuth to write to firebase database.
     *
     * @return The instance of UserAuth to write to firebase database.
     */
    @Override
    public UserAuth getUserFromInputValue() {
        UserAuth user = new UserAuth();
        user.UserEmail = getUserEmail();
        user.LocationCountry = getUserCountry();
        user.UserDisplayName = getUserNickName();
        user.UserFullName = getUserFullName();
        user.AuthProvider = Constants.AUTH_EMAIL;
        user.Uid = "NA";
        user.Status = Constants.STATUS_PENDING;
        user.DateTime = getRegisterDateTime();

        return user;
    }

    /**
     * The instance of ProgressDialog LoadingListener.
     *
     * @return The instance of ProgressDialog LoadingListener.
     */
    @Override
    public LoadingListener getLoadingViewListener() {
        return getLoadingListener();
    }

    /**
     * The instance of FirebaseAuth to create user at firebase.
     *
     * @return The instance of FirebaseAuth to create user at firebase.
     */
    @Override
    public FirebaseAuth getFirebaseAuthObj() {
        return mAuth;
    }

    @Override
    protected void onDestroy() {

        if(alertDialog!= null){
            if(alertDialog.isShowing()){
                alertDialog.dismiss();
            }
        }
        super.onDestroy();
    }

}
