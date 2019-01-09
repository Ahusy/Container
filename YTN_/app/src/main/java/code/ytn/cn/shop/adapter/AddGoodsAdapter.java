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
 * Created by dell on 2018/5/18
 */

public class AddGoodsAdapter extends RecyclerView.Adapter<AddGoodsAdapter.MyViewHolder> {

    private Context context;
    private List<ChooseGoodsListEntity> mDatas;

    public AddGoodsAdapter(Context context, List<ChooseGoodsListEntity> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public AddGoodsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_recy_add_goods, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AddGoodsAdapter.MyViewHolder holder, int position) {
        ImageUtils.setSmallImg(holder.img, mDatas.get(position).getFirstPic());
        holder.name.setText(mDatas.get(position).getGoodsName() + mDatas.get(position).getGoodsSpec());
        holder.price.setText("货柜售价：￥" + mDatas.get(position).getGoodsPrice());

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
        TextView name, price;
        CheckBox cb;

        public MyViewHolder(View itemView) {
            super(itemView);

            img = (SimpleDraweeView) itemView.findViewById(R.id.item_order_detail_img);
            name = (TextView) itemView.findViewById(R.id.item_recy_add_goods_name);
            price = (TextView) itemView.findViewById(R.id.item_recy_add_goods_price);
            cb = (CheckBox) itemView.findViewById(R.id.item_recy_add_goods_cb);
        }
    }

}
