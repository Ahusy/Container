package code.ytn.cn.shop.event;

/**
 * Created by dell on 2018/5/23
 * 统计商品数量和小计
 */

public class PriceAndCountEvent {
    private double price;
    private double count;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }
}
