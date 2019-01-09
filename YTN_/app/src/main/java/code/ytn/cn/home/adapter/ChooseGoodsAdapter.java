package code.ytn.cn.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.event.Count;
import code.ytn.cn.event.GoodsCheck;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.utils.ImageUtils;
import code.ytn.cn.utils.SafeSharePreferenceUtil;

/**
 * Created by dell on 2018/3/9
 * 选择商品
 */

public class ChooseGoodsAdapter extends RecyclerView.Adapter<ChooseGoodsAdapter.MyViewHolder> {

    private List<ChooseGoodsListEntity> mDatas;
    private List<ChooseGoodsListEntity> mSelected;
    private List<GoodsCheck> mList;
    Context context;

    public ChooseGoodsAdapter(Context context, List<ChooseGoodsListEntity> mDatas, List<ChooseGoodsListEntity> mSelected) {

        this.mDatas = mDatas;
        this.context = context;
        this.mSelected = mSelected;
        mList = new ArrayList<>();
    }

    @Override
    public ChooseGoodsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_list_choose_goods, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChooseGoodsAdapter.MyViewHolder holder, final int position) {

        holder.name.setText(mDatas.get(position).getGoodsName());
//        holder.spec.setText(mDatas.get(position).getGoodsSpec());
        ImageUtils.setSmallImg(holder.img, mDatas.get(position).getFirstPic());

        String goodsId = mDatas.get(position).getGoodsId();
        boolean aBoolean = SafeSharePreferenceUtil.getBoolean(goodsId, false);
        holder.cb.setChecked(aBoolean);

        holder.cb.setOnClickListener(v -> {

            if (!holder.cb.isChecked()) {
                mDatas.get(position).setChecked(true);

                SafeSharePreferenceUtil.saveBoolean(mDatas.get(position).getGoodsId(), false);
                EventBus.getDefault().post(compute());
            } else {
                mDatas.get(position).setChecked(false);
                SafeSharePreferenceUtil.saveBoolean(mDatas.get(position).getGoodsId(), true);
                EventBus.getDefault().post(compute());
            }
            mDatas.get(position).setChecked(!mDatas.get(position).isChecked());
        });

    }

    // 计算数量
    private Count compute() {
        int count = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            String goodsId = mDatas.get(i).getGoodsId();
            boolean aBoolean = SafeSharePreferenceUtil.getBoolean(goodsId, false);
            if (aBoolean) {
                count++;
            }
        }

        Count mCount = new Count();
        mCount.setCount(count);
        return mCount;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, spec;
        CheckBox cb;
        SimpleDraweeView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = (SimpleDraweeView) itemView.findViewById(R.id.item_choose_goods_img);
            name = (TextView) itemView.findViewById(R.id.item_choose_goods_name);
            spec = (TextView) itemView.findViewById(R.id.item_choose_goods_spec);
            cb = (CheckBox) itemView.findViewById(R.id.item_choose_goods_check);
        }
    }

    public List<GoodsCheck> getListGoods() {
        return mList;
    }

}
