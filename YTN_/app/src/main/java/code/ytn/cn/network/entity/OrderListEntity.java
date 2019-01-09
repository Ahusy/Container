package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2018/3/14
 */

public class OrderListEntity {

    @SerializedName("order_code")
    private String orderCode;
    @SerializedName("pay_time")
    private String payTime;
    @SerializedName("num")
    private String num;
    @SerializedName("pay_money")
    private String payMoney;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("status")
    private String status;
    @SerializedName("error_status")
    private String errorStatus;
    @SerializedName("pay_type")
    private String payType;

    public String getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getErrorStatusName(){

        return errorStatus;
    }
}
