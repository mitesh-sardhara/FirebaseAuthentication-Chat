package com.bytesnmaterials.zro.repositories;

import com.bytesnmaterials.zro.models.UserAuth;

import java.util.List;

/**
 * An interface class for UserRepository.
 *
 * @author mitesh
 * @version 1.0
 * @since 25/7/16
 */
public interface IUserRepository {

    void AddUserAccount(UserAuth user);

    UserAuth GetUserFromFirebase(String uid);

    void RemoveUserAccount(String uid);

    List<UserAuth> GetAllUsers();

    List<UserAuth> GetAllUsersExceptMe(String uid);
}
