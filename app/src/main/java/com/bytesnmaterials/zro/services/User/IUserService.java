package com.bytesnmaterials.zro.services.User;

import com.bytesnmaterials.zro.models.User;
import com.bytesnmaterials.zro.models.UserAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * An interface class for UserService.
 *
 * @author mitesh
 * @version 1.0
 * @since 22/7/16
 */
public interface IUserService {

    UserAuth GetUserFromUid(String email);

    List<UserAuth> GetAllUsers();

    List<UserAuth> GetAllUsersExceptMe(String uid);

    void SaveUserProfile(FirebaseUser mFirebaseUser, UserAuth u);

}
