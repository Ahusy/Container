package code.ytn.cn.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.InGoodsListEntity;
import code.ytn.cn.utils.ImageUtils;

/**
 * Created by dell on 2018/3/9
 * 入库
 */

public class InputDetailtAdapter extends BaseAdapterNew<InGoodsListEntity> {

    private Context context;
    private InGoodsListEntity entity;

    public InputDetailtAdapter(Context context, List<InGoodsListEntity> mDatas) {
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
        TextView numOne = ViewHolder.get(convertView, R.id.item_num_one);
        TextView numTwo = ViewHolder.get(convertView, R.id.item_num_two);
        TextView spec = ViewHolder.get(convertView, R.id.item_spec);

        if (entity != null) {
            name.setText(entity.getGoodsName());
            numOne.setText(entity.getTransNum());
            numTwo.setText(entity.getTinNum());
//            spec.setText(entity.getGoodsSpecs());
            ImageUtils.setSmallImg(img, entity.getFirstPic());
        }
    }

}
