package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/7/10
 */

public class OrderMissEntity extends Entity {
    @SerializedName("order_list")
    private List<OrderMissListEntity> listEntities;
    @SerializedName("total_page")
    private int totalPage;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<OrderMissListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<OrderMissListEntity> listEntities) {
        this.listEntities = listEntities;
    }

    public class OrderMissListEntity {
    @SerializedName("flow_no")
    private String flowNo;
    @SerializedName("pay_time")
    private String payTime;

        public String getFlowNo() {
            return flowNo;
        }

        public void setFlowNo(String flowNo) {
            this.flowNo = flowNo;
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
}
}
