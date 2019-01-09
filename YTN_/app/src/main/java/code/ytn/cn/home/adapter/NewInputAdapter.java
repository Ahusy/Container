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

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.utils.ImageUtils;
import code.ytn.cn.utils.InputCompleteWatcher;

/**
 * Created by dell on 2018/7/24
 */

public class NewInputAdapter extends BaseAdapter {
    private Context context;
    private InputCompleteWatcher inputCompleteWatcher;
    public IsONClick onClick;
    private List<ChooseGoodsListEntity> mDatas;

    public void setOnClick(IsONClick onClick) {
        this.onClick = onClick;
    }


    public NewInputAdapter(Context context, List<ChooseGoodsListEntity> mDatas) {
        this.mDatas = mDatas;
        this.context = context;

        inputCompleteWatcher = new InputCompleteWatcher(isFinish -> {
            onClick.onItemStatus(isFinish);
            return false;
        });
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
            convertView = View.inflate(context, R.layout.item_new_choose_goods, null);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.item_new_pic);
            holder.name = (TextView) convertView.findViewById(R.id.item_new_name);
            holder.et = (EditText) convertView.findViewById(R.id.item_new_et);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ChooseGoodsListEntity bean = mDatas.get(position);
        inputCompleteWatcher.addEditText(holder.et);
        ImageUtils.setSmallImg(holder.img, bean.getFirstPic());
        holder.name.setText(bean.getGoodsName());
//        holder.et.addTextChangedListener(new GoodsAllotAdapter.EditTextListener(id));
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
                //获得EditText所在position里面的Bean，并设置数据
                ChooseGoodsListEntity bean = (ChooseGoodsListEntity) holder.et.getTag();
                bean.setInputNum(s + "");
            }
        });

        if (!TextUtils.isEmpty(bean.getInputNum())) {
            holder.et.setText(bean.getInputNum());
        } else {
            holder.et.setText("");
        }

        return convertView;
    }


    class ViewHolder {
        SimpleDraweeView img;
        TextView name;
        EditText et;
    }

    public interface IsONClick {
        void onItemStatus(boolean isFinish);
    }
}

