package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2018/3/9
 * 出库单列表
 */

public class OutStorageListEntity{
    @SerializedName("outOrder_code")
    private String outOrderCode;
    @SerializedName("relation_code")
    private String relationCode;
    @SerializedName("cabinet_name")
    private String cabinetName;
    @SerializedName("cabinet_id")
    private String cabinetId;
    @SerializedName("transfer_time")
    private String transTime;
    @SerializedName("producer_name")
    private String produName;
    @SerializedName("operator_name")
    private String operName;
    @SerializedName("status")
    private String status;
    @SerializedName("out_id")
    private String outId;

    @SerializedName("depot_type")
    private String depotType;

    public String getDepotType() {
        return depotType;
    }

    public void setDepotType(String depotType) {
        this.depotType = depotType;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getOutOrderCode() {
        return outOrderCode;
    }

    public void setOutOrderCode(String outOrderCode) {
        this.outOrderCode = outOrderCode;
    }

    public String getRelationCode() {
        return relationCode;
    }

    public void setRelationCode(String relationCode) {
        this.relationCode = relationCode;
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

    public String getProduName() {
        return produName;
    }

    public void setProduName(String produName) {
        this.produName = produName;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
