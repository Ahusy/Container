package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 优惠后商品
 */

public class ItemResultEntity extends Entity {
    @SerializedName("type")
    private String type; // 1、不优惠  2、买一赠一  3、满减或直降
    @SerializedName("dsc_total")
    private String dscTotal; // 总优惠金额
    @SerializedName("total")
    private String total;  // 原价总金额
    @SerializedName("coupon_code")
    private String couponCode; // 优惠券码
    @SerializedName("coupon_name")
    private String couponName;  // 优惠券名
    @SerializedName("act_total") // 合计
    private String actTotal;

    public String getActTotal() {
        return actTotal;
    }

    public void setActTotal(String actTotal) {
        this.actTotal = actTotal;
    }

    @SerializedName("item_list")
    private List<ItemResultListEntity> listEntities;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDscTotal() {
        return dscTotal;
    }

    public void setDscTotal(String dscTotal) {
        this.dscTotal = dscTotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

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

    public List<ItemResultListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<ItemResultListEntity> listEntities) {
        this.listEntities = listEntities;
    }

    public class ItemResultListEntity {
        @SerializedName("goods_id")
        private String goodsId;
        @SerializedName("goods_name")
        private String goodsName;
        @SerializedName("first_pic")
        private String firstPic;
        @SerializedName("cabinet_price")
        private String cabinetPrice;
        @SerializedName("dsc_price")
        private String dscPrice;
        @SerializedName("specs")
        private String specs;
        @SerializedName("num")
        private String num;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
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

        public String getFirstPic() {
            return firstPic;
        }

        public void setFirstPic(String firstPic) {
            this.firstPic = firstPic;
        }

        public String getCabinetPrice() {
            return cabinetPrice;
        }

        public void setCabinetPrice(String cabinetPrice) {
            this.cabinetPrice = cabinetPrice;
        }

        public String getDscPrice() {
            return dscPrice;
        }

        public void setDscPrice(String dscPrice) {
            this.dscPrice = dscPrice;
        }

        public String getSpecs() {
            return specs;
        }

        public void setSpecs(String specs) {
            this.specs = specs;
        }
    }
}


