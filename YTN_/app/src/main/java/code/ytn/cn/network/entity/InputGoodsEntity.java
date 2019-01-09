package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/3/9
 * 入库单查询
 */

public class InputGoodsEntity extends Entity{
    @SerializedName("total_page")
    private int totalPage;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @SerializedName("inOrder_list")
    private List<InOrderListEntity> inOrderListEntities;

    public List<InOrderListEntity> getInOrderListEntities() {
        return inOrderListEntities;
    }

    public void setInOrderListEntities(List<InOrderListEntity> inOrderListEntities) {
        this.inOrderListEntities = inOrderListEntities;
    }

}
