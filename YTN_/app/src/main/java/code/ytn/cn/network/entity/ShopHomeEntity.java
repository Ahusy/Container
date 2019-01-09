package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by SHfeihu on 2018/5/15
 */

public class ShopHomeEntity implements Serializable{
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("operating_cabinet")
    private String operCabinet;
    @SerializedName("ytotal_order")
    private String ytotalOrder;
    @SerializedName("ytotal_money")
    private String ytotalMoney;
    @SerializedName("ytotal_goods")
    private String ytotalGoods;
    @SerializedName("torder")
    private String torder;
    @SerializedName("ttotal_money")
    private String totalMoney;
    @SerializedName("t_count")
    private String tCount;
    @SerializedName("yxd_count")
    private String yxdCount;
    @SerializedName("txd_count")
    private String txdCount;

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String gettCount() {
        return tCount;
    }

    public void settCount(String tCount) {
        this.tCount = tCount;
    }

    public String getYxdCount() {
        return yxdCount;
    }

    public void setYxdCount(String yxdCount) {
        this.yxdCount = yxdCount;
    }

    public String getTxdCount() {
        return txdCount;
    }

    public void setTxdCount(String txdCount) {
        this.txdCount = txdCount;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOperCabinet() {
        return operCabinet;
    }

    public void setOperCabinet(String operCabinet) {
        this.operCabinet = operCabinet;
    }

    public String getYtotalOrder() {
        return ytotalOrder;
    }

    public void setYtotalOrder(String ytotalOrder) {
        this.ytotalOrder = ytotalOrder;
    }

    public String getYtotalMoney() {
        return ytotalMoney;
    }

    public void setYtotalMoney(String ytotalMoney) {
        this.ytotalMoney = ytotalMoney;
    }

    public String getYtotalGoods() {
        return ytotalGoods;
    }

    public void setYtotalGoods(String ytotalGoods) {
        this.ytotalGoods = ytotalGoods;
    }

    public String getTorder() {
        return torder;
    }

    public void setTorder(String torder) {
        this.torder = torder;
    }
}
