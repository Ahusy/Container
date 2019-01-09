package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2018/4/16
 */

public class StatusEntity extends Entity {
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
