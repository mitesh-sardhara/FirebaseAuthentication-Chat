package com.bytesnmaterials.zro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
public class ChatHubsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    List<HubInfo> listHubsForUser;
    LayoutInflater inflater;
    String loggedInUserUid;

    public ChatHubsListAdapter(Context context, List<HubInfo> listHubsForUser){
        this.context = context;
        this.listHubsForUser = listHubsForUser;

        if(context!= null)
            inflater = LayoutInflater.from(context);

        SharedPreferenceServices preferenceServices = new SharedPreferenceServices();
        loggedInUserUid = preferenceServices.getText(context, Constants.KEY_LOGGED_IN_USER_ID);
    }

    public void cleanUp() {
        listHubsForUser.clear();
    }

    public void refreshList(List<HubInfo> listHubsForUser){
        this.listHubsForUser = listHubsForUser;
        notifyDataSetChanged();
    }

    public void addHub(HubInfo hubInfo){
        listHubsForUser.add(hubInfo);
        ((UsersChatActivity)context).fragmentChats.listHubs.add(hubInfo);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewUser = inflater.inflate(R.layout.row_hubs_list, parent, false);
        viewHolder= new ViewHolderHubs(viewUser);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderHubs viewHolderUser=(ViewHolderHubs)holder;
        HubInfo hub=listHubsForUser.get(position);
        viewHolderUser.getHubNameTextView().setText(hub.Name);
    }

    @Override
    public int getItemCount() {
        return listHubsForUser.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolderHubs extends RecyclerView.ViewHolder {

        TextView txtHubName;

        public ViewHolderHubs(View itemView) {
            super(itemView);
            txtHubName = (TextView)itemView.findViewById(R.id.textViewHubNameRow);
        }

        public TextView getHubNameTextView() {
            return txtHubName;
        }

        public void setHubNameTextView(TextView txtHubName) {
            this.txtHubName = txtHubName;
        }
    }
}
