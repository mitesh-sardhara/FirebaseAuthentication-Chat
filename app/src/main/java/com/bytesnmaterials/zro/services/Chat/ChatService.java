package com.bytesnmaterials.zro.services.Chat;

import android.content.Context;

import com.bytesnmaterials.zro.models.ChatHubMember;
import com.bytesnmaterials.zro.models.ChatMessage;
import com.bytesnmaterials.zro.models.HubInfo;
import com.bytesnmaterials.zro.repositories.ChatHubMemberRepository;
import com.bytesnmaterials.zro.repositories.ChatHubMessageRepository;
import com.bytesnmaterials.zro.repositories.ChatHubRepository;
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

    public ChatService(Context context){
        super();
        _chatHubRepository = new ChatHubRepository(context);
        _chatMemberRepository = new ChatHubMemberRepository(context);
        _chatMessageRepository = new ChatHubMessageRepository(context);
        _userChatHubRepository = new UserChatHubRepository(context);
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

}
