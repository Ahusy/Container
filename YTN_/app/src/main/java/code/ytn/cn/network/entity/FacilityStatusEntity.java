package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2018/7/9
 */

public class FacilityStatusEntity extends Entity {
    @SerializedName("c_state")
    private String state;
    @SerializedName("desc")
    private String desc;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("user_phone")
    private String phone;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
