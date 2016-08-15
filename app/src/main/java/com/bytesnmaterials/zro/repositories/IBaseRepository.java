package com.bytesnmaterials.zro.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

/**
 * An interface class for BaseRepository.
 *
 * @author mitesh
 * @version 1.0
 * @since 25/7/16
 */
public interface IBaseRepository {

    String getDataUrl(String SubDa);

    FirebaseAuth GetFirebaseAuthRef();

    DatabaseReference GetFirebaseDatabaseRef();
}
