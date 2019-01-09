package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/3/10
 */

public class ReperCheckDetailEntity extends Entity{
    @SerializedName("goods_list")
    private List<ReperCheckDetailListEntity> listEntities;

    public List<ReperCheckDetailListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<ReperCheckDetailListEntity> listEntities) {
        this.listEntities = listEntities;
    }
}
