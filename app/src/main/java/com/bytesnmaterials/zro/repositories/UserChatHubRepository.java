package com.bytesnmaterials.zro.repositories;

import android.content.Context;

import com.bytesnmaterials.zro.models.HubInfo;

import java.util.List;

/**
 * Created by mitesh on 10/8/16.
 */
public class UserChatHubRepository extends BaseRepository implements IUserChatHubRepository{

    public UserChatHubRepository(Context context) {
        super(context);
    }

    @Override
    public List<HubInfo> GetChatHubListForUser(String Uid) {
        return null;
    }

    @Override
    public void AddChatHubToHubList(HubInfo hubInfo) {

    }

    @Override
    public void AddChatHubListForUid(HubInfo hubInfo) {

    }
}
