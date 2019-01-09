package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2018/4/16
 */

public class ReplmentListDetailEntity {
    @SerializedName("goods_id")
    private String goodsId;
    @SerializedName("replenish_num")
    private String replenNum;
    @SerializedName("goods_name")
    private String goodsName;
    @SerializedName("goods_spec")
    private String goodsSpec;
    @SerializedName("first_pic")
    private String firstPic;

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getReplenNum() {
        return replenNum;
    }

    public void setReplenNum(String replenNum) {
        this.replenNum = replenNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }
}
