package code.ytn.cn.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.OrderQueryDetailListEntity;
import code.ytn.cn.shop.event.CountEvent;
import code.ytn.cn.utils.ImageUtils;

/**
 * Created by dell on 2018/5/17
 */

public class DialogErrorAdapter extends BaseAdapter {

    private Context context;
    private List<OrderQueryDetailListEntity> mDatas;

    public DialogErrorAdapter(Context context, List<OrderQueryDetailListEntity> mDatas) {
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
            convertView = View.inflate(context, R.layout.item_dialog_goods, null);
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_dialog_goods,null);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.item_dialog_lv_img);
            holder.cb = (CheckBox) convertView.findViewById(R.id.item_dialog_lv_cb);
            holder.name = (TextView) convertView.findViewById(R.id.item_dialog_lv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageUtils.setSmallImg(holder.img, mDatas.get(position).getFirstPic());
        holder.name.setText(mDatas.get(position).getGoodsName() + mDatas.get(position).getGoodsSpecs());
        holder.cb.setOnClickListener(v -> {
            if (holder.cb.isChecked()) {
                mDatas.get(position).setCheck(true);
                EventBus.getDefault().post(compute());

            } else {
                mDatas.get(position).setCheck(false);
            }
        });
        holder.cb.setChecked(mDatas.get(position).isCheck());
        return convertView;
    }

    private CountEvent compute() {
        int count = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).isCheck()) {
                count++;
            }
        }
        CountEvent event = new CountEvent();
        event.setCount(count);
        return event;
    }

    class ViewHolder {
        SimpleDraweeView img;
        CheckBox cb;
        TextView name;
    }

}
