package com.bytesnmaterials.zro.features.chat;

import com.bytesnmaterials.zro.models.UserAuth;

/**
 * Created by mitesh on 9/8/16.
 */
public interface IUsersChatView {

    UserAuth getLoggedInUser();

    void GoFromUsers(String receipientId, String recipientName);

    void GoFromChats(String chatId, String chatName);
}
