package code.ytn.cn.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.CounterListEntity;

import static code.ytn.cn.common.CommonConstant.STATUS_FOUR;
import static code.ytn.cn.common.CommonConstant.STATUS_ONE;
import static code.ytn.cn.common.CommonConstant.STATUS_THREE;
import static code.ytn.cn.common.CommonConstant.STATUS_TWO;

/**
 * Created by dell on 2018/3/9
 */

public class CounterAdapter extends BaseAdapterNew<CounterListEntity> {
    private Context context;
    private View.OnClickListener onClickListener;

    public CounterAdapter(Context context, List<CounterListEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_list_counter;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        CounterListEntity entity = getItem(position);
        TextView num = ViewHolder.get(convertView, R.id.item_num);
        TextView name = ViewHolder.get(convertView, R.id.item_name);
        TextView address = ViewHolder.get(convertView, R.id.item_address);
        TextView status = ViewHolder.get(convertView, R.id.item_select);
        TextView distance = ViewHolder.get(convertView, R.id.item_distance);
        TextView btn = ViewHolder.get(convertView, R.id.item_btn);
        TextView stockOutStatus = ViewHolder.get(convertView, R.id.item_status);

        if (entity != null) {
            num.setText(entity.getCabinetCode());
            name.setText(entity.getCabinetName());
            address.setText(entity.getAddress());
            distance.setText(entity.getDistance() + context.getString(R.string.text_rice));
            switch (entity.getStatus()) {
                case STATUS_ONE:
                    status.setBackground(context.getResources().getDrawable(R.drawable.inuse));
                    break;
                case STATUS_TWO:
                    status.setBackground(context.getResources().getDrawable(R.drawable.blockup));
                    break;
                case STATUS_THREE:
                    status.setBackground(context.getResources().getDrawable(R.drawable.icon_block));
                    break;
                case STATUS_FOUR:
                    status.setBackground(context.getResources().getDrawable(R.drawable.bushu));
            }
            if (entity.getGoodsStatus().equals("1")) {
                stockOutStatus.setTextColor(Color.RED);
                stockOutStatus.setText("缺货（" + entity.getGoodsNum() + "）");
            } else {
                stockOutStatus.setText("正常");
            }
        }
    }

}
