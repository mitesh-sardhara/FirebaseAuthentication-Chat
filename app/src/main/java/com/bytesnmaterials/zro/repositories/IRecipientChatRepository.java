package com.bytesnmaterials.zro.repositories;

import com.bytesnmaterials.zro.models.ChatMessage;
import com.bytesnmaterials.zro.models.HubInfo;

import java.util.List;

/**
 * Created by mitesh on 25/8/16.
 */
public interface IRecipientChatRepository {

    String GetChatIDForRecipient(String user1Id, String user2Id);

    void AddRecipientChatHub(String user1Id, String user2Id, String chatId);

}
