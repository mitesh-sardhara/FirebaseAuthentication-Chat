package com.bytesnmaterials.zro.services.Chat;

import android.content.Context;

import com.bytesnmaterials.zro.models.ChatHubMember;
import com.bytesnmaterials.zro.models.ChatMessage;
import com.bytesnmaterials.zro.models.HubInfo;
import com.bytesnmaterials.zro.repositories.ChatHubMemberRepository;
import com.bytesnmaterials.zro.repositories.ChatHubMessageRepository;
import com.bytesnmaterials.zro.repositories.ChatHubRepository;
import com.bytesnmaterials.zro.repositories.RecipientChatRepository;
import com.bytesnmaterials.zro.repositories.UserChatHubRepository;
import com.bytesnmaterials.zro.services.BaseDataService;

import java.util.List;

/**
 * Created by mitesh on 3/8/16.
 */
public class ChatService extends BaseDataService implements IChatService {

    private ChatHubRepository _chatHubRepository;

    private ChatHubMemberRepository _chatMemberRepository;

    private ChatHubMessageRepository _chatMessageRepository;

    private UserChatHubRepository _userChatHubRepository;

    private RecipientChatRepository _recipientChatRepository;

    public ChatService(Context context){
        super();
        _chatHubRepository = new ChatHubRepository(context);
        _chatMemberRepository = new ChatHubMemberRepository(context);
        _chatMessageRepository = new ChatHubMessageRepository(context);
        _userChatHubRepository = new UserChatHubRepository(context);
        _recipientChatRepository = new RecipientChatRepository(context);
    }

    @Override
    public void AddChatHub(HubInfo hub) {
        _chatHubRepository.AddChatHub(hub);
    }

    @Override
    public void AddChatMessageToMessageListOfChatHub(String chatId, ChatMessage message) {
        _chatMessageRepository.AddMessageToMessageListOfChat(chatId, message);
    }

    @Override
    public void UpdateLastUpdatedAtHub(String chatId, String lastMessage, String lastUpdated) {
        _chatHubRepository.UpdateChatHubLastUpdated(chatId, lastMessage, lastUpdated);
    }

    public void UpdateChatHubAtUserChat(String Uid, String oponantId, String chatId, String lastMessage, String lastUpdated) {
        _userChatHubRepository.UpdateLastUpdatedToHubListForUser(Uid, oponantId, chatId, lastMessage, lastUpdated);
    }

    @Override
    public List<ChatMessage> GetChatMessageListForChat(String chatId) {
        return _chatMessageRepository.GetMessageListForChat(chatId);
    }

    @Override
    public void AddChatMemberToMemberListOfChatHub(String chatId, ChatHubMember chatHubMember) {
        _chatMemberRepository.AddMemberToMemberListOfChat(chatId, chatHubMember);
    }

    @Override
    public List<ChatHubMember> GetChatMemberListForChat(String chatId) {
        return _chatMemberRepository.GetMemberListForChat(chatId);
    }

    @Override
    public String GetChatIdForRecipient(String userId1, String userId2) {
        return _recipientChatRepository.GetChatIDForRecipient(userId1, userId2);
    }

    @Override
    public void AddChatForRecipient(String userId1, String userId2, String chatId) {
        _recipientChatRepository.AddRecipientChatHub(userId1, userId2, chatId);
    }

    @Override
    public HubInfo GetChatHubFromChatId(String chatId) {
        return _chatHubRepository.GetChatHub(chatId);
    }

    @Override
    public List<HubInfo> GetAllChatHub() {
        return _chatHubRepository.GetAllChatHubs();
    }

    @Override
    public List<HubInfo> GetChatHubListForUser(String Uid) {
        return _userChatHubRepository.GetChatHubListForUser(Uid);
    }

    @Override
    public void AddHubToChatHubListOfUser(String Uid, HubInfo hubInfo) {
        _userChatHubRepository.AddChatHubToHubListForUser(Uid, hubInfo);
    }

}
