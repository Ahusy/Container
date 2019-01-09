package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 选择调出货柜商品
 */

public class ChooseGoodsEntity {
    @SerializedName("goods_list")
    private List<ChooseGoodsListEntity> listEntities;
    @SerializedName("total_page")
    private int totalPage;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ChooseGoodsListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<ChooseGoodsListEntity> listEntities) {
        this.listEntities = listEntities;
    }
}
