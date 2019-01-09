package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/3/13
 * 出入单详情
 */

public class OutGoodsDetailEntity extends Entity{

    @SerializedName("goods_list")
    private List<OutGoodsListEntity> listEntities;

    public List<OutGoodsListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<OutGoodsListEntity> listEntities) {
        this.listEntities = listEntities;
    }
}
