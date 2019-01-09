package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2018/3/9
 * 入库单列表
 */

public class InOrderListEntity {
    @SerializedName("inOrder_code")
    private String inOrderCode;
    @SerializedName("in_id")
    private String inId;
    @SerializedName("cabinet_name")
    private String cabinetName;
    @SerializedName("cabinet_id")
    private String cabinetId;
    @SerializedName("transfer_time")
    private String transTime;
    @SerializedName("producer_name")
    private String producerName;
    @SerializedName("status")
    private String status;
    @SerializedName("relation_code")
    private String relaCode;

    public String getRelaCode() {
        return relaCode;
    }

    public void setRelaCode(String relaCode) {
        this.relaCode = relaCode;
    }

    public String getInOrderCode() {
        return inOrderCode;
    }

    public void setInOrderCode(String inOrderCode) {
        this.inOrderCode = inOrderCode;
    }

    public String getInId() {
        return inId;
    }

    public void setInId(String inId) {
        this.inId = inId;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }

    public String getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(String cabinetId) {
        this.cabinetId = cabinetId;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
