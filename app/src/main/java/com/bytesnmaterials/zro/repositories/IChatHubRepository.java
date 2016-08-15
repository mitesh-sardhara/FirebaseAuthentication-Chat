package com.bytesnmaterials.zro.repositories;

import com.bytesnmaterials.zro.models.HubInfo;

import java.util.List;

/**
 * Created by mitesh on 3/8/16.
 */
public interface IChatHubRepository {

    void AddChatHub(HubInfo hubInfo);

    List<HubInfo> GetAllChatHubs();

    HubInfo GetChatHub(String chatId);

    void DeleteChatHub(String chatId);
}
