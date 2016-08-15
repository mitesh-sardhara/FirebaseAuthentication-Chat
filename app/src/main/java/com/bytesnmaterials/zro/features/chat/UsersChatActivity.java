package com.bytesnmaterials.zro.features.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.R;
import com.bytesnmaterials.zro.adapters.ViewPagerAdapter;
import com.bytesnmaterials.zro.base.BaseActivity;
import com.bytesnmaterials.zro.fragments.FragmentChats;
import com.bytesnmaterials.zro.fragments.FragmentUsers;
import com.bytesnmaterials.zro.models.HubInfo;
import com.bytesnmaterials.zro.models.UserAuth;
import com.bytesnmaterials.zro.services.SharedPreferenceServices;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mitesh on 9/8/16.
 */
public class UsersChatActivity extends BaseActivity implements IUsersChatView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Inject
    DatabaseReference mDatabase;

    public FragmentUsers fragmentUsers;

    public FragmentChats fragmentChats;

    public ViewPagerAdapter adapter;

    private final static int REQUEST_CHAT = 999;

    private ChildEventListener mUsersChildListener = null;

    private ChildEventListener mChatHubsClildListener = null;

    private UserAuth loggedInuser = null;


    public static Intent newIntent(Activity activity) {
        return new Intent(activity, UsersChatActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_chat);
        ButterKnife.bind(this);
        getMainComponent().inject(this);
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tabLayout.setupWithViewPager(viewPager);

        SharedPreferenceServices prefs = new SharedPreferenceServices();
        loggedInuser = prefs.getLoggedInUserFromPreference(this);

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //catch here title and home icon click
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    @SuppressWarnings("deprecation")
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentChats = new FragmentChats();
        fragmentUsers = new FragmentUsers();
        adapter.addFragment(fragmentChats, "CHATS");
        adapter.addFragment(fragmentUsers, "USERS");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public UserAuth getLoggedInUser() {
        SharedPreferenceServices preferenceServices = new SharedPreferenceServices();
        UserAuth user = preferenceServices.getLoggedInUserFromPreference(this);
        return user;
    }

    @Override
    public void GoFromUsers(String receipientId, String receipientName) {
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra(Constants.KEY_CHAT_FROM, Constants.FROM_USERS);
        i.putExtra(Constants.KEY_SELECTED_RECIPIENT_UID, receipientId);
        i.putExtra(Constants.KEY_SELECTED_RECIPIENT_NAME, receipientName);
        startActivityForResult(i, REQUEST_CHAT);
    }

    @Override
    public void GoFromChats(String chatId, String chatName) {
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra(Constants.KEY_CHAT_FROM, Constants.FROM_CHATS);
        i.putExtra(Constants.KEY_SELECTED_CHAT_ID, chatId);
        i.putExtra(Constants.KEY_SELECTED_CHAT_NAME, chatName);
        startActivityForResult(i, REQUEST_CHAT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mChatHubsClildListener == null) {
            addChatHubsClildListener();
        }

        if (mUsersChildListener == null) {
            addUsersClildListener();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mUsersChildListener != null) {
            mDatabase.removeEventListener(mUsersChildListener);
        }

        if (mChatHubsClildListener != null) {
            mDatabase.removeEventListener(mChatHubsClildListener);
        }
    }

    public void addChatHubsClildListener() {
        mChatHubsClildListener = mDatabase.child(Constants.NODE_CHAT_HUBS)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.exists()) {
                            HubInfo hub = dataSnapshot.getValue(HubInfo.class);
                            Log.e("addChildEventListener", "addinghub-------"+hub.Name);
                            fragmentChats.addHub(hub);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void addUsersClildListener() {
        mUsersChildListener = mDatabase.child(Constants.NODE_USERS)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.exists()) {

                            UserAuth user = dataSnapshot.getValue(UserAuth.class);
                            if(!user.Uid.equals(loggedInuser.Uid)){
                                Log.e("addChildEventListener", "addinguser---------"+user.UserDisplayName);
                                fragmentUsers.addUser(user);
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
