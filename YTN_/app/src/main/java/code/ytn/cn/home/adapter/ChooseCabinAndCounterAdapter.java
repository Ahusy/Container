package code.ytn.cn.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.CounterListEntity;

/**
 * Created by dell on 2018/3/9
 * 选择货柜仓
 */

public class ChooseCabinAndCounterAdapter extends BaseAdapter {
    private Context context;
    private View.OnClickListener onClickListener;
    private List<CounterListEntity> mDatas;
    private PopupWindow mPopupWindow;

    public ChooseCabinAndCounterAdapter(Context context, List<CounterListEntity> mDatas, View.OnClickListener onClickListener, PopupWindow mPopupWindow) {
        this.mDatas = mDatas;
        this.context = context;
        this.onClickListener = onClickListener;
        this.mPopupWindow = mPopupWindow;
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        } else {
            return mDatas.size();
        }
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_choose_counter, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(mDatas.get(position).getCabinetName());

        return convertView;
    }

    static class ViewHolder {
        TextView mTextView;
    }
}
