package code.ytn.cn.shop.fragment;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.SalesRankEntity;

/**
 * Created by dell on 2018/7/11
 */

public class TodayRankAdapter extends BaseAdapterNew<SalesRankEntity.SalesRankListEntity>{
    private Context context;
    List<SalesRankEntity.SalesRankListEntity> mDatas;
    public TodayRankAdapter(Context context, List<SalesRankEntity.SalesRankListEntity> mDatas) {
        super(context, mDatas);
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    protected int getResourceId(int position) {
        return R.layout.item_list_today_fragment;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        SalesRankEntity.SalesRankListEntity entity = getItem(position);
        TextView num = ViewHolder.get(convertView, R.id.item_today_frag_num);
        TextView name = ViewHolder.get(convertView, R.id.item_today_frag_name);
        TextView spec = ViewHolder.get(convertView, R.id.item_today_frag_sepc);
        TextView count = ViewHolder.get(convertView, R.id.item_today_frag_count);

        if (entity != null) {
            num.setText(entity.getNum());
            name.setText(entity.getFname());
            spec.setText(entity.getSpecs());
            count.setText(entity.getNowCount());
        }

    }
}
