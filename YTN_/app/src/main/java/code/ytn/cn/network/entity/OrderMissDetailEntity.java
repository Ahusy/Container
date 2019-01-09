package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/7/10
 */

public class OrderMissDetailEntity extends Entity {

    @SerializedName("goods_list")
    private List<GoodsListEntity> listEntities;
    @SerializedName("order_flow_no")
    private String orderFlowNo;
    @SerializedName("pay_time")
    private String payTime;
    @SerializedName("user_phone")
    private String userPhone;
    @SerializedName("status")
    private String status;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("create_date")
    private String createDate;
    @SerializedName("order_total")
    private String orderTotal;
    @SerializedName("refund_amt")
    private String refundAmt;
    @SerializedName("pay_type")
    private String payType;

    public List<GoodsListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<GoodsListEntity> listEntities) {
        this.listEntities = listEntities;
    }

    public String getOrderFlowNo() {
        return orderFlowNo;
    }

    public void setOrderFlowNo(String orderFlowNo) {
        this.orderFlowNo = orderFlowNo;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getRefundAmt() {
        return refundAmt;
    }

    public void setRefundAmt(String refundAmt) {
        this.refundAmt = refundAmt;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    @SerializedName("flow_no")
    private String flowNo;
    @SerializedName("goods_num")
    private String goodsNum;

    public class GoodsListEntity{
        @SerializedName("goods_name")
        private String GoodsName;
        @SerializedName("goods_price")
        private String goodsPrice;
        @SerializedName("goods_specs")
        private String goodsSpecs;
        @SerializedName("first_pic")
        private String firstPic;

        public String getGoodsName() {
            return GoodsName;
        }

        public void setGoodsName(String goodsName) {
            GoodsName = goodsName;
        }

        public String getGoodsPrice() {
            return goodsPrice;
        }

        public void setGoodsPrice(String goodsPrice) {
            this.goodsPrice = goodsPrice;
        }

        public String getGoodsSpecs() {
            return goodsSpecs;
        }

        public void setGoodsSpecs(String goodsSpecs) {
            this.goodsSpecs = goodsSpecs;
        }

        public String getFirstPic() {
            return firstPic;
        }

        public void setFirstPic(String firstPic) {
            this.firstPic = firstPic;
        }


        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        @SerializedName("qty")
        private String qty;
        @SerializedName("unit_price")
        private String unitPrice;
    }
}
