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
 * Created by dell on 2018/4/11
 */

public class GoodsAllotAdapter extends BaseAdapter {

    private Context context;
    private View.OnClickListener onClickListener;
    private InputCompleteWatcher inputCompleteWatcher;
    public GoodsAllotAdapter.IsONClick onClick;
    private List<ChooseGoodsListEntity> mDatas;

    public void setOnClick(GoodsAllotAdapter.IsONClick onClick) {
        this.onClick = onClick;
    }


    public GoodsAllotAdapter(Context context, List<ChooseGoodsListEntity> mDatas, View.OnClickListener onClickListener) {
        this.mDatas = mDatas;
        this.context = context;
        this.onClickListener = onClickListener;

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
            convertView = View.inflate(context, R.layout.item_list_goods_allot, null);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.item_allot_lv_img);
            holder.name = (TextView) convertView.findViewById(R.id.item_allot_lv_name);
            holder.et = (EditText) convertView.findViewById(R.id.item_allot_lv_et);
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
                //获得Edittext所在position里面的Bean，并设置数据
                ChooseGoodsListEntity bean = (ChooseGoodsListEntity) holder.et.getTag();
                bean.setSaleCount(s + "");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (!TextUtils.isEmpty(bean.getSaleCount())) {
            holder.et.setText(bean.getSaleCount());
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

    public interface IsONClick {
        void onItemStatus(boolean isFinish);
    }
}
