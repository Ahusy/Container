package code.ytn.cn.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.DataEntity;

/**
 * Created by dell on 2018/7/6
 */

public class WeekSpinnerAdapter extends BaseAdapter {
    private Context context;
    List<DataEntity> mDatas;
    private String weeks;

    public WeekSpinnerAdapter(Context context, List<DataEntity> mDatas, String weeks) {
        this.mDatas = mDatas;
        this.context = context;
        this.weeks = weeks;
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
        LayoutInflater _LayoutInflater = LayoutInflater.from(context);
        convertView = _LayoutInflater.inflate(R.layout.dialog_spinner_week, null);
        if (convertView != null) {
            TextView item = (TextView) convertView.findViewById(R.id.dialog_spinner_week);
            TextView item1 = (TextView) convertView.findViewById(R.id.dialog_spinner_data);
            if (mDatas.get(position).getWeek().equals("请选择周")) {
                item.setText("请选择周");
            } else if (mDatas.get(position).getWeek().equals(String.valueOf(weeks))) {
                item.setText("本周");
                item1.setText("（" + mDatas.get(position).getStart() + "-" + mDatas.get(position).getEnd() + ")");
            } else {
                item.setText("第" + mDatas.get(position).getWeek() + "周");
                item1.setText("（" + mDatas.get(position).getStart() + "-" + mDatas.get(position).getEnd() + ")");
            }
        }
        return convertView;
    }
}
