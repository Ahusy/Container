package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2018/3/14
 */

public class OrderQueryDetailListEntity {
    @SerializedName("first_pic")
    private String firstPic;

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }

    @SerializedName("goods_price")
    private String goodsPrice;
    @SerializedName("goods_name")
    private String goodsName;
    @SerializedName("goods_num")
    private String goodsNum;
    @SerializedName("goods_specs")
    private String goodsSpecs;
    @SerializedName("pay_time")
    private String patTime;

    public boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getGoodsSpecs() {
        return goodsSpecs;
    }

    public void setGoodsSpecs(String goodsSpecs) {
        this.goodsSpecs = goodsSpecs;
    }

    public String getPatTime() {
        return patTime;
    }

    public void setPatTime(String patTime) {
        this.patTime = patTime;
    }
}
