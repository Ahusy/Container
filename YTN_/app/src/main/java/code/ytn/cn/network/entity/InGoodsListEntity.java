package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2018/3/9
 * 调动货物列表
 */

public class InGoodsListEntity {
    @SerializedName("goods_specs")
    private String goodsSpecs;

    public String getGoodsSpecs() {
        return goodsSpecs;
    }

    public void setGoodsSpecs(String goodsSpecs) {
        this.goodsSpecs = goodsSpecs;
    }

    @SerializedName("first_pic")
    private String firstPic;

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }

    @SerializedName("goods_name")
    private String goodsName;
    @SerializedName("transfer_num")
    private String transNum;
    @SerializedName("goods_id")
    private String goodsId;
    @SerializedName("tIn_num")
    private String tinNum;

    private String etText;

    public String getEtText() {
        return etText;
    }

    public void setEtText(String etText) {
        this.etText = etText;
    }

    public String getTinNum() {
        return tinNum;
    }

    public void setTinNum(String tinNum) {
        this.tinNum = tinNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getTransNum() {
        return transNum;
    }

    public void setTransNum(String transNum) {
        this.transNum = transNum;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
}
