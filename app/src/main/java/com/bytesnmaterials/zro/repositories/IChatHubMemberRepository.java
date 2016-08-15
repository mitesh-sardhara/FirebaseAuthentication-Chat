package com.bytesnmaterials.zro.repositories;

import com.bytesnmaterials.zro.models.ChatHubMember;

import java.util.List;
import java.util.Map;

/**
 * Created by mitesh on 3/8/16.
 */
public interface IChatHubMemberRepository {

    void AddMemberToMemberListOfChat(String chatId, ChatHubMember chatHubMember);

    List<ChatHubMember> GetMemberListForChat(String chatId);

    Map<String, Object> ToMap(ChatHubMember chatHubMember);

}
