package code.ytn.cn.shop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.utils.ImageUtils;

/**
 * Created by dell on 2018/5/21
 */

public class GoodsAdjustPriceAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ChooseGoodsListEntity> mDatas;

    public GoodsAdjustPriceAdapter(Context context, ArrayList<ChooseGoodsListEntity> mDatas) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_list_goods_adjust_price, null);
//            holder.cb = (CheckBox) convertView.findViewById(R.id.item_goods_adjust_price_cb);
            holder.name = (TextView) convertView.findViewById(R.id.item_goods_adjust_price_name);
            holder.spec = (TextView) convertView.findViewById(R.id.item_goods_adjust_price_spec);
            holder.standPrice = (TextView) convertView.findViewById(R.id.item_goods_adjust_stand_price);
            holder.counterPrice = (TextView) convertView.findViewById(R.id.item_goods_adjust_counter_price);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.item_goods_adjust_price_img);
            holder.etPrice = (EditText) convertView.findViewById(R.id.item_goods_adjust_price_edit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChooseGoodsListEntity bean = mDatas.get(position);
        holder.name.setText(bean.getGoodsName());
        holder.counterPrice.setText("货柜售价：￥" + bean.getGoodsPrice());
        holder.standPrice.setText("标准售价：￥" + bean.getStandPrice());
        ImageUtils.setSmallImg(holder.img, bean.getFirstPic());
        holder.spec.setText("规格：" + bean.getGoodsSpec());
//        holder.etPrice.setText(bean.getGoodsPrice());
        holder.etPrice.setTag(bean);

        //清除焦点
        holder.etPrice.clearFocus();
        holder.etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //获得Edittext所在position里面的Bean，并设置数据
                ChooseGoodsListEntity bean = (ChooseGoodsListEntity) holder.etPrice.getTag();
                bean.setAdjustPrice(s + "");

            }
        });

        if (!TextUtils.isEmpty(bean.getAdjustPrice())) {
            holder.etPrice.setText(bean.getAdjustPrice());
        } else {
            holder.etPrice.setText("");
        }

        return convertView;
    }

    class ViewHolder {
        //        CheckBox cb;
        TextView name, spec, standPrice, counterPrice;
        SimpleDraweeView img;
        EditText etPrice;
    }
}
