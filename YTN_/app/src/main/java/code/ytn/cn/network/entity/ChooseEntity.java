package code.ytn.cn.network.entity;

/**
 * Created by dell on 2018/3/27
 */

public class ChooseEntity {
    private String type = "";

    public ChooseEntity(String type) {
        this.type = type;
    }
    public ChooseEntity(){}

    public String getType() {

        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
