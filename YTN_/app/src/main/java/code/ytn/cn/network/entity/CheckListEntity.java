package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2018/3/10
 */

public class CheckListEntity {
    @SerializedName("check_id")
    private String checkId;
    @SerializedName("check_code")
    private String checkCode;
    @SerializedName("relation_id")
    private String relaId;
    @SerializedName("relation_name")
    private String relaName;
    @SerializedName("check_time")
    private String checkTime;
    @SerializedName("operator")
    private String operator;
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getRelaId() {
        return relaId;
    }

    public void setRelaId(String relaId) {
        this.relaId = relaId;
    }

    public String getRelaName() {
        return relaName;
    }

    public void setRelaName(String relaName) {
        this.relaName = relaName;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }
}
