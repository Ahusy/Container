package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/8/22
 */

public class ChannelEntity extends Entity {

    @SerializedName("channel_list")
    private List<ChannelListEntity> listEntities;

    public List<ChannelListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<ChannelListEntity> listEntities) {
        this.listEntities = listEntities;
    }

    public static class ChannelListEntity{

        @SerializedName("channel")
        private String channel;

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @SerializedName("name")

        private String name;
        @SerializedName("pay_name")
        private String payName;

        public String getPayName() {
            return payName;
        }

        public void setPayName(String payName) {
            this.payName = payName;
        }
    }

}
