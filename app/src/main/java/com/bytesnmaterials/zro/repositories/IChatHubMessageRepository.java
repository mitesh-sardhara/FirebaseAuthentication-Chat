package com.bytesnmaterials.zro.repositories;

import com.bytesnmaterials.zro.models.ChatMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by mitesh on 3/8/16.
 */
public interface IChatHubMessageRepository {

    void AddMessageToMessageListOfChat(String chatId, ChatMessage chatMessage);

    List<ChatMessage> GetMessageListForChat(String chatId);

    Map<String, Object> ToMap(ChatMessage chatMessage);
}
