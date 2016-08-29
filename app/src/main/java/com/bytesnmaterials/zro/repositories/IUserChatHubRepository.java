package com.bytesnmaterials.zro.repositories;

import com.bytesnmaterials.zro.models.HubInfo;

import java.util.List;

/**
 * Created by mitesh on 10/8/16.
 */
public interface IUserChatHubRepository {

    List<HubInfo> GetChatHubListForUser(String Uid);

    void AddChatHubToHubListForUser(String Uid, HubInfo hubInfo);

    void UpdateLastUpdatedToHubListForUser(String Uid, String oponantId, String hubKey, String LastMessage, String LastStamp);

}
