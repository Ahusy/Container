package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SHfeihu on 2018/5/15
 */

public class ShopHomeListEntity {
    @SerializedName("store_list")
    private List<ShopHomeEntity> shopHomeEntities;
    @SerializedName("cabinet_list")
    private List<CounterListEntity> counterListEntities;

    public List<CounterListEntity> getCounterListEntities() {
        return counterListEntities;
    }

    public void setCounterListEntities(List<CounterListEntity> counterListEntities) {
        this.counterListEntities = counterListEntities;
    }

    @SerializedName("result_code")
    private String resultCode;
    @SerializedName("result_msg")
    private String resultMsg;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public List<ShopHomeEntity> getShopHomeEntities() {
        return shopHomeEntities;
    }

    public void setShopHomeEntities(List<ShopHomeEntity> shopHomeEntities) {
        this.shopHomeEntities = shopHomeEntities;
    }
}
