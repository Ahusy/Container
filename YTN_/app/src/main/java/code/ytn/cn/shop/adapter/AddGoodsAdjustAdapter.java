package code.ytn.cn.shop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.utils.ImageUtils;

/**
 * Created by dell on 2018/6/4
 */

public class AddGoodsAdjustAdapter extends RecyclerView.Adapter<AddGoodsAdjustAdapter.MyViewHolder> {

    private Context context;
    private List<ChooseGoodsListEntity> mDatas;

    public AddGoodsAdjustAdapter(Context context, List<ChooseGoodsListEntity> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public AddGoodsAdjustAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_list_add_goods_adjust, null);
        AddGoodsAdjustAdapter.MyViewHolder holder = new AddGoodsAdjustAdapter.MyViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AddGoodsAdjustAdapter.MyViewHolder holder, int position) {
        ImageUtils.setSmallImg(holder.img, mDatas.get(position).getFirstPic());
        holder.name.setText(mDatas.get(position).getGoodsName());
        holder.spec.setText("规格：" + mDatas.get(position).getGoodsSpec());
        holder.standPrice.setText("标准售价：￥" + mDatas.get(position).getStandPrice());
        holder.counterPrice.setText("货柜售价：￥" + mDatas.get(position).getGoodsPrice());

        holder.cb.setOnClickListener(v -> {
            if (holder.cb.isChecked()) {
                mDatas.get(position).setChecked(true);
            } else {
                mDatas.get(position).setChecked(false);
            }
        });

        holder.cb.setChecked(mDatas.get(position).isChecked());
    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView img;
        TextView name, spec, standPrice, counterPrice;
        CheckBox cb;

        public MyViewHolder(View itemView) {
            super(itemView);

            img = (SimpleDraweeView) itemView.findViewById(R.id.item_add_goods_img);
            name = (TextView) itemView.findViewById(R.id.item_add_goods_adjust_price_name);
            spec = (TextView) itemView.findViewById(R.id.item_add_goods_adjust_price_spec);
            standPrice = (TextView) itemView.findViewById(R.id.item_add_goods_adjust_stand_price);
            counterPrice = (TextView) itemView.findViewById(R.id.item_add_goods_adjust_counter_price);
            cb = (CheckBox) itemView.findViewById(R.id.item_add_goods_adjust_cb);
        }
    }
}


