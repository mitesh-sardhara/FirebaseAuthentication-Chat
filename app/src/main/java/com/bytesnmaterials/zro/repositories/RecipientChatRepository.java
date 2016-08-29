package com.bytesnmaterials.zro.repositories;

import android.content.Context;
import android.util.Log;
import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.features.chat.ChatActivity;
import com.bytesnmaterials.zro.models.ChatHubMember;
import com.bytesnmaterials.zro.models.HubInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by mitesh on 25/8/16.
 */
public class RecipientChatRepository extends BaseRepository implements IRecipientChatRepository{

    private Context context = null;

    public RecipientChatRepository(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public String GetChatIDForRecipient(String user1Id, String user2Id) {

        DatabaseReference mDatabaseReference = GetFirebaseDatabaseRef();
        mDatabaseReference.child(Constants.NODE_RECIPIENT_CHAT).child(user1Id).child(user2Id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("GetChatIDForRecipient", "dataSnapshot = "+dataSnapshot);
                        if(dataSnapshot != null){
                            if(dataSnapshot.getValue() != null){
                                String chatId = (String) dataSnapshot.getValue();
                                Log.e("GetChatIDForRecipient", "chatId = "+chatId);
                                ((ChatActivity)context).RefreshReciepientChat(chatId);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("GetChatIDForRecipient", "GetChatIDForRecipient:onCancelled", databaseError.toException());
                    }
                });
        return null;
    }

    @Override
    public void AddRecipientChatHub(String user1Id, String user2Id, String chatId) {
        Log.e("AddRecipientChatHub", "-------------------------------------");
        Log.e("AddRecipientChatHub", "user1Id = "+user1Id+ " user2Id = "+user2Id + " ChatId = "+chatId);
        DatabaseReference mDatabase = GetFirebaseDatabaseRef();
        mDatabase.child(Constants.NODE_RECIPIENT_CHAT)
                .child(user1Id)
                .child(user2Id)
                .setValue(chatId);
        mDatabase.child(Constants.NODE_RECIPIENT_CHAT)
                .child(user2Id)
                .child(user1Id)
                .setValue(chatId);
    }
}
