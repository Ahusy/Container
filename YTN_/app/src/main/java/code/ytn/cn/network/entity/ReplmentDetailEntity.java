package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/4/16
 */

public class ReplmentDetailEntity extends Entity {
    public List<ReplmentListDetailEntity> getListDetailEntities() {
        return listDetailEntities;
    }

    public void setListDetailEntities(List<ReplmentListDetailEntity> listDetailEntities) {
        this.listDetailEntities = listDetailEntities;
    }

    @SerializedName("goods_list")
    private List<ReplmentListDetailEntity> listDetailEntities;
}
