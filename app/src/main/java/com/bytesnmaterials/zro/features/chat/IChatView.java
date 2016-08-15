package com.bytesnmaterials.zro.features.chat;

import com.bytesnmaterials.zro.models.ChatMessage;

import java.util.List;

/**
 * Created by mitesh on 8/8/16.
 */
public interface IChatView {

    void initChat();

    void createNewChat();

    void loadChat(int fromKey);

    void loadChatFromUsers();

    void loadChatFromChats();

    String getInputMessage();

    void addChatMessage();

    void deleteChatMessage();

    void showErrorDialog(String message);
}
