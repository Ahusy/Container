package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 选择调出货柜商品
 */

public class ChooseGoodsListEntity implements Serializable{
    @SerializedName("goods_id")
    private String goodsId;
    @SerializedName("goods_name")
    private String goodsName;
    @SerializedName("goods_price")
    private String goodsPrice;
    @SerializedName("goods_spec")
    private String goodsSpec;
    @SerializedName("first_pic")
    private String firstPic;
    @SerializedName("damage_count")
    private String damageCount;
    @SerializedName("sale_count")
    private String saleCount;
    @SerializedName("stock")
    private String stock;
    @SerializedName("out_num")
    private String outNum;
    @SerializedName("replment_count")
    private String replmentCount;
    @SerializedName("adjust_price")
    private String adjustPrice;
    @SerializedName("stand_price")
    private String standPrice;
    @SerializedName("result_code")
    private String resultCode;
    @SerializedName("result_msg")
    private String resultMsg;
    @SerializedName("stock_warning")
    private String stockWarning;

    public String getStockWarning() {
        return stockWarning;
    }

    public void setStockWarning(String stockWarning) {
        this.stockWarning = stockWarning;
    }

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

    public String getStandPrice() {
        return standPrice;
    }

    public void setStandPrice(String standPrice) {
        this.standPrice = standPrice;
    }

    public String getAdjustPrice() {
        return adjustPrice;
    }

    public void setAdjustPrice(String adjustPrice) {
        this.adjustPrice = adjustPrice;
    }

    public String getReplmentCount() {
        return replmentCount;
    }

    public void setReplmentCount(String replmentCount) {
        this.replmentCount = replmentCount;
    }

    public String getOutNum() {
        return outNum;
    }

    public void setOutNum(String outNum) {
        this.outNum = outNum;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    private int num = 1;


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDamageCount() {
        return damageCount;
    }

    public void setDamageCount(String damageCount) {
        this.damageCount = damageCount;
    }

    public String getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(String saleCount) {
        this.saleCount = saleCount;
    }

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean checked;

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

    @SerializedName("input_num")
    private String inputNum;

    public String getNewOutNUm() {
        return newOutNUm;
    }

    public void setNewOutNUm(String newOutNUm) {
        this.newOutNUm = newOutNUm;
    }

    @SerializedName("new_out_num")
    private String newOutNUm;

    public String getInputNum() {
        return inputNum;
    }

    public void setInputNum(String inputNum) {
        this.inputNum = inputNum;
    }
}
