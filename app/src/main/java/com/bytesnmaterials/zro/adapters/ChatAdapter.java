package com.bytesnmaterials.zro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.R;
import com.bytesnmaterials.zro.features.chat.ChatActivity;
import com.bytesnmaterials.zro.models.ChatMessage;
import com.bytesnmaterials.zro.services.SharedPreferenceServices;

import java.util.List;

/**
 * Created by mitesh on 8/8/16.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    List<ChatMessage> listChatMessages;
    LayoutInflater inflater;
    String loggedInUserUid;

    private static final int SENDER=0;
    private static final int RECIPIENT=1;

    public ChatAdapter(Context context, List<ChatMessage> listChatMessages){
        this.context = context;
        this.listChatMessages = listChatMessages;
        inflater = LayoutInflater.from(context);

        SharedPreferenceServices preferenceServices = new SharedPreferenceServices();
        loggedInUserUid = preferenceServices.getText(context, Constants.KEY_LOGGED_IN_USER_ID);
    }

    @Override
    public int getItemViewType(int position) {
        if(listChatMessages.get(position).AuthorUserKey.equalsIgnoreCase(loggedInUserUid)){
            Log.e("Adapter", " sender");
            return SENDER;
        }else {
            return RECIPIENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case SENDER:
                View viewSender = inflater.inflate(R.layout.row_chat_view_right, viewGroup, false);
                viewHolder= new ViewHolderSender(viewSender);
                break;
            case RECIPIENT:
                View viewRecipient = inflater.inflate(R.layout.row_chat_view_left, viewGroup, false);
                viewHolder=new ViewHolderRecipient(viewRecipient);
                break;
            default:
                View viewSenderDefault = inflater.inflate(R.layout.row_chat_view_right, viewGroup, false);
                viewHolder= new ViewHolderSender(viewSenderDefault);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()){
            case SENDER:
                ViewHolderSender viewHolderSender=(ViewHolderSender)viewHolder;
                configureSenderView(viewHolderSender,position);
                break;
            case RECIPIENT:
                ViewHolderRecipient viewHolderRecipient=(ViewHolderRecipient)viewHolder;
                configureRecipientView(viewHolderRecipient,position);
                break;
        }

    }

    private void configureSenderView(ViewHolderSender viewHolderSender, int position) {
        ChatMessage senderFireMessage=listChatMessages.get(position);
        viewHolderSender.getSenderMessageTextView().setText(senderFireMessage.Message);
    }

    private void configureRecipientView(ViewHolderRecipient viewHolderRecipient, int position) {
        ChatMessage recipientFireMessage=listChatMessages.get(position);
        viewHolderRecipient.getRecipientMessageTextView().setText(recipientFireMessage.Message);
    }

    @Override
    public int getItemCount() {
        return listChatMessages.size();
    }


    public void refillAdapter(ChatMessage newFireChatMessage){

        /*add new message chat to list*/
        listChatMessages.add(newFireChatMessage);

        /*refresh view*/
        notifyItemInserted(getItemCount()-1);
    }

    public void refillFirsTimeAdapter(List<ChatMessage> newFireChatMessage){

        /*add new message chat to list*/
        listChatMessages.clear();

        listChatMessages.addAll(newFireChatMessage);

        /*refresh view*/
        notifyItemInserted(getItemCount()-1);
    }

    public void cleanUp() {
        listChatMessages.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public class ViewHolderSender extends RecyclerView.ViewHolder {

        TextView mSenderMessageTextView;

        public ViewHolderSender(View itemView) {
            super(itemView);
            mSenderMessageTextView = (TextView) itemView.findViewById(R.id.textViewMessageSender);
        }

        public TextView getSenderMessageTextView() {
            return mSenderMessageTextView;
        }

        public void setSenderMessageTextView(TextView senderMessage) {
            mSenderMessageTextView = senderMessage;
        }
    }

    public class ViewHolderRecipient extends RecyclerView.ViewHolder {

        TextView mRecipientMessageTextView;

        public ViewHolderRecipient(View itemView) {
            super(itemView);
            mRecipientMessageTextView = (TextView) itemView.findViewById(R.id.textViewMessageRecipient);
        }

        public TextView getRecipientMessageTextView() {
            return mRecipientMessageTextView;
        }

        public void setRecipientMessageTextView(TextView recipientMessage) {
            mRecipientMessageTextView = recipientMessage;
        }
    }

}
