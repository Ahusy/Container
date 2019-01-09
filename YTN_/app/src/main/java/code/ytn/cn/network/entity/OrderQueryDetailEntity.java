package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/3/14
 */

public class OrderQueryDetailEntity extends Entity{
    @SerializedName("orderItems_order")
    private List<OrderQueryDetailListEntity> listEntities;

    public List<OrderQueryDetailListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<OrderQueryDetailListEntity> listEntities) {
        this.listEntities = listEntities;
    }

    @SerializedName("coupon_code")
    private String couponCode;
    @SerializedName("coupon_name")
    private String couponName;
    @SerializedName("dsc_total")
    private String dscTotal;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getDscTotal() {
        return dscTotal;
    }

    public void setDscTotal(String dscTotal) {
        this.dscTotal = dscTotal;
    }
}
