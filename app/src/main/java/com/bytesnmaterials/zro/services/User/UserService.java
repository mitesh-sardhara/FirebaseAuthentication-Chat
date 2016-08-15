package com.bytesnmaterials.zro.services.User;

import android.content.Context;

import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.models.UserAuth;
import com.bytesnmaterials.zro.repositories.UserRepository;
import com.bytesnmaterials.zro.services.BaseDataService;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.List;

/**
 * A class to deal with created Users with firebase database.
 *
 * @author mitesh
 * @version 1.0
 * @since 22/7/16
 */
public class UserService extends BaseDataService implements IUserService{

    /* An instance of UserRepository to handle firebase database operations. */
    private UserRepository _repository;

    public UserService(Context context) {
        super();
        _repository = new UserRepository(context);
    }

    /**
     * This method is used fetch user's profile using uid/key.
     * This method will be called to save user profile info once you logged in with Google or Facebook.
     * @param uid : uid as key by which user can be fetched from firebase database.
     * @return An instance of UserAuth.
     */
    @Override
    public UserAuth GetUserFromUid(String uid){
        UserAuth user = _repository.GetUserFromFirebase(uid);
        return user;
    }

    @Override
    public List<UserAuth> GetAllUsers() {
        return _repository.GetAllUsers();
    }

    @Override
    public List<UserAuth> GetAllUsersExceptMe(String uid) {
        return _repository.GetAllUsersExceptMe(uid);
    }

    /**
     * This method is used to save authenticated user's profile information to firebase database.
     * This method will be called to save user profile info once you logged in with Google or Facebook.
     * @param mFirebaseUser : An instance of FirebaseUser class which returns currently logged in user
     */
    /*@Override
    public void SaveUserProfile_GoogleOrFacebookSignin(FirebaseUser mFirebaseUser) {
        UserAuth userAuth = new UserAuth();
        userAuth.AuthProvider = (mFirebaseUser.getProviderData()).get(0).getProviderId();
        userAuth.UserEmail = mFirebaseUser.getEmail();
        userAuth.UserFullName = "NA";
        userAuth.UserDisplayName = mFirebaseUser.getDisplayName();
        userAuth.LocationCountry = "NA";
        userAuth.Status = Constants.STATUS_VERIFIED;
        userAuth.DateTime = (new Date()).toString();
        userAuth.Uid = mFirebaseUser.getUid();

        _repository.AddUserAccount(userAuth);
    }*/

    /**
     * This method is used to save registered user's profile information to firebase database.
     * This method will be called to save user profile info when user is registered.
     * @param mFirebaseUser : An instance of FirebaseUser class which returns currently logged in user
     * @param u : An instance of UserAuth with all details which need to be saved for profile.
     */
    @Override
    public void SaveUserProfile(FirebaseUser mFirebaseUser, UserAuth u) {
        UserAuth userAuth = new UserAuth();
        userAuth.AuthProvider = u.AuthProvider;
        userAuth.UserEmail = u.UserEmail;
        userAuth.UserFullName = u.UserFullName;
        userAuth.UserDisplayName = u.UserDisplayName;
        userAuth.LocationCountry = u.LocationCountry;
        userAuth.DateTime = u.DateTime;
        userAuth.Status = u.Status;
        userAuth.Uid = mFirebaseUser.getUid();

        _repository.AddUserAccount(userAuth);
    }

}
