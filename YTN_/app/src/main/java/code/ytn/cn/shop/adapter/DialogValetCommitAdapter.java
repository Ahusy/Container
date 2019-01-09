package code.ytn.cn.shop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.ItemResultEntity;
import code.ytn.cn.utils.ImageUtils;

/**
 * 代客下单确认
 */

public class DialogValetCommitAdapter extends BaseAdapterNew<ItemResultEntity.ItemResultListEntity> {

    private Context context;

    public DialogValetCommitAdapter(Context context, List<ItemResultEntity.ItemResultListEntity> mDatas) {
        super(context, mDatas);
        this.context = context;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.dialog_valet_comit_list;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void setViewData(View convertView, int position) {
        ItemResultEntity.ItemResultListEntity entity = getItem(position);
        SimpleDraweeView img = ViewHolder.get(convertView, R.id.dialog_valet_commit_list_img);
        TextView name = ViewHolder.get(convertView, R.id.dialog_valet_commit_list_name);
        TextView price = ViewHolder.get(convertView, R.id.dialog_valet_commit_list_price);
        TextView count = ViewHolder.get(convertView, R.id.dialog_valet_commit_list_count);
        TextView dscPrice = ViewHolder.get(convertView, R.id.dialog_valet_commit_list_dsc_price);

        if (entity != null) {
            ImageUtils.setSmallImg(img, entity.getFirstPic());
            name.setText(entity.getGoodsName() + " " + entity.getSpecs());
            price.setText("货柜售价：￥" + entity.getCabinetPrice());
            count.setText("x" + entity.getNum());
            if (!entity.getDscPrice().equals("0")){
                dscPrice.setVisibility(View.VISIBLE);
                dscPrice.setText("优惠金额:￥"+entity.getDscPrice());
            }
        }
    }
}
