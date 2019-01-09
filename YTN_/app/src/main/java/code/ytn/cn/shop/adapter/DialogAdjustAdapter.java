package code.ytn.cn.shop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.utils.ImageUtils;

/**
 * Created by dell on 2018/5/21
 */

public class DialogAdjustAdapter extends BaseAdapter {

    private Context context;
    private List<ChooseGoodsListEntity> mDatas;

    public DialogAdjustAdapter(Context context, List<ChooseGoodsListEntity> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.dialog_adjust_price_comit, null);
            holder.name = (TextView) convertView.findViewById(R.id.dialog_goods_adjust_price_name);
            holder.spec = (TextView) convertView.findViewById(R.id.dialog_goods_adjust_price_spec);
            holder.standPrice = (TextView) convertView.findViewById(R.id.dialog_goods_adjust_price_stand_price);
            holder.counterPrice = (TextView) convertView.findViewById(R.id.dialog_goods_adjust_price_counter_price);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.dialog_goods_adjust_price_img);
            holder.price = (TextView) convertView.findViewById(R.id.dialog_goods_adjust_price_edit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageUtils.setSmallImg(holder.img, mDatas.get(position).getFirstPic());
        holder.name.setText(mDatas.get(position).getGoodsName());
        holder.spec.setText("规格" + mDatas.get(position).getGoodsSpec());
        holder.price.setText("￥" + mDatas.get(position).getAdjustPrice());
        holder.counterPrice.setText("货柜售价：￥" + mDatas.get(position).getGoodsPrice());
        holder.standPrice.setText("标准售价：￥" + mDatas.get(position).getStandPrice());

        return convertView;
    }

    class ViewHolder {
        TextView name, spec, standPrice, counterPrice, price;
        SimpleDraweeView img;
    }
}
