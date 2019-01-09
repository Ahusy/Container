package code.ytn.cn.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.ReplmentListDetailEntity;
import code.ytn.cn.utils.ImageUtils;

/**
 * Created by dell on 2018/4/16
 */

public class ReplmentDetailAdapter extends BaseAdapterNew<ReplmentListDetailEntity> {
    private Context context;

    public ReplmentDetailAdapter(Context context, List<ReplmentListDetailEntity> mDatas) {
        super(context, mDatas);
        this.context = context;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_list_goods_detail;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        ReplmentListDetailEntity entity = getItem(position);
        SimpleDraweeView img = ViewHolder.get(convertView, R.id.item_img);
        TextView name = ViewHolder.get(convertView, R.id.item_name);
        TextView numOne = ViewHolder.get(convertView, R.id.item_num_one);
        TextView numTwo = ViewHolder.get(convertView, R.id.item_num_two);
        TextView spec = ViewHolder.get(convertView, R.id.item_spec);
        numOne.setVisibility(View.GONE);

        if (entity != null) {
            ImageUtils.setSmallImg(img, entity.getFirstPic());
            name.setText(entity.getGoodsName() + entity.getGoodsSpec());
            numTwo.setText(entity.getReplenNum());
        }
    }
}
