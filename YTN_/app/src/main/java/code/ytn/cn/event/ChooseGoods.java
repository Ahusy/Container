package code.ytn.cn.event;

import java.io.Serializable;
import java.util.List;

import code.ytn.cn.network.entity.ChooseGoodsListEntity;

/**
 * Created by dell on 2018/3/14
 */

public class ChooseGoods implements Serializable{
    private List<ChooseGoodsListEntity> listEntities;

    public ChooseGoods(List<ChooseGoodsListEntity> listEntities) {
        this.listEntities = listEntities;
    }

    public List<ChooseGoodsListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<ChooseGoodsListEntity> listEntities) {
        this.listEntities = listEntities;
    }
}
