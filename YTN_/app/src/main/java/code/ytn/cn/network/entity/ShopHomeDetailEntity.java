package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

public class ShopHomeDetailEntity extends Entity {

    @SerializedName("status")
    private String status;
    @SerializedName("total")
    private String total;
    @SerializedName("order_count")
    private String orderCount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }
}
