package code.ytn.cn.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.InOrderListEntity;

import static code.ytn.cn.common.CommonConstant.STATUS_ONE;
import static code.ytn.cn.common.CommonConstant.STATUS_THREE;
import static code.ytn.cn.common.CommonConstant.STATUS_TWO;

/**
 * Created by dell on 2018/3/9
 * 入库单列表
 */

public class InOrderListAdapter extends BaseAdapterNew<InOrderListEntity> {

    private Context context;

    public InOrderListAdapter(Context context, List<InOrderListEntity> mDatas) {
        super(context, mDatas);
        this.context = context;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_list_inputgoods;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        InOrderListEntity entity = getItem(position);
        TextView num = ViewHolder.get(convertView, R.id.item_inputgoods_num);
        TextView name = ViewHolder.get(convertView, R.id.item_inputgoods_name);
        TextView time = ViewHolder.get(convertView, R.id.item_inputgoods_time);
        TextView status = ViewHolder.get(convertView, R.id.item_inputgoods_select);
        TextView btn = ViewHolder.get(convertView, R.id.item_inputgoods_btn);

        if (entity != null) {
            num.setText(entity.getInOrderCode());
            name.setText(entity.getCabinetName());
            time.setText(entity.getTransTime());
            switch (entity.getStatus()) {
                case STATUS_ONE:// 待处理
                    btn.setText(R.string.title_extcu_input);
                    status.setBackground(context.getResources().getDrawable(R.drawable.pending));
                    break;
                case STATUS_TWO: // 完成
                    btn.setText(R.string.text_look_detail);
                    status.setBackground(context.getResources().getDrawable(R.drawable.off_the_stocks));
                    break;
                case STATUS_THREE:// 取消
                    status.setText(R.string.text_look_detail);
                    status.setBackground(context.getResources().getDrawable(R.drawable.dismis));
                    break;
            }
        }
    }
}
