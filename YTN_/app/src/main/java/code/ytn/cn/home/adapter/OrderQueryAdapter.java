package code.ytn.cn.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.OrderListEntity;

import static code.ytn.cn.common.CommonConstant.ORDER_SIX;
import static code.ytn.cn.common.CommonConstant.STATUS_ZERO;

/**
 * Created by dell on 2018/3/9
 * 订单
 */

public class OrderQueryAdapter extends BaseAdapterNew<OrderListEntity> {

    private Context context;
    private View.OnClickListener onClickListener;

    public OrderQueryAdapter(Context context, List<OrderListEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_list_order_query;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        OrderListEntity entity = getItem(position);
        TextView num = ViewHolder.get(convertView, R.id.item_order_query_num);
        TextView time = ViewHolder.get(convertView, R.id.item_time);
        TextView name = ViewHolder.get(convertView, R.id.item_order_query_name);
        TextView count = ViewHolder.get(convertView, R.id.item_order_query_count);
        TextView price = ViewHolder.get(convertView, R.id.item_price);
        TextView status = ViewHolder.get(convertView, R.id.item_order_query_select);

        if (entity != null) {
            num.setText(entity.getOrderCode());
            name.setText(entity.getUserId());
            time.setText(entity.getPayTime());
            count.setText(entity.getNum());
            price.setText(entity.getPayMoney());
            if (entity.getErrorStatus().equals(STATUS_ZERO) || entity.getErrorStatus().equals(ORDER_SIX)) {
                // 正常
                status.setBackground(context.getResources().getDrawable(R.drawable.order_ok));
            } else {
                // 异常
                status.setBackground(context.getResources().getDrawable(R.drawable.order_no));
            }
        }
    }
}
