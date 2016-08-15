package com.bytesnmaterials.zro.repositories;

import android.content.Context;
import android.util.Log;

import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.features.chat.ChatActivity;
import com.bytesnmaterials.zro.features.chat.UsersChatActivity;
import com.bytesnmaterials.zro.models.HubInfo;
import com.bytesnmaterials.zro.models.UserAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitesh on 3/8/16.
 */
public class ChatHubRepository extends BaseRepository implements IChatHubRepository {

    private HubInfo hubInfo= null;

    List<HubInfo> listChatHubs;

    Context context;

    public ChatHubRepository(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void AddChatHub(HubInfo hubInfo) {
        DatabaseReference mDatabase = GetFirebaseDatabaseRef();

        mDatabase.child(Constants.NODE_CHAT_HUBS)
                .child(hubInfo.Key)
                .setValue(hubInfo);
        ((ChatActivity)context).refreshHubInfo(hubInfo);
    }

    @Override
    public List<HubInfo> GetAllChatHubs() {
        listChatHubs = new ArrayList<>();
        DatabaseReference mDatabaseReference = GetFirebaseDatabaseRef();
        mDatabaseReference.child(Constants.NODE_CHAT_HUBS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ((UsersChatActivity)context).fragmentChats.listHubs.clear();
                        // Get user value
                        for (DataSnapshot hubsSnapshot: dataSnapshot.getChildren()) {
                            HubInfo hub = hubsSnapshot.getValue(HubInfo.class);
                            Log.e("Adding chat", "--> "+hub.Name+" stamp:"+hub.stamp+" Key:"+hub.Key);
                            ((UsersChatActivity)context).fragmentChats.listHubs.add(hub);
                        }

                        ((UsersChatActivity)context).fragmentChats.updateChats();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("GetAllChatHubs", "GetAllChatHubs:onCancelled", databaseError.toException());
                    }
                });
        return listChatHubs;
    }

    @Override
    public HubInfo GetChatHub(String chatId) {
        DatabaseReference mDatabaseReference = GetFirebaseDatabaseRef();
        mDatabaseReference.child(Constants.NODE_CHAT_HUBS).child(chatId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get message value
                        hubInfo = dataSnapshot.getValue(HubInfo.class);
                        ((ChatActivity)context).refreshHubInfo(hubInfo);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("GetChatHub", "GetChatHub:onCancelled", databaseError.toException());
                    }
                });
        return hubInfo;
    }

    @Override
    public void DeleteChatHub(String chatId) {

    }


}
