package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2018/8/10
 */

public class MessageEntity extends Entity{
    @SerializedName("message_list")
    private List<MessageListEntity> listEntities;
    @SerializedName("unreadCount")
    private String unreadCount;
    public class MessageListEntity implements Serializable{
        @SerializedName("id")
        private String msgId;
        @SerializedName("message_title")
        private String msgTitle;
        @SerializedName("message_content")
        private String msgContent;
        @SerializedName("send_time")
        private String sendTime;
        @SerializedName("read_state")
        private String msgCode;
        @SerializedName("message_type")
        private String msgType;

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getMsgTitle() {
            return msgTitle;
        }

        public void setMsgTitle(String msgTitle) {
            this.msgTitle = msgTitle;
        }

        public String getMsgContent() {
            return msgContent;
        }

        public void setMsgContent(String msgContent) {
            this.msgContent = msgContent;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getMsgCode() {
            return msgCode;
        }

        public void setMsgCode(String msgCode) {
            this.msgCode = msgCode;
        }
    }

    public List<MessageListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<MessageListEntity> listEntities) {
        this.listEntities = listEntities;
    }

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }
}
