package code.ytn.cn.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.shop.event.PriceAndCountEvent;
import code.ytn.cn.utils.BigDecimalUtils;
import code.ytn.cn.utils.ImageUtils;

/**
 * Created by dell on 2018/5/18
 */

public class ValetOrderAdapter extends RecyclerView.Adapter<ValetOrderAdapter.MyViewHolder> {

    private Context context;
    private List<ChooseGoodsListEntity> mDatas;

    public ValetOrderAdapter(Context context, List<ChooseGoodsListEntity> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    //  定义接口写条目点击事件和长按事件

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public ValetOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_list_valet_goods, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ValetOrderAdapter.MyViewHolder holder, int position) {
        ImageUtils.setSmallImg(holder.img, mDatas.get(position).getFirstPic());
        holder.name.setText(mDatas.get(position).getGoodsName());
        holder.price.setText("￥" + mDatas.get(position).getGoodsPrice());
        holder.etNum.setText(mDatas.get(position).getNum() + "");

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 从布局中获取条目位置
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(v -> {
                // 从布局中获取条目位置
                int pos = holder.getLayoutPosition();
                mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                return false;
            });
        }

        EventBus.getDefault().post(compute());

        holder.add.setOnClickListener(v -> {
            int num = mDatas.get(position).getNum();
            holder.etNum.setText(++num + "");
            mDatas.get(position).setNum(num);
            EventBus.getDefault().post(compute());
        });

        holder.del.setOnClickListener(v -> {
            int num = mDatas.get(position).getNum();
            if (num == 1) {
                return;
            }
            holder.etNum.setText(--num + "");
            mDatas.get(position).setNum(num);
            EventBus.getDefault().post(compute());
        });

    }

    private PriceAndCountEvent compute() {
        double count = 0;
        double price = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            double goodsPrice = Double.parseDouble(mDatas.get(i).getGoodsPrice());
//            price += goodsPrice * mDatas.get(i).getNum();
            double round = BigDecimalUtils.mul(goodsPrice, mDatas.get(i).getNum(), 2);
            price += round;
            count += mDatas.get(i).getNum();
        }
        PriceAndCountEvent priceAndCountEvent = new PriceAndCountEvent();
        priceAndCountEvent.setCount(count);
        priceAndCountEvent.setPrice(price);
        return priceAndCountEvent;
    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        EventBus.getDefault().post(compute());
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView img;
        TextView name, price, del, add, etNum;

        public MyViewHolder(View itemView) {
            super(itemView);

            img = (SimpleDraweeView) itemView.findViewById(R.id.item_list_valet_goods_img);
            name = (TextView) itemView.findViewById(R.id.item_list_valet_goods_name);
            price = (TextView) itemView.findViewById(R.id.item_list_valet_goods_price);
            del = (TextView) itemView.findViewById(R.id.item_list_valet_goods_del);
            add = (TextView) itemView.findViewById(R.id.item_list_valet_goods_add);
            etNum = (TextView) itemView.findViewById(R.id.item_list_valet_goods_num);

        }
    }
}
