package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/3/14
 */

public class OrderQueryEntity extends Entity {
    @SerializedName("total_page")
    private int totalPage;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @SerializedName("cabinet_name")
    private String cabiName;
    @SerializedName("order_list")
    private List<OrderListEntity> listEntities;

    public String getCabiName() {
        return cabiName;
    }

    public void setCabiName(String cabiName) {
        this.cabiName = cabiName;
    }

    public List<OrderListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<OrderListEntity> listEntities) {
        this.listEntities = listEntities;
    }
}
