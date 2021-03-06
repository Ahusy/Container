package code.ytn.cn.home.adapter;

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
 * Created by dell on 2018/4/12
 * 盘点
 */

public class AddNewCheckAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ChooseGoodsListEntity> mDatas;

    public AddNewCheckAdapter(Context context, ArrayList<ChooseGoodsListEntity> mDatas) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_list_repertory, null);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.item_rep_img);
            holder.name = (TextView) convertView.findViewById(R.id.item_rep_name);
            holder.et1 = (EditText) convertView.findViewById(R.id.item_rep_et1);
            holder.et2 = (EditText) convertView.findViewById(R.id.item_rep_et2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ChooseGoodsListEntity bean = mDatas.get(position);
//        inputCompleteWatcher.addEditText(holder.et2);
        ImageUtils.setSmallImg(holder.img, bean.getFirstPic());
        holder.name.setText(bean.getGoodsName() + bean.getGoodsSpec());
        holder.et1.setTag(bean);
        holder.et2.setTag(bean);
        //清除焦点
        holder.et1.clearFocus();
        holder.et2.clearFocus();

        holder.et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //获得Edittext所在position里面的Bean，并设置数据
                ChooseGoodsListEntity bean = (ChooseGoodsListEntity) holder.et1.getTag();
                if (!s.toString().equals("") && s != null) {
                    bean.setDamageCount(s + "");
                } else {
                }
            }
        });

        if (!TextUtils.isEmpty(bean.getDamageCount())) {
            holder.et1.setText(bean.getDamageCount());
        } else {
            holder.et1.setText("");
        }


        holder.et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //获得Edittext所在position里面的Bean，并设置数据
                ChooseGoodsListEntity bean = (ChooseGoodsListEntity) holder.et1.getTag();
                bean.setSaleCount(s + "");
            }
        });

        if (!TextUtils.isEmpty(bean.getSaleCount())) {
            holder.et2.setText(bean.getSaleCount());
        } else {
            holder.et2.setText("");
        }

        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView img;
        TextView name;
        EditText et1;
        EditText et2;
    }

}
