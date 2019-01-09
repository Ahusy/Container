package code.ytn.cn.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.utils.ImageUtils;

/**
 * Created by dell on 2018/6/19
 * 库存查看
 */

public class QueryRepetoryAdapter extends BaseAdapterNew<ChooseGoodsListEntity> {

    private Context context;
    private View.OnClickListener onClickListener;
    private boolean type;

    public QueryRepetoryAdapter(Context context, List<ChooseGoodsListEntity> mDatas, View.OnClickListener onClickListener, boolean type) {
        super(context, mDatas);
        this.context = context;
        this.type = type;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_list_goods_detail;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        ChooseGoodsListEntity entity = getItem(position);
        SimpleDraweeView img = ViewHolder.get(convertView, R.id.item_img);
        TextView name = ViewHolder.get(convertView, R.id.item_name);
        TextView numOne = ViewHolder.get(convertView, R.id.item_num_one);
        TextView numTwo = ViewHolder.get(convertView, R.id.item_num_two);
        TextView spec = ViewHolder.get(convertView, R.id.item_spec);

        if (entity != null) {
            if (type == true) {
                numTwo.setTextColor(Color.RED);
                numOne.setText(entity.getStockWarning());
            } else {
                numOne.setText(entity.getGoodsPrice());
            }
            ImageUtils.setSmallImg(img, entity.getFirstPic());
            name.setText(entity.getGoodsName());
            spec.setText(entity.getGoodsSpec());
            numTwo.setText(entity.getStock());
        }
    }
}
