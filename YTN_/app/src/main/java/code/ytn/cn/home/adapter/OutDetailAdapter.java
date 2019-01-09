package code.ytn.cn.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.OutGoodsListEntity;
import code.ytn.cn.utils.ImageUtils;

/**
 * Created by dell on 2018/3/9
 * 出库详情
 */

public class OutDetailAdapter extends BaseAdapterNew<OutGoodsListEntity> {

    private Context context;
    private OutGoodsListEntity entity;

    public OutDetailAdapter(Context context, List<OutGoodsListEntity> mDatas) {
        super(context, mDatas);
        this.context = context;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_list_goods_detail;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        entity = getItem(position);
        SimpleDraweeView img = ViewHolder.get(convertView, R.id.item_img);
        TextView name = ViewHolder.get(convertView, R.id.item_name);
        TextView noe = ViewHolder.get(convertView, R.id.item_num_one);
        TextView two = ViewHolder.get(convertView, R.id.item_num_two);
        TextView spec = ViewHolder.get(convertView, R.id.item_spec);

        if (entity != null) {
            name.setText(entity.getGoodsName());
            noe.setText(entity.getOutNum());
            two.setText(entity.getTrueName());
//            spec.setText(entity.getGoodsSpecs());
            ImageUtils.setSmallImg(img, entity.getFirstPic());
        }

    }

}
