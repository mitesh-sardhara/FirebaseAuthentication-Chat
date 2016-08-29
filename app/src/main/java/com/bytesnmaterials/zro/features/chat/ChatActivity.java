package com.bytesnmaterials.zro.features.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.R;
import com.bytesnmaterials.zro.adapters.ChatAdapter;
import com.bytesnmaterials.zro.base.BaseActivity;
import com.bytesnmaterials.zro.models.ChatHubMember;
import com.bytesnmaterials.zro.models.ChatMessage;
import com.bytesnmaterials.zro.models.HubInfo;
import com.bytesnmaterials.zro.models.UserAuth;
import com.bytesnmaterials.zro.services.Chat.ChatService;
import com.bytesnmaterials.zro.services.SharedPreferenceServices;
import com.bytesnmaterials.zro.util.ZeroDateTimeUtil;
import com.bytesnmaterials.zro.util.ZeroNetUtil;
import com.bytesnmaterials.zro.util.ZeroUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mitesh on 8/8/16.
 */
public class ChatActivity extends BaseActivity implements IChatView, Validator.ValidationListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.chatRecyclerView)
    RecyclerView recyclerView;

    @NotEmpty
    @BindView(R.id.edtMessage)
    TextInputEditText edtMessage;

    @BindView(R.id.imageViewSend)
    ImageView imageViewSend;

    @Inject
    FirebaseAuth mAuth;

    @Inject
    FirebaseDatabase mFirebaseDatabase;

    @Inject
    DatabaseReference mDatabase;

    ChildEventListener mMessageChatListener;

    Validator validator;

    public Context context;

    private ChatService chatService;

    private UserAuth loggedInUser = null;

    private HubInfo hubInfo;
    //public List<ChatMessage> listChatMessagaes;
    private List<ChatHubMember> listChatHubMembers;

    private ChatAdapter adapterChatList;

    private String selectedChatHubId = "";
    private String selectedChatHubName = "";
    private String selectedOponentUId = "";
    private String selectedOponentName = "";
    private String LastMessage = "";
    private String LastUpdated = "";
    private int chatOpenFrom = -1;

    private boolean isCreatedNewChat = false;

    private SharedPreferenceServices prefServices;
    private boolean isLoadingChat = true;

    private int FLAG_CREATE_OR_RESUME_CHAT = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        getMainComponent().inject(this);
        setSupportActionBar(toolbar);
        validator = new Validator(this);
        validator.setValidationListener(this);
        context = this;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initChat();
    }

    @Override
    public void initChat() {
        prefServices = new SharedPreferenceServices();
        chatService = new ChatService(context);

        loggedInUser = prefServices.getLoggedInUserFromPreference(this);
        chatOpenFrom = getIntent().getIntExtra(Constants.KEY_CHAT_FROM, Constants.FROM_USERS);

        //initialize with empty adapter
        List<ChatMessage>  emptyMessageChat=new ArrayList<ChatMessage>();
        adapterChatList=new ChatAdapter(context, emptyMessageChat);
        recyclerView.setAdapter(adapterChatList);

        loadChat(chatOpenFrom);
    }

    @Override
    public void createNewChat() {

        /*ChatMessage chatMessage = PrepareChatMessageFromInput();

        hubInfo = PrepareHubInfoForNewChat();
        selectedChatHubId = hubInfo.Key;
        selectedChatHubName = hubInfo.Name;
        getSupportActionBar().setTitle(hubInfo.Name);

        List<ChatMessage>  emptyMessageChat=new ArrayList<ChatMessage>();
        emptyMessageChat.add(chatMessage);
        if(adapterChatList.getItemCount() != 0){
            adapterChatList.cleanUp();
        }
        adapterChatList.refillFirsTimeAdapter(emptyMessageChat);
        recyclerView.scrollToPosition(adapterChatList.getItemCount()-1);*/

        //chatService.AddChatMessageToMessageListOfChatHub(selectedChatHubId, chatMessage);
    }

    @Override
    public void addChatMessage() {

        ChatMessage chatMessage = PrepareChatMessageFromInput();
        LastMessage = chatMessage.Message;
        LastUpdated = chatMessage.Stamp;
        if(adapterChatList.getItemCount() == 0){ // adding first message.
            Log.e("addchatMessage", "----adding first message---");
            hubInfo = PrepareHubInfoForNewChat();
            hubInfo.LastMessage = LastMessage;
            hubInfo.LastUpdated = LastUpdated;

            selectedChatHubId = hubInfo.Key;
            selectedChatHubName = hubInfo.Name;

            chatService.AddChatHub(hubInfo);

            ///
            ChatHubMember member1 = new ChatHubMember();
            member1.Key = loggedInUser.Uid;
            member1.UserKey = loggedInUser.Uid;
            member1.UserName = loggedInUser.UserDisplayName;

            chatService.AddChatMemberToMemberListOfChatHub(selectedChatHubId, member1);

            chatService.AddHubToChatHubListOfUser(member1.Key, hubInfo);

            ChatHubMember member2 = new ChatHubMember();
            member2.Key = selectedOponentUId;
            member2.UserKey = selectedOponentUId;
            member2.UserName = selectedOponentName;
            chatService.AddChatMemberToMemberListOfChatHub(selectedChatHubId, member2);

            chatService.AddHubToChatHubListOfUser(member2.Key, hubInfo);
            chatService.AddChatForRecipient(loggedInUser.Uid, selectedOponentUId, hubInfo.Key);

            getSupportActionBar().setTitle(selectedOponentName);
            ///
        }
        refillAdapter(chatMessage);
        chatService.AddChatMessageToMessageListOfChatHub(selectedChatHubId, chatMessage);
        clearEditbox();
    }

    @Override
    public void loadChat(int chatOpenFrom) {
        switch(chatOpenFrom){
            case Constants.FROM_CHATS:
                selectedChatHubId = getIntent().getStringExtra(Constants.KEY_SELECTED_CHAT_ID);
                selectedChatHubName = getIntent().getStringExtra(Constants.KEY_SELECTED_CHAT_NAME);
                Log.e("loadChat:", "-->"+selectedChatHubId+", "+selectedChatHubName );
                loadChatFromChats();
                break;
            case Constants.FROM_USERS:
                selectedOponentName = getIntent().getStringExtra(Constants.KEY_SELECTED_RECIPIENT_NAME);
                selectedOponentUId = getIntent().getStringExtra(Constants.KEY_SELECTED_RECIPIENT_UID);
                getSupportActionBar().setTitle(selectedOponentName);
                loadChatFromUsers();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mMessageChatListener == null){
            addMessageChildListener();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeMessageChildListener();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(hubInfo!=null){
            if(!LastUpdated.equals("") && !LastMessage.equals("")){
                Log.e("=============", "=======onBackPressed========>");
                Log.e("LastUpdated", "=>"+LastUpdated);
                Log.e("LastMessage", "=>"+LastMessage);
                Log.e("loggedInUid", "=>"+loggedInUser.Uid);
                Log.e("selectedOponentUId", "=>"+selectedOponentUId);
                Log.e("hubInfo.Key", "=>"+hubInfo.Key);
                Log.e("=============", "=======onDestroy========>");
                Intent intent = new Intent();
                intent.putExtra("CHAT_ID",hubInfo.Key);
                intent.putExtra("LAST_MESSAGE",LastMessage);
                intent.putExtra("LAST_UPDATED",LastUpdated);
                intent.putExtra("OPONANT",selectedOponentUId);
                setResult(RESULT_OK, intent);
                finish();
            }else{
                finish();
            }
        }else{
            finish();
        }
    }

    @Override
    public void loadChatFromUsers() {
        Log.e("loadChatFromUsers", "selectedOponentUId=>"+selectedOponentUId);
        chatService.GetChatIdForRecipient(loggedInUser.Uid, selectedOponentUId);
    }

    @Override
    public void loadChatFromChats() {
        isLoadingChat = true;
        chatService.GetChatMessageListForChat(selectedChatHubId);
        chatService.GetChatHubFromChatId(selectedChatHubId);
        chatService.GetChatMemberListForChat(selectedChatHubId);
    }

    @Override
    public String getInputMessage() {
        return edtMessage.getText().toString().trim();
    }


    @OnClick(R.id.imageViewSend)
    public void sendClick() {
        if(ZeroNetUtil.isConnected(this)){
            validator.validate();
        } else {
            showErrorDialog(getString(R.string.error_no_network));
        }
    }

    @Override
    public void deleteChatMessage() {

    }
    /**
     * This method is used to show error dialog.
     *
     * @param message : An error message need to be shown with alert.
     */
    @Override
    public void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onValidationSucceeded() {
        addChatMessage();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private ChatMessage PrepareChatMessageFromInput(){
        String stamp = ZeroDateTimeUtil.getUTCDateTimeAsString();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.AuthorUserKey = loggedInUser.Uid;
        chatMessage.AuthorUserName = loggedInUser.UserDisplayName;
        chatMessage.Stamp = stamp;
        chatMessage.MessageId = ""+ZeroDateTimeUtil.getUTCDateTimeAsMillis();
        chatMessage.Message = getInputMessage();

        return chatMessage;
    }

    public void RefreshReciepientChat(String selectedChatId){
        selectedChatHubId = selectedChatId;
        loadChatFromChats();
    }

    private HubInfo PrepareHubInfoForNewChat(){
        HubInfo chatHubInfo = new HubInfo();
        chatHubInfo.Key = ZeroUtil.random();
        chatHubInfo.Name = loggedInUser.UserDisplayName+"-"+selectedOponentName
                + "-" + ZeroDateTimeUtil.getUTCDateTimeAsString();
        chatHubInfo.Type = Constants.CHAT_TYPE_PERSONAL;
        chatHubInfo.stamp = ZeroDateTimeUtil.getUTCDateTimeAsString();
        return chatHubInfo;
    }

    public void refreshHubInfo(HubInfo hubInfo){
        this.hubInfo = hubInfo;
        selectedChatHubId = hubInfo.Key;
        selectedChatHubName = hubInfo.Name;
        //chatService.AddChatMessageToMessageListOfChatHub(selectedChatHubId, PrepareChatMessageFromInput());
    }

    public void refreshMembers(List<ChatHubMember> chatMemberList){
        if(chatMemberList.size()>0){
            for(int i=0;i<chatMemberList.size();i++){
                ChatHubMember member = chatMemberList.get(i);
                if(!member.UserKey.equals(loggedInUser.Uid)){
                    selectedOponentUId = member.UserKey;
                    selectedOponentName = member.UserName;
                    getSupportActionBar().setTitle(selectedOponentName);
                }
            }
        }

    }

    private void refillAdapter(ChatMessage newMessage){
        if(!newMessage.Message.trim().equals("")){
            adapterChatList.refillAdapter(newMessage);
            recyclerView.scrollToPosition(adapterChatList.getItemCount()-1);
        }
    }

    public void refreshRecipientChat(String chatId){
        selectedChatHubId = chatId;

    }

    public void refreshMessgeListForChat(List<ChatMessage> chatMessageList){
        Log.e("refreshMessgeList:", "-->"+chatMessageList.size());
        if(adapterChatList.getItemCount() != 0){
            adapterChatList.cleanUp();
        }

        LastMessage = chatMessageList.get(chatMessageList.size()-1).Message;
        LastUpdated = chatMessageList.get(chatMessageList.size()-1).Stamp;

        adapterChatList=new ChatAdapter(context, chatMessageList);
        recyclerView.setAdapter(adapterChatList);
        recyclerView.scrollToPosition(adapterChatList.getItemCount()-1);
        isLoadingChat = false;
    }

    private void clearEditbox(){
        edtMessage.setText("");
    }

    public void addMessageChildListener(){
        mMessageChatListener = mDatabase.child(Constants.NODE_CHAT_HUB_MESSAGES)
                .child(selectedChatHubId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.e("addMessageListener:", "onChildAdded----x----");
                        if(dataSnapshot.exists()){
                            if(!isLoadingChat){
                                ChatMessage newMessage=dataSnapshot.getValue(ChatMessage.class);
                                if(newMessage.AuthorUserKey != null &&
                                        !newMessage.Message.trim().equals("")){
                                    if(!newMessage.AuthorUserKey.equals(loggedInUser.Uid)){
                                        Log.e("addMessageListener:", "loggedInUser.author:"+loggedInUser.Uid);
                                        Log.e("addMessageListener:", "newMessage.author:"+newMessage.AuthorUserKey);
                                        refillAdapter(newMessage);
                                    }
                                    LastMessage = newMessage.Message;
                                    LastUpdated = newMessage.Stamp;
                                }
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

    public void removeMessageChildListener(){
        if(mMessageChatListener !=null) {
            // Remove listener
            mDatabase.removeEventListener(mMessageChatListener);
        }
    }
}
