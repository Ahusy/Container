package code.ytn.cn.shop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.ChannelEntity;

/**
 * Created by dell on 2018/8/23
 */

public class ValetOrderChannelAdapter extends BaseAdapter {
    private Context context;
    private View.OnClickListener onClickListener;
    private List<ChannelEntity.ChannelListEntity> mDatas;
    private PopupWindow mPopupWindow;

    public ValetOrderChannelAdapter(Context context, List<ChannelEntity.ChannelListEntity> mDatas, View.OnClickListener onClickListener, PopupWindow mPopupWindow) {
//        this.(context,mDatas);
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
            convertView = View.inflate(context, R.layout.textview, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.spinner_txt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(mDatas.get(position).getPayName());

        return convertView;
    }

    static class ViewHolder {
        TextView mTextView;
    }
}
