package com.bytesnmaterials.zro.repositories;

import android.content.Context;

import com.bytesnmaterials.zro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This class is used as base class for repositories.
 *
 * @author mitesh
 * @version 1.0
 * @since 25/7/16
 */
public class BaseRepository implements IBaseRepository {

    private Context mContext;

    public BaseRepository(Context context) {
        mContext = context;
    }

    /**
     * This method returns firebase database/repository url.
     * @return Firebase database/repository url.
     * @param SubDa : Sub Repository name from firebase.
     */
    @Override
    public String getDataUrl(String SubDa) {
        if (SubDa.equals(""))
            return mContext.getResources().getString(R.string.firebase_url_Base);
        else
            return mContext.getResources().getString(R.string.firebase_url_Base) + SubDa;
    }

    /**
     * This method returns instance of FirebaseAuth.
     * @return Instance of FirebaseAuth.
     */
    @Override
    public FirebaseAuth GetFirebaseAuthRef() {
        return FirebaseAuth.getInstance();
    }

    /**
     * This method returns instance of Firebase DatabaseReference.
     * @return Instance of DatabaseReference from Firebase.
     */
    @Override
    public DatabaseReference GetFirebaseDatabaseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

}
