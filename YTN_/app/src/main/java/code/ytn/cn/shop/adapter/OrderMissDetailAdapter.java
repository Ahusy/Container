package code.ytn.cn.shop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.OrderMissDetailEntity;
import code.ytn.cn.utils.ImageUtils;

/**
 * Created by dell on 2018/7/10
 */

public class OrderMissDetailAdapter extends BaseAdapterNew<OrderMissDetailEntity.GoodsListEntity> {
    private Context context;

    public OrderMissDetailAdapter(Context context, List<OrderMissDetailEntity.GoodsListEntity> mDatas) {
        super(context, mDatas);
        this.context = context;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_list_goods_order_miss;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        OrderMissDetailEntity.GoodsListEntity entity = getItem(position);
        SimpleDraweeView img = ViewHolder.get(convertView, R.id.item_order_miss_detail_img);
        TextView name = ViewHolder.get(convertView, R.id.item_order_miss_detail_name);
        TextView pack = ViewHolder.get(convertView, R.id.item_order_miss_detail_pack);
        TextView spec = ViewHolder.get(convertView, R.id.item_order_miss_detail_spec);
        TextView price = ViewHolder.get(convertView, R.id.item_order_miss_detail_price);
        TextView count = ViewHolder.get(convertView, R.id.item_order_miss_detail_count);

        if (entity != null) {
            ImageUtils.setSmallImg(img, entity.getFirstPic());
            name.setText(entity.getGoodsName());
            spec.setText("规格:" + entity.getGoodsSpecs());
            price.setText(entity.getGoodsPrice());
            count.setText("x" + entity.getQty());
        }

    }
}
