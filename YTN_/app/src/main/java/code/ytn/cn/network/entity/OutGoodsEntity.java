package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/3/9
 * 出库单查询
 */

public class OutGoodsEntity extends Entity{
    @SerializedName("total_page")
    private int totalPage;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @SerializedName("outOrder_list")
    private List<OutStorageListEntity> outStorageListEntities;

    public List<OutStorageListEntity> getOutStorageListEntities() {
        return outStorageListEntities;
    }

    public void setOutStorageListEntities(List<OutStorageListEntity> outStorageListEntities) {
        this.outStorageListEntities = outStorageListEntities;
    }

}
