
package com.example.sino.model.chatgroup;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.sino.model.chatgroupmember.ChatGroupMember;
import com.example.sino.model.db.ChatMessage;
import com.example.sino.utils.converters.ChatGroupMemberListConverter;
import com.example.sino.utils.converters.ChatMessageConverter;
import com.example.sino.utils.converters.ChatMessageListConverter;
import com.example.sino.utils.converters.LongListConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity (tableName = "chat_group")
public class ChatGroup implements Parcelable {

    @SerializedName("maxMember")
    @Expose
    public Integer maxMember;
    @SerializedName("notifyAct")
    @Expose
    public Boolean notifyAct;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("id")
    @Expose
    @PrimaryKey
    public Integer id;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("chatGroupMembers")
    @Expose
    @TypeConverters({ChatGroupMemberListConverter.class})
    private List<ChatGroupMember> chatGroupMemberList;

    private Long serverGroupId;
    private Boolean privateIs;
    private Integer statusEn;
    @TypeConverters({ChatMessageListConverter.class})
    private List<ChatMessage> chatMessageList;
    @TypeConverters({ChatMessageConverter.class})
    private ChatMessage lastChatMessage;
    private Integer countOfUnreadMessage;
    @TypeConverters(LongListConverter.class)
    private List<Long> notServerGroupIdList;
    private Integer countOfMembers;

    public ChatGroup() {
    }

    protected ChatGroup(Parcel in) {
        if (in.readByte() == 0) {
            maxMember = null;
        } else {
            maxMember = in.readInt();
        }
        byte tmpNotifyAct = in.readByte();
        notifyAct = tmpNotifyAct == 0 ? null : tmpNotifyAct == 1;
        name = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        if (in.readByte() == 0) {
            serverGroupId = null;
        } else {
            serverGroupId = in.readLong();
        }
        byte tmpPrivateIs = in.readByte();
        privateIs = tmpPrivateIs == 0 ? null : tmpPrivateIs == 1;
        if (in.readByte() == 0) {
            statusEn = null;
        } else {
            statusEn = in.readInt();
        }
        if (in.readByte() == 0) {
            countOfUnreadMessage = null;
        } else {
            countOfUnreadMessage = in.readInt();
        }
        if (in.readByte() == 0) {
            countOfMembers = null;
        } else {
            countOfMembers = in.readInt();
        }
    }

    public static final Creator<ChatGroup> CREATOR = new Creator<ChatGroup>() {
        @Override
        public ChatGroup createFromParcel(Parcel in) {
            return new ChatGroup(in);
        }

        @Override
        public ChatGroup[] newArray(int size) {
            return new ChatGroup[size];
        }
    };

    public Integer getMaxMember() {
        return maxMember;
    }

    public void setMaxMember(Integer maxMember) {
        this.maxMember = maxMember;
    }

    public Boolean getNotifyAct() {
        return notifyAct;
    }

    public void setNotifyAct(Boolean notifyAct) {
        this.notifyAct = notifyAct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ChatGroupMember> getChatGroupMemberList() {
        return chatGroupMemberList;
    }

    public void setChatGroupMemberList(List<ChatGroupMember> chatGroupMemberList) {
        this.chatGroupMemberList = chatGroupMemberList;
    }

    public Long getServerGroupId() {
        return serverGroupId;
    }

    public void setServerGroupId(Long serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    public Boolean getPrivateIs() {
        return privateIs;
    }

    public void setPrivateIs(Boolean privateIs) {
        this.privateIs = privateIs;
    }

    public Integer getStatusEn() {
        return statusEn;
    }

    public void setStatusEn(Integer statusEn) {
        this.statusEn = statusEn;
    }

    public List<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }

    public void setChatMessageList(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

    public ChatMessage getLastChatMessage() {
        return lastChatMessage;
    }

    public void setLastChatMessage(ChatMessage lastChatMessage) {
        this.lastChatMessage = lastChatMessage;
    }

    public Integer getCountOfUnreadMessage() {
        return countOfUnreadMessage;
    }

    public void setCountOfUnreadMessage(Integer countOfUnreadMessage) {
        this.countOfUnreadMessage = countOfUnreadMessage;
    }

    public List<Long> getNotServerGroupIdList() {
        return notServerGroupIdList;
    }

    public void setNotServerGroupIdList(List<Long> notServerGroupIdList) {
        this.notServerGroupIdList = notServerGroupIdList;
    }

    public Integer getCountOfMembers() {
        return countOfMembers;
    }

    public void setCountOfMembers(Integer countOfMembers) {
        this.countOfMembers = countOfMembers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (maxMember == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(maxMember);
        }
        parcel.writeByte((byte) (notifyAct == null ? 0 : notifyAct ? 1 : 2));
        parcel.writeString(name);
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        if (status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(status);
        }
        if (serverGroupId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(serverGroupId);
        }
        parcel.writeByte((byte) (privateIs == null ? 0 : privateIs ? 1 : 2));
        if (statusEn == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(statusEn);
        }
        if (countOfUnreadMessage == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(countOfUnreadMessage);
        }
        if (countOfMembers == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(countOfMembers);
        }
    }
}
