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


    void AddChatMessageToMessageListOfChatHub(String chatId, ChatMessage message);

    List<ChatMessage> GetChatMessageListForChat(String chatId);


    void AddChatMemberToMemberListOfChatHub(String chatId, ChatHubMember chatHubMember);


    List<ChatHubMember> GetChatMemberListForChat(String chatId);

}
