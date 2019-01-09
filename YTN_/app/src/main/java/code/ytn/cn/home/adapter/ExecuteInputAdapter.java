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

import java.util.HashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.InGoodsListEntity;
import code.ytn.cn.utils.ImageUtils;
import code.ytn.cn.utils.InputCompleteWatcher;

/**
 * Created by dell on 2018/3/9
 * 入库
 */

public class ExecuteInputAdapter extends BaseAdapter {

    private Context context;
    private InputCompleteWatcher inputCompleteWatcher;
    public ExecuteInputAdapter.IsOnClick onClick;
    private HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();

    List<InGoodsListEntity> mDatas;

    public void setOnClick(ExecuteInputAdapter.IsOnClick onClick) {
        this.onClick = onClick;
    }

    public ExecuteInputAdapter(Context context, List<InGoodsListEntity> mDatas) {
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
            convertView = View.inflate(context, R.layout.item_list_execute_input, null);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.item_exe_lv_img);
            holder.name = (TextView) convertView.findViewById(R.id.item_exe_lv_name);
            holder.et = (EditText) convertView.findViewById(R.id.item_exe_lv_et);
            holder.num = (TextView) convertView.findViewById(R.id.item_exe_lv_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final InGoodsListEntity bean = mDatas.get(position);
        inputCompleteWatcher.addEditText(holder.et);
        ImageUtils.setSmallImg(holder.img, bean.getFirstPic());
        holder.name.setText(bean.getGoodsName() + bean.getGoodsSpecs());
        holder.num.setText(bean.getTransNum());
        holder.et.setTag(bean);

        //清除焦点
        holder.et.clearFocus();
        holder.et.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //获得Edittext所在position里面的Bean，并设置数据
                InGoodsListEntity bean = (InGoodsListEntity) holder.et.getTag();
                bean.setTinNum(s + "");
                if (holder.et.getText().toString().trim() != null && !holder.et.getText().toString().trim().equals("")) {
                    int mEt = Integer.parseInt(holder.et.getText().toString().trim());
                    int mNum = Integer.parseInt(bean.getTransNum().toString().trim());
                    if (mEt == 0) {
                        holder.et.setText("");
                    }
                    // 如果输入的实际数量小于或者等于调出数量,map保存true
                    if (mEt <= mNum) {
                        hashMap.put(bean.getGoodsId(), true);
                    } else {
                        // 否则保存false
                        hashMap.put(bean.getGoodsId(), false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (!TextUtils.isEmpty(bean.getTinNum())) {
            holder.et.setText(bean.getTinNum());
        } else {
            holder.et.setText("");
        }
        return convertView;
    }


    public HashMap<String, Boolean> getHashMap() {
        return hashMap;
    }

    class ViewHolder {
        SimpleDraweeView img;
        TextView name;
        TextView num;
        TextView spec;
        EditText et;
    }


    public interface IsOnClick {
        void onItemStatus(boolean isFinish);
    }

}


