package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 用户信息
 */
public class UserEntity extends Entity{
    @SerializedName("user_id")
    private String userId;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("chant_id")
    private String chantId;
    @SerializedName("rank")
    private String rank;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChantId() {
        return chantId;
    }

    public void setChantId(String chantId) {
        this.chantId = chantId;
    }

}
