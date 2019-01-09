package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/4/16
 */

public class ReplmentEntity extends Entity{
    @SerializedName("total_page")
    private int totalPage;
    @SerializedName("order_list")
    private List<ReplmentListEntity> listEntities;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ReplmentListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<ReplmentListEntity> listEntities) {
        this.listEntities = listEntities;
    }
}
