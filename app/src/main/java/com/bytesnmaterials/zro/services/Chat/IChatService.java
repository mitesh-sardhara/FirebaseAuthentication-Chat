package com.bytesnmaterials.zro.services.Chat;

import com.bytesnmaterials.zro.models.ChatHubMember;
import com.bytesnmaterials.zro.models.ChatMessage;
import com.bytesnmaterials.zro.models.HubInfo;

import java.util.List;

/**
 * Created by mitesh on 3/8/16.
 */
public interface IChatService {

    void AddChatHub(HubInfo hub);

    HubInfo GetChatHubFromChatId(String chatId);

    List<HubInfo> GetAllChatHub();

    List<HubInfo> GetChatHubListForUser(String Uid);

    void AddHubToChatHubListOfUser(String Uid, HubInfo hubInfo);

    void AddChatMessageToMessageListOfChatHub(String chatId, ChatMessage message);

    void UpdateLastUpdatedAtHub(String chatId, String lastMessage, String lastUpdated);

    List<ChatMessage> GetChatMessageListForChat(String chatId);


    void AddChatMemberToMemberListOfChatHub(String chatId, ChatHubMember chatHubMember);

    List<ChatHubMember> GetChatMemberListForChat(String chatId);

    String GetChatIdForRecipient(String userId1, String userId2);

    void AddChatForRecipient(String userId1, String userId2, String chatId);

}
