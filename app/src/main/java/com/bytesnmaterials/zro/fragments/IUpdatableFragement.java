package com.bytesnmaterials.zro.fragments;

import com.bytesnmaterials.zro.models.HubInfo;
import com.bytesnmaterials.zro.models.User;
import com.bytesnmaterials.zro.models.UserAuth;

import java.util.List;

/**
 * Created by mitesh on 10/8/16.
 */
public interface IUpdatableFragement {

    public void updateUsers();

    public void updateChats();

    public void addHub(HubInfo hub);

    public void addUser(UserAuth userAuth);

    public void reloadFragment();
}
