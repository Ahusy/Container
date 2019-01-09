package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/3/10
 */

public class RepertoryCheckEntity extends Entity{
    @SerializedName("total_page")
    private int totalPage;
    @SerializedName("check_list")
    private List<CheckListEntity> listEntities;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<CheckListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<CheckListEntity> listEntities) {
        this.listEntities = listEntities;
    }
}
