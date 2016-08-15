package com.bytesnmaterials.zro.repositories;

import android.content.Context;
import android.util.Log;

import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.features.chat.UsersChatActivity;
import com.bytesnmaterials.zro.models.UserAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as model class for User firebase repository and handles data operations.
 * This also contains methods to manage user at firebase database.
 *
 * @author mitesh
 * @version 1.0
 * @since 25/7/16
 */
public class UserRepository extends GeoBaseRepository implements IUserRepository{

    UserAuth user = null;
    List<UserAuth> listUsers = null;
    Context context;

    public UserRepository(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * This method is used to push user's profile to firebase database with uid as key.
     * @param user1 : An instance of UserAuth to be pushed to firebase database.
     */
    @Override
    public void AddUserAccount(UserAuth user1){
        Log.e("========","=======AddUserAccount==============");
        Log.e("email","=>"+user1.UserEmail);
        Log.e("username","=>"+user1.UserDisplayName);
        Log.e("fullname","=>"+user1.UserFullName);
        Log.e("country","=>"+user1.LocationCountry);
        Log.e("datetime","=>"+user1.DateTime);
        Log.e("uid","=>"+user1.Uid);
        Log.e("status","=>"+user1.Status);
        Log.e("provider","=>"+user1.AuthProvider);
        Log.e("========","=======AddUserAccount==============");
        DatabaseReference mDatabase = GetFirebaseDatabaseRef();
        mDatabase.child(Constants.NODE_USERS).child(user1.Uid).setValue(user1);
    }

    /**
     * This method is used to get user's profile using email from firebase database.
     * @param uid : uid, which need to be passed as key to fetch user.
     * @return An instance of UserAuth data model class.
     */
    @Override
    public UserAuth GetUserFromFirebase(String uid){
        DatabaseReference mDatabaseReference = GetFirebaseDatabaseRef();
        mDatabaseReference.child(Constants.NODE_USERS).child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        user = dataSnapshot.getValue(UserAuth.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("GetUserFromFirebase", "getUser:onCancelled", databaseError.toException());
                    }
                });
        return user;
    }

    /**
     * This method is used to delete user's profile using email from firebase database.
     * @param uid : uid, which need to be passed as key to delete user.
     * @return An instance of UserAuth data model class.
     */
    @Override
    public void RemoveUserAccount(String uid) {
       //write code here to remove from firebase
    }

    @Override
    public List<UserAuth> GetAllUsers() {
        listUsers = new ArrayList<>();
        DatabaseReference mDatabaseReference = GetFirebaseDatabaseRef();
        mDatabaseReference.child(Constants.NODE_USERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("GetAllUsers", "------------------------------------");
                        Log.e("GetAllUsers", "==>"+dataSnapshot.toString()+" HasChildren:"+dataSnapshot.hasChildren());
                        if(dataSnapshot.hasChildren()){
                            ((UsersChatActivity)context).fragmentUsers.listUsers.clear();
                            // Get user value
                            for (DataSnapshot usersSnapshot: dataSnapshot.getChildren()) {
                                Log.e("usersSnapshot", "--> "+usersSnapshot.toString());
                                UserAuth user = usersSnapshot.getValue(UserAuth.class);
                                Log.e("Adding user", "--> "+user.Uid+" "+user.UserDisplayName);

                                ((UsersChatActivity)context).fragmentUsers.listUsers.add(user);
                            }

                            ((UsersChatActivity)context).fragmentUsers.updateUsers();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("GetAllUsers", "GetAllUsers:onCancelled", databaseError.toException());
                    }
                });

        return listUsers;
    }

    @Override
    public List<UserAuth> GetAllUsersExceptMe(final String uid) {
        listUsers = new ArrayList<>();
        DatabaseReference mDatabaseReference = GetFirebaseDatabaseRef();
        mDatabaseReference.child(Constants.NODE_USERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("GetAllUsersExceptMe", "------------------------------------");
                        Log.e("GetAllUsersExceptMe", "==>"+dataSnapshot.toString()+" HasChildren:"+dataSnapshot.hasChildren());
                        ((UsersChatActivity)context).fragmentUsers.listUsers.clear();
                        if(dataSnapshot.hasChildren()){
                            // Get user value
                            for (DataSnapshot usersSnapshot: dataSnapshot.getChildren()) {
                                UserAuth user = usersSnapshot.getValue(UserAuth.class);
                                if(!user.Uid.equals(uid)){
                                    ((UsersChatActivity)context).fragmentUsers.listUsers.add(user);
                                }
                            }
                            ((UsersChatActivity)context).fragmentUsers.updateUsers();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("GetAllUsersExceptMe", "GetAllUsersExceptMe:onCancelled", databaseError.toException());
                    }
                });
        return listUsers;
    }


}
