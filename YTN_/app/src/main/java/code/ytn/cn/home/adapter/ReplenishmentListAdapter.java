package code.ytn.cn.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.ReplmentListEntity;

import static code.ytn.cn.common.CommonConstant.STATUS_FOUR;
import static code.ytn.cn.common.CommonConstant.STATUS_ONE;
import static code.ytn.cn.common.CommonConstant.STATUS_THREE;
import static code.ytn.cn.common.CommonConstant.STATUS_TWO;

/**
 * Created by dell on 2018/4/16
 * 补货列表
 */

public class ReplenishmentListAdapter extends BaseAdapterNew<ReplmentListEntity> {
    private Context context;
    private View.OnClickListener onClickListener;

    public ReplenishmentListAdapter(Context context, List<ReplmentListEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_list_replment;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        ReplmentListEntity entity = getItem(position);
        TextView num = ViewHolder.get(convertView, R.id.item_replment_num);
        TextView name = ViewHolder.get(convertView, R.id.item_replment_name);
        TextView time = ViewHolder.get(convertView, R.id.item_replment_time);
        TextView status = ViewHolder.get(convertView, R.id.item_rep_select);
        TextView detail = ViewHolder.get(convertView, R.id.item_replment_detail);
        TextView btn = ViewHolder.get(convertView, R.id.item_btn);

        if (entity != null) {

            num.setText(entity.getReplenCode());
            name.setText(entity.getCabinetName());
            time.setText(entity.getCreateData());
            switch (entity.getStatus()) {
                case STATUS_ONE:  // 待审核
                    status.setBackground(context.getResources().getDrawable(R.drawable.pending));
                    break;
                case STATUS_TWO:  // 已完成
                    status.setBackground(context.getResources().getDrawable(R.drawable.off_the_stocks));
                    break;
                case STATUS_THREE:  // 锁定
                    status.setBackground(context.getResources().getDrawable(R.drawable.icon_block));
                    break;
                case STATUS_FOUR:
                    status.setBackground(context.getResources().getDrawable(R.drawable.bushu));
                    break;
            }
        }
    }
}
