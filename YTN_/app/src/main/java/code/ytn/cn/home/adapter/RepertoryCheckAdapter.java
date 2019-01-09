package code.ytn.cn.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.CheckListEntity;

import static code.ytn.cn.common.CommonConstant.STATUS_ONE;
import static code.ytn.cn.common.CommonConstant.STATUS_THREE;
import static code.ytn.cn.common.CommonConstant.STATUS_TWO;

/**
 * Created by dell on 2018/3/10
 */

public class RepertoryCheckAdapter extends BaseAdapterNew<CheckListEntity> {
    private Context context;
    private View.OnClickListener onClickListener;

    public RepertoryCheckAdapter(Context context, List<CheckListEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getResourceId(int resId) {
        return R.layout.item_list_repertory_check;
    }

    @Override
    public void setViewData(View convertView, int position) {

        CheckListEntity entity = getItem(position);
        TextView num = ViewHolder.get(convertView, R.id.item_repertory_num);
        TextView name = ViewHolder.get(convertView, R.id.item_repertory_out_name);
        TextView time = ViewHolder.get(convertView, R.id.item_repertory_time);
        TextView status = ViewHolder.get(convertView, R.id.item_rep_select);

        if (entity != null) {
            num.setText(entity.getCheckCode());
            name.setText(entity.getRelaName());
            time.setText(entity.getCheckTime());
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
            }
        }
    }
}
