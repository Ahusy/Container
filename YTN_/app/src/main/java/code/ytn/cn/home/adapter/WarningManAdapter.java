package code.ytn.cn.home.adapter;

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

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.utils.ImageUtils;

/**
 * Created by dell on 2018/7/4
 * 库存预警适配器
 */

public class WarningManAdapter extends BaseAdapter {

    private Context context;
    private List<ChooseGoodsListEntity> mDatas;

    public WarningManAdapter(Context context, List<ChooseGoodsListEntity> mDatas) {
        this.mDatas = mDatas;
        this.context = context;
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
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_list_warning, null);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.item_warning_lv_img);
            holder.name = (TextView) convertView.findViewById(R.id.item_warning_lv_name);
            holder.et = (EditText) convertView.findViewById(R.id.item_warning_lv_et);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ChooseGoodsListEntity bean = mDatas.get(position);
        ImageUtils.setSmallImg(holder.img, bean.getFirstPic());
        holder.name.setText(bean.getGoodsName() + bean.getGoodsSpec());
        holder.et.setTag(bean);

        //清除焦点
        holder.et.clearFocus();

        holder.et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (holder.et.toString().equals("0") || s.toString().startsWith("0")) {
                    holder.et.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //获得Edittext所在position里面的Bean，并设置数据
                ChooseGoodsListEntity bean = (ChooseGoodsListEntity) holder.et.getTag();
                bean.setStockWarning(s + "");
            }
        });

        if (!TextUtils.isEmpty(bean.getStockWarning())) {
            holder.et.setText(bean.getStockWarning());
        } else {
            holder.et.setText("");
        }

        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView img;
        TextView name;
        TextView spec;
        EditText et;
    }
}
