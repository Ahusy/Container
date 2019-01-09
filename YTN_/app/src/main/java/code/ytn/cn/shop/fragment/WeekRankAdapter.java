package code.ytn.cn.shop.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.SalesRankEntity;

/**
 * Created by dell on 2018/7/11
 */

public class WeekRankAdapter extends BaseAdapterNew<SalesRankEntity.SalesRankListEntity>{
    private Context context;
    List<SalesRankEntity.SalesRankListEntity> mDatas;
    public WeekRankAdapter(Context context, List<SalesRankEntity.SalesRankListEntity> mDatas) {
        super(context, mDatas);
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_list_week_fragment;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        SalesRankEntity.SalesRankListEntity entity = getItem(position);
        TextView num = ViewHolder.get(convertView, R.id.week_frag_num);
        TextView name = ViewHolder.get(convertView, R.id.week_frag_name);
        TextView spec = ViewHolder.get(convertView, R.id.week_frag_spec);
        TextView count = ViewHolder.get(convertView, R.id.week_frag_count);
        TextView per = ViewHolder.get(convertView, R.id.week_frag_per);
        ImageView arro = ViewHolder.get(convertView, R.id.week_frag_arro);

        if (entity != null) {
            num.setText(entity.getNum());
            name.setText(entity.getFname());
            spec.setText(entity.getSpecs());
            count.setText(entity.getNowCount());
            per.setText(entity.getRingRatio());
            if (entity.getTrend().equals("flat")){
                arro.setImageDrawable(context.getResources().getDrawable(R.drawable.arro_h));
            }else if(entity.getTrend().equals("rise")){
                arro.setImageDrawable(context.getResources().getDrawable(R.drawable.arro_top));
            }else if(entity.getTrend().equals("lower")){
                arro.setImageDrawable(context.getResources().getDrawable(R.drawable.arro_btm));
            }
        }

    }
}
