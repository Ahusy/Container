package code.ytn.cn.event;

import java.io.Serializable;

/**
 * Created by dell on 2018/3/21
 */

public class  GoodsCheck implements Serializable{
    private  String name;
    private  boolean flag;

    public String getId() {
        return name;
    }

    public void setId(String name) {
        this.name = name;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }


    @Override
    public String toString() {
        return "GoodsCheck{" +
                "name='" + name + '\'' +
                ", flag=" + flag +
                '}';
    }
}
