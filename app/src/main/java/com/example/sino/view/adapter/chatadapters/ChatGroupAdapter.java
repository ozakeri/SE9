package com.example.sino.view.adapter.chatadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sino.R;
import com.example.sino.model.chatgroup.ChatGroup;
import com.example.sino.utils.common.HejriUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ChatGroupAdapter extends RecyclerView.Adapter<ChatGroupAdapter.CustomView> {

    private List<ChatGroup> chatGroupList = new ArrayList<>();

    public ChatGroupAdapter(List<ChatGroup> chatGroupList) {
        this.chatGroupList = chatGroupList;
    }

    private static onIemClickListener clickListener;

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_group_item, parent, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {

        ChatGroup chatGroup = chatGroupList.get(position);

        System.out.println("ChatGroupAdapter=======" + chatGroup.name);

        if (chatGroup != null) {

            holder.groupVT.setText(chatGroup.getName());

            if (chatGroup.getPrivateIs() != null && chatGroup.getPrivateIs()) {
                holder.groupIcon.setBackgroundResource(R.drawable.ic_avatar);
            } else {
                holder.groupIcon.setBackgroundResource(R.drawable.ic_avatar);
            }

            if (chatGroup.getCountOfUnreadMessage() != null) {
                if (chatGroup.getCountOfUnreadMessage() == 0) {
                    holder.counterVT.setVisibility(View.INVISIBLE);
                } else {
                    holder.counterVT.setText(String.valueOf(chatGroup.getCountOfUnreadMessage()));
                }
            } else {
                holder.counterVT.setVisibility(View.INVISIBLE);
            }


            if (chatGroup.getLastChatMessage() != null) {

                String messageSummary = "";
                String senderName = "";
                String displayDate = "";

                messageSummary = chatGroup.getLastChatMessage().getMessage();

                if (messageSummary != null) {
                    if (messageSummary.contains("\n")) {
                        messageSummary = messageSummary.substring(0, messageSummary.indexOf("\n"));
                    }
                    messageSummary = messageSummary.length() > 50 ? messageSummary.substring(0, 50) : messageSummary;
                } else {
                    messageSummary = "sendingFile";
                }

                if (chatGroup.getLastChatMessage().getSenderAppUser() != null) {
                    senderName = (chatGroup.getLastChatMessage().getSenderAppUser().getName() + " " + chatGroup.getLastChatMessage().getSenderAppUser().getFamily());
                } else {
                    senderName = chatGroup.getLastChatMessage().getSenderAppUserId().toString();
                }

                if (chatGroup.getLastChatMessage().getSendDate() != null) {
                    displayDate = HejriUtil.chrisToHejriDateTime(Timestamp.valueOf(chatGroup.getLastChatMessage().getSendDate()));
                } else if (chatGroup.getLastChatMessage().getDateCreation() != null) {
                    displayDate = HejriUtil.chrisToHejriDateTime(Timestamp.valueOf(chatGroup.getLastChatMessage().getDateCreation()));

                }
                holder.nameVT.setText(senderName + " " + ":");
                holder.messageVT.setText(messageSummary);
                holder.dateVT.setText(displayDate);
            }


        }

    }

    @Override
    public int getItemCount() {
        return chatGroupList.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView groupVT, counterVT, nameVT, messageVT, dateVT;
        ImageView groupIcon;

        public CustomView(@NonNull View view) {
            super(view);
            groupVT = (TextView) view.findViewById(R.id.group_VT);
            counterVT = (TextView) view.findViewById(R.id.counter_VT);
            nameVT = (TextView) view.findViewById(R.id.name_VT);
            messageVT = (TextView) view.findViewById(R.id.message_VT);
            dateVT = (TextView) view.findViewById(R.id.date_VT);
            groupIcon = view.findViewById(R.id.groupIcon);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(onIemClickListener clickListener) {
        ChatGroupAdapter.clickListener = clickListener;
    }

    public interface onIemClickListener {
        void onItemClick(int position, View view);
    }
}
