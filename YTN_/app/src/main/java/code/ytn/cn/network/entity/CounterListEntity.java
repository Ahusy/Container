package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by dell on 2018/3/9
 */

public class CounterListEntity implements Serializable{
    @SerializedName("cabinet_code")
    private String cabinetCode;
    @SerializedName("cabinet_name")
    private String cabinetName;
    @SerializedName("address")
    private String address;
    @SerializedName("cabinet_id")
    private String cabinetId;
    @SerializedName("status")
    private String status;
    @SerializedName("distance")
    private String distance;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("goods_status")
    private String goodsStatus;
    @SerializedName("goods_num")
    private String goodsNum;
    @SerializedName("open_depot")
    private String openDepot;
    @SerializedName("depot_name")
    private String depotName;

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getOpenDepot() {
        return openDepot;
    }

    public void setOpenDepot(String openDepot) {
        this.openDepot = openDepot;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public String getDistance() {
        return distance;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCabinetCode() {
        return cabinetCode;
    }

    public void setCabinetCode(String cabinetCode) {
        this.cabinetCode = cabinetCode;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(String cabinetId) {
        this.cabinetId = cabinetId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
