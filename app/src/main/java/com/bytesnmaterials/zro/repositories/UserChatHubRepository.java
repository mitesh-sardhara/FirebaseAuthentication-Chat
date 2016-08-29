package com.bytesnmaterials.zro.repositories;

import android.content.Context;
import android.util.Log;

import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.features.chat.ChatActivity;
import com.bytesnmaterials.zro.features.chat.UsersChatActivity;
import com.bytesnmaterials.zro.models.ChatMessage;
import com.bytesnmaterials.zro.models.HubInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitesh on 10/8/16.
 */
public class UserChatHubRepository extends BaseRepository implements IUserChatHubRepository{

    private Context context;

    private List<HubInfo> chatList = null;

    public UserChatHubRepository(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public List<HubInfo> GetChatHubListForUser(String Uid) {
        chatList = new ArrayList<>();
        DatabaseReference mDatabaseReference = GetFirebaseDatabaseRef();
        mDatabaseReference.child(Constants.NODE_USER_CHAT).child(Uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){
                            // Get user value
                            for (DataSnapshot chatMessageSnapshot: dataSnapshot.getChildren()) {
                                HubInfo hubInfo = chatMessageSnapshot.getValue(HubInfo.class);
                                ((UsersChatActivity)context).fragmentChats.listHubs.add(hubInfo);
                            }
                        }
                        Log.e("GetChatHubListForUser", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                        Log.e("GetChatHubListForUser", "size-->"+chatList.size());
                        ((UsersChatActivity)context).fragmentChats.updateChats();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("GetChatHubListForUser", "GetChatHubListForUser:onCancelled", databaseError.toException());
                    }
                });
        return chatList;
    }

    @Override
    public void AddChatHubToHubListForUser(String Uid, HubInfo hubInfo) {
        DatabaseReference mDatabase = GetFirebaseDatabaseRef();
        mDatabase.child(Constants.NODE_USER_CHAT)
                .child(Uid)
                .child(hubInfo.Key)
                .setValue(hubInfo);
    }

    @Override
    public void UpdateLastUpdatedToHubListForUser(String Uid,  String oponantId, String hubKey, String LastMessage, String LastStamp) {
        DatabaseReference mDatabase = GetFirebaseDatabaseRef();
        mDatabase.child(Constants.NODE_USER_CHAT)
                .child(Uid)
                .child(hubKey).child("LastMessage")
                .setValue(LastMessage);
        mDatabase.child(Constants.NODE_USER_CHAT)
                .child(Uid)
                .child(hubKey).child("LastUpdated")
                .setValue(LastStamp);

        mDatabase.child(Constants.NODE_USER_CHAT)
                .child(oponantId)
                .child(hubKey).child("LastMessage")
                .setValue(LastMessage);
        mDatabase.child(Constants.NODE_USER_CHAT)
                .child(oponantId)
                .child(hubKey).child("LastUpdated")
                .setValue(LastStamp);

        ((UsersChatActivity)context).fragmentChats.reloadFragment();
    }

}
