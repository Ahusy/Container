package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/3/12
 * 入库单详情
 */

public class InputGoodsDetailEntity extends Entity{
    @SerializedName("goods_list")
    private List<InGoodsListEntity> listEntities;

    public List<InGoodsListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<InGoodsListEntity> listEntities) {
        this.listEntities = listEntities;
    }
}
