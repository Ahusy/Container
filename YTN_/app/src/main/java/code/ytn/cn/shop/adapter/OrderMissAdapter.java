package code.ytn.cn.shop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.entity.OrderMissEntity;
import code.ytn.cn.utils.VerifyUtils;

import static code.ytn.cn.common.CommonConstant.ORDER_EIGHT;
import static code.ytn.cn.common.CommonConstant.ORDER_FIVE;
import static code.ytn.cn.common.CommonConstant.ORDER_FOUR;
import static code.ytn.cn.common.CommonConstant.ORDER_NINE;
import static code.ytn.cn.common.CommonConstant.ORDER_ONE;
import static code.ytn.cn.common.CommonConstant.ORDER_SEVEN;
import static code.ytn.cn.common.CommonConstant.ORDER_SIX;
import static code.ytn.cn.common.CommonConstant.ORDER_THREE;
import static code.ytn.cn.common.CommonConstant.ORDER_TWO;

/**
 * Created by dell on 2018/7/10
 */

public class OrderMissAdapter extends BaseAdapterNew<OrderMissEntity.OrderMissListEntity> {
    private Context context;

    public OrderMissAdapter(Context context, List<OrderMissEntity.OrderMissListEntity> mDatas) {
        super(context, mDatas);
        this.context = context;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_list_order_miss;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        OrderMissEntity.OrderMissListEntity entity = getItem(position);
        TextView num = ViewHolder.get(convertView, R.id.item_miss_order_num);
        TextView time = ViewHolder.get(convertView, R.id.item_miss_order_time);
        TextView user = ViewHolder.get(convertView, R.id.item_miss_order_user);
        TextView status = ViewHolder.get(convertView, R.id.item_miss_order_status);
        TextView czPerson = ViewHolder.get(convertView, R.id.item_miss_order_cz_person);
        TextView czTime = ViewHolder.get(convertView, R.id.item_miss_order_cz_time);
        TextView payMoney = ViewHolder.get(convertView, R.id.item_miss_order_pay_money);
        TextView returnMoney = ViewHolder.get(convertView, R.id.item_miss_order_return_money);

        if (entity != null) {
            String statusName = "";
            // 订单状态：1，待支付；2，已支付；3，已发货； 4.已完成；5、取消；6：退款；7：未购物；8：支付失败；9：已销单
            switch (entity.getStatus()) {
                case ORDER_ONE:
                    statusName = context.getString(R.string.order_unpaid);
                    break;
                case ORDER_TWO:
                    statusName = context.getString(R.string.order_have_paid);
                    break;
                case ORDER_THREE:
                    statusName = context.getString(R.string.order_shipped);
                    break;
                case ORDER_FOUR:
                    statusName = context.getString(R.string.order_off_the_stocks);
                    break;
                case ORDER_FIVE:
                    statusName = context.getString(R.string.order_cancel);
                    break;
                case ORDER_SIX:
                    statusName = context.getString(R.string.order_refund);
                    break;
                case ORDER_SEVEN:
                    statusName = context.getString(R.string.order_no_shopping);
                    break;
                case ORDER_EIGHT:
                    statusName = context.getString(R.string.order_pay_error);
                    break;
                case ORDER_NINE:
                    statusName = context.getString(R.string.order_miss);
                    break;
            }
            num.setText(entity.getFlowNo());
            time.setText(entity.getPayTime());
            if (entity.getUserPhone() != null && !entity.getUserPhone().equals("")) {
                boolean phone = VerifyUtils.checkPhoneNumber(entity.getUserPhone());
                if (phone) {
                    String userPhone = entity.getUserPhone();
                    String mobile = userPhone.substring(0, 3) + "****" + userPhone.substring(7, userPhone.length());
                    user.setText(mobile);
                } else {
                    user.setText(entity.getUserPhone());
                }
            } else {
                user.setText("");
            }
            status.setText(statusName);
            czPerson.setText(UserCenter.getName(context));
            czTime.setText(entity.getCreateDate());
            payMoney.setText(entity.getOrderTotal());
            returnMoney.setText(entity.getRefundAmt());
        }

    }
}
