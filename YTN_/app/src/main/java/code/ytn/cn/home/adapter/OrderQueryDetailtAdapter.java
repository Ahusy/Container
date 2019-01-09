package code.ytn.cn.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.OrderQueryDetailListEntity;
import code.ytn.cn.utils.ImageUtils;

/**
 * Created by dell on 2018/3/9
 * 订单详情
 */

public class OrderQueryDetailtAdapter extends BaseAdapterNew<OrderQueryDetailListEntity> {


    private Context context;
    private View.OnClickListener onClickListener;

    public OrderQueryDetailtAdapter(Context context, List<OrderQueryDetailListEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_list_order_query_detail;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void setViewData(View convertView, int position) {
        OrderQueryDetailListEntity entity = getItem(position);
        SimpleDraweeView img = ViewHolder.get(convertView, R.id.item_order_detail_img);
        TextView name = ViewHolder.get(convertView, R.id.item_order_detail_name);
        TextView pack = ViewHolder.get(convertView, R.id.item_order_detail_pack);
        TextView size = ViewHolder.get(convertView, R.id.item_order_detail_size);
        TextView price = ViewHolder.get(convertView, R.id.item_order_detail_price);
        TextView count = ViewHolder.get(convertView, R.id.item_order_detail_count);
        if (entity != null) {
            name.setText(entity.getGoodsName());
            size.setText(context.getString(R.string.text_specification) + entity.getGoodsSpecs());
            price.setText(entity.getGoodsPrice());
            count.setText("x" + entity.getGoodsNum());
            ImageUtils.setSmallImg(img, entity.getFirstPic());
        }
    }

}
