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
import com.bytesnmaterials.zro.features.chat.UsersChatActivity;
import com.bytesnmaterials.zro.models.HubInfo;
import com.bytesnmaterials.zro.models.UserAuth;
import com.bytesnmaterials.zro.services.SharedPreferenceServices;

import java.util.List;

/**
 * Created by mitesh on 9/8/16.
 */
public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    List<UserAuth> listUserAuth;
    LayoutInflater inflater;
    String loggedInUserUid;

    public UsersAdapter(Context context, List<UserAuth> listUserAuth){
        this.context = context;
        this.listUserAuth = listUserAuth;
        inflater = LayoutInflater.from(context);

        SharedPreferenceServices preferenceServices = new SharedPreferenceServices();
        loggedInUserUid = preferenceServices.getText(context, Constants.KEY_LOGGED_IN_USER_ID);
    }

    public void refreshList(List<UserAuth> listUserAuth){
        this.listUserAuth = listUserAuth;
        notifyDataSetChanged();
    }

    public void cleanUp() {
        listUserAuth.clear();
    }

    public void addUser(UserAuth userAuth){
        listUserAuth.add(userAuth);
        ((UsersChatActivity)context).fragmentUsers.listUsers.add(userAuth);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewUser = inflater.inflate(R.layout.row_users_list, parent, false);
        viewHolder= new ViewHolderUser(viewUser);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderUser viewHolderUser=(ViewHolderUser)holder;
        UserAuth user=listUserAuth.get(position);
        viewHolderUser.getUserNameTextView().setText(user.UserDisplayName);
    }

    @Override
    public int getItemCount() {
        return listUserAuth.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolderUser extends RecyclerView.ViewHolder {

        TextView txtUserName;

        public ViewHolderUser(View itemView) {
            super(itemView);
            txtUserName = (TextView)itemView.findViewById(R.id.textViewUserNameRow);
        }

        public TextView getUserNameTextView() {
            return txtUserName;
        }

        public void setUserNameTextView(TextView txtUserName) {
            this.txtUserName = txtUserName;
        }
    }
}
