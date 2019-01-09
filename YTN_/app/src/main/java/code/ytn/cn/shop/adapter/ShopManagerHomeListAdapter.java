package code.ytn.cn.shop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.ShopHomeEntity;

/**
 * Created by dell on 2018/5/15
 */

public class ShopManagerHomeListAdapter extends BaseAdapterNew<ShopHomeEntity> {
    private Context context;
    private View.OnClickListener onClickListener;

    public ShopManagerHomeListAdapter(Context context, List<ShopHomeEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_list_shop_manager_home;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        ShopHomeEntity entity = getItem(position);
        TextView shopName = ViewHolder.get(convertView, R.id.item_shop_name);
        TextView counCount = ViewHolder.get(convertView, R.id.item_counter_count);
        TextView todayOrder = ViewHolder.get(convertView, R.id.item_today_order);
        TextView yesOrder = ViewHolder.get(convertView, R.id.item_yesterday_order);
        TextView yesSellGoods = ViewHolder.get(convertView, R.id.item_yesterday_sell_goods);
        TextView yesDealLimit = ViewHolder.get(convertView, R.id.item_yesterday_deal_limit);
        TextView todDealLimit = ViewHolder.get(convertView, R.id.item_today_deal_limit);
        TextView todSellGoods = ViewHolder.get(convertView, R.id.item_today_sell_goods);
        TextView todMissOrder = ViewHolder.get(convertView, R.id.item_today_miss_order);
        TextView yesMissOrder = ViewHolder.get(convertView, R.id.item_yesterday_miss_order);

        if (entity != null) {
            shopName.setText(entity.getStoreName());
            counCount.setText(entity.getOperCabinet());
            todayOrder.setText(entity.getTorder());
            yesOrder.setText(entity.getYtotalOrder());
            yesSellGoods.setText(entity.getYtotalGoods());
            yesDealLimit.setText("￥" + entity.getYtotalMoney());
            todDealLimit.setText("￥" + entity.getTotalMoney());
            todSellGoods.setText(entity.gettCount());
            todMissOrder.setText(entity.getTxdCount());
            yesMissOrder.setText(entity.getYxdCount());
        }
    }
}
