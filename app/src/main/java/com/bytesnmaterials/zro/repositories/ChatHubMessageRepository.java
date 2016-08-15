package com.bytesnmaterials.zro.repositories;

import android.content.Context;
import android.util.Log;

import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.features.chat.ChatActivity;
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
public class ChatHubMessageRepository extends BaseRepository implements IChatHubMessageRepository{

    private Context context;

    private List<ChatMessage> chatMessageList = null;

    private ChatMessage message = null;

    public ChatHubMessageRepository(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void AddMessageToMessageListOfChat(String chatId, final ChatMessage chatMessage) {

        DatabaseReference mDatabase = GetFirebaseDatabaseRef();
        mDatabase.child(Constants.NODE_CHAT_HUB_MESSAGES)
                .child(chatId)
                .child(chatMessage.MessageId)
                .setValue(chatMessage);
    }

    @Override
    public List<ChatMessage> GetMessageListForChat(String chatId) {
        chatMessageList = new ArrayList<>();
        DatabaseReference mDatabaseReference = GetFirebaseDatabaseRef();
        mDatabaseReference.child(Constants.NODE_CHAT_HUB_MESSAGES).child(chatId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){
                            // Get user value
                            for (DataSnapshot chatMessageSnapshot: dataSnapshot.getChildren()) {
                                ChatMessage chatMessage = chatMessageSnapshot.getValue(ChatMessage.class);
                                chatMessageList.add(chatMessage);
                            }
                        }
                        Log.e("GetMessageListForChat", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                        Log.e("GetMessageListForChat", "size-->"+chatMessageList.size());
                        ((ChatActivity)context).refreshMessgeListForChat(chatMessageList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("GetMessageListForChat", "GetMessageListForChat:onCancelled", databaseError.toException());
                    }
                });
        return chatMessageList;
    }

    @Override
    public Map<String, Object> ToMap(ChatMessage chatMessage) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("MessageId", chatMessage.MessageId);
        result.put("Message", chatMessage.Message);
        result.put("AuthorUserName", chatMessage.AuthorUserName);
        result.put("AuthorUserKey", chatMessage.AuthorUserKey);
        result.put("Stamp", chatMessage.Stamp);

        return result;
    }
}
