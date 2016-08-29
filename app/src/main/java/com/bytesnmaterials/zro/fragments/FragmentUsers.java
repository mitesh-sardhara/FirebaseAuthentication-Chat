package com.bytesnmaterials.zro.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bytesnmaterials.zro.R;
import com.bytesnmaterials.zro.adapters.UsersAdapter;
import com.bytesnmaterials.zro.base.BaseFragment;
import com.bytesnmaterials.zro.features.chat.UsersChatActivity;
import com.bytesnmaterials.zro.listeners.RecyclerItemClickListener;
import com.bytesnmaterials.zro.models.HubInfo;
import com.bytesnmaterials.zro.models.UserAuth;
import com.bytesnmaterials.zro.services.User.UserService;
import com.bytesnmaterials.zro.util.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mitesh on 9/8/16.
 */
public class FragmentUsers extends BaseFragment implements IUpdatableFragement{

    @BindView(R.id.usersListRecyclerView)
    RecyclerView userListRecyclerView;

    @BindView(R.id.progress_users)
    ProgressBar progressBar;

    UsersAdapter adapterUsers;

    UserService userService;

    public List<UserAuth> listUsers = new ArrayList<>();

    public FragmentUsers() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        ButterKnife.bind(this,view);

        userListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userListRecyclerView.setHasFixedSize(true);
        userListRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        userService = new UserService(getActivity());

        populateList();
        OnListClick();
        getAllUsers();

        return view;
    }

    public void OnListClick(){
        userListRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), userListRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        String selectedUid = listUsers.get(position).Uid;
                        String selectedName = listUsers.get(position).UserDisplayName;
                        ((UsersChatActivity) getActivity()).GoFromUsers(selectedUid, selectedName);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    public void getAllUsers(){
        listUsers.clear();
        userService.GetAllUsersExceptMe(((UsersChatActivity) getActivity()).getLoggedInUser().Uid);
    }


    public void populateList(){
        adapterUsers = new UsersAdapter(getActivity(), listUsers);
        adapterUsers.notifyDataSetChanged();
        userListRecyclerView.setAdapter(adapterUsers);
    }

    @Override
    public void updateUsers() {
        if(progressBar!=null && progressBar.isShown())
            progressBar.setVisibility(View.GONE);

        Log.e("updateUsers:", "Size-->"+listUsers.size());
        if(adapterUsers==null){
            populateList();
        }else{
            adapterUsers.refreshList(listUsers);
        }
    }

    @Override
    public void updateChats() {

    }

    @Override
    public void addHub(HubInfo hub) {

    }

    @Override
    public void addUser(UserAuth userAuth) {
        /*listUsers.add(userAuth);
        Log.e("listUsers", "=>"+listUsers);
        if(adapterUsers==null){
            populateList();
        }else{
            adapterUsers.addUser(userAuth);
        }*/
    }

    @Override
    public void reloadFragment() {

    }
}
