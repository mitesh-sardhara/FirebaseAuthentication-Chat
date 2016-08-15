package com.bytesnmaterials.zro.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bytesnmaterials.zro.R;
import com.bytesnmaterials.zro.adapters.ChatHubsListAdapter;
import com.bytesnmaterials.zro.base.BaseFragment;
import com.bytesnmaterials.zro.features.chat.UsersChatActivity;
import com.bytesnmaterials.zro.listeners.RecyclerItemClickListener;
import com.bytesnmaterials.zro.models.HubInfo;
import com.bytesnmaterials.zro.models.UserAuth;
import com.bytesnmaterials.zro.services.Chat.ChatService;
import com.bytesnmaterials.zro.util.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by mitesh on 9/8/16.
 */
public class FragmentChats extends BaseFragment implements IUpdatableFragement{

    @BindView(R.id.chatsListRecyclerView)
    RecyclerView chatsListRecyclerView;

    @BindView(R.id.progress_chats)
    ProgressBar progressBar;

    ChatHubsListAdapter adapterHubs;

    ChatService chatService;

    public List<HubInfo> listHubs = new ArrayList<>();

    public FragmentChats() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        ButterKnife.bind(this,view);

        chatsListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatsListRecyclerView.setHasFixedSize(true);
        chatsListRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        populateList();
        OnListClick();
        getAllHubsForUser();

        return view;
    }

    public void OnListClick(){
        chatsListRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), chatsListRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        String selectedChatId = listHubs.get(position).Key;
                        String selectedChatName = listHubs.get(position).Name;
                        Log.e("onItemClick:", "-->"+selectedChatId+", "+selectedChatName );
                        ((UsersChatActivity) getActivity()).GoFromChats(selectedChatId, selectedChatName);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    public void populateList(){

        adapterHubs = new ChatHubsListAdapter(getActivity(), listHubs);
        adapterHubs.notifyDataSetChanged();
        chatsListRecyclerView.setAdapter(adapterHubs);
    }

    public void getAllHubsForUser(){
        listHubs.clear();
        chatService = new ChatService(getActivity());
        chatService.GetAllChatHub();
        //chatService.GetChatHubListForUser(((UsersChatActivity) getActivity()).getLoggedInUser().Uid);
    }


    @Override
    public void updateUsers() {

    }

    @Override
    public void updateChats() {
        if(progressBar!=null && progressBar.isShown())
            progressBar.setVisibility(View.GONE);
        Log.e("updateChats:", "Size-->"+listHubs.size());
        if(adapterHubs==null){
            populateList();
        }else{
            adapterHubs.refreshList(listHubs);
        }
    }

    @Override
    public void addHub(HubInfo hub) {
        if(adapterHubs==null){
            populateList();
        }else{
            adapterHubs.addHub(hub);
        }
    }

    @Override
    public void addUser(UserAuth userAuth) {

    }
}
