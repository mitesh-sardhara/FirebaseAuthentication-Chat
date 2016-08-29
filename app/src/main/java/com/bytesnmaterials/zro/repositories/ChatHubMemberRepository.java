package com.bytesnmaterials.zro.repositories;

import android.content.Context;
import android.util.Log;

import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.features.chat.ChatActivity;
import com.bytesnmaterials.zro.models.ChatHubMember;
import com.bytesnmaterials.zro.models.ChatMessage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mitesh on 3/8/16.
 */
public class ChatHubMemberRepository extends BaseRepository implements IChatHubMemberRepository {

    private List<ChatHubMember> chatHubMemberList = null;

    private Context context = null;

    public ChatHubMemberRepository(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void AddMemberToMemberListOfChat(String chatId, ChatHubMember chatHubMember) {

        DatabaseReference mDatabase = GetFirebaseDatabaseRef();
        mDatabase.child(Constants.NODE_CHAT_HUB_MEMBERS)
                .child(chatId)
                .child(chatHubMember.Key)
                .setValue(chatHubMember);
    }

    @Override
    public List<ChatHubMember> GetMemberListForChat(String chatId) {
        chatHubMemberList = new ArrayList<>();
        DatabaseReference mDatabaseReference = GetFirebaseDatabaseRef();
        mDatabaseReference.child(Constants.NODE_CHAT_HUB_MEMBERS).child(chatId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChildren()){
                            // Get user value
                            for (DataSnapshot chatMemberSnapshot: dataSnapshot.getChildren()) {
                                ChatHubMember chatHubMember = chatMemberSnapshot.getValue(ChatHubMember.class);
                                chatHubMemberList.add(chatHubMember);
                                Log.e("GetMemberListForChat", "UserName-->"+chatHubMember.UserName);
                            }
                        }
                        Log.e("GetMemberListForChat", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                        Log.e("GetMemberListForChat", "size-->"+chatHubMemberList.size());
                        ((ChatActivity)context).refreshMembers(chatHubMemberList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("GetMemberListForChat", "GetMemberListForChat:onCancelled", databaseError.toException());
                    }
                });
        return chatHubMemberList;
    }

    @Override
    public Map<String, Object> ToMap(ChatHubMember chatHubMember) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Key", chatHubMember.Key);
        result.put("UserKey", chatHubMember.UserKey);
        result.put("UserName", chatHubMember.UserName);

        return result;
    }
}
