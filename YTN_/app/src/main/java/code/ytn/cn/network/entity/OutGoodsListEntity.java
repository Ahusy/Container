package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by dell on 2018/3/9
 * 出库调动货物列表
 */

public class OutGoodsListEntity implements Serializable{
    @SerializedName("first_pic")
    private String firstPic;

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }

    @SerializedName("goods_id")
    private String goodsId;
    @SerializedName("goods_name")
    private String goodsName;
    @SerializedName("out_num")
    private String outNum;
    @SerializedName("true_num")
    private String trueName;
    @SerializedName("goods_specs")
    private String goodsSpecs;

    public String getGoodsSpecs() {
        return goodsSpecs;
    }

    public void setGoodsSpecs(String goodsSpecs) {
        this.goodsSpecs = goodsSpecs;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOutNum() {
        return outNum;
    }

    public void setOutNum(String outNum) {
        this.outNum = outNum;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
