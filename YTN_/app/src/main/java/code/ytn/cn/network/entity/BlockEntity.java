package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2018/3/30
 */

public class BlockEntity {
    @SerializedName("result_msg")
    private String resultMsg;
    @SerializedName("result_code")
    private String resultCode;
    @SerializedName("status")
    private String status;

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
