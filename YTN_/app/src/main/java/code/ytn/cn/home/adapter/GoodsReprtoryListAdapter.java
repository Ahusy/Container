package code.ytn.cn.home.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.utils.ImageUtils;
import code.ytn.cn.utils.InputCompleteWatcher;

/**
 * Created by dell on 2018/3/14
 * 盘点
 */

public class GoodsReprtoryListAdapter extends BaseAdapterNew<ChooseGoodsListEntity> {

    private Context context;
    private View.OnClickListener onClickListener;
    private String id;
    private ArrayList<ChooseGoodsListEntity> mDatas;
    private final InputCompleteWatcher inputCompleteWatcher;
    private GoodsReprtoryListAdapter.ReproBackEditTextContent backEditTextContent;
    public GoodsReprtoryListAdapter.IsOnClick onClick;

    public void setOnClick(GoodsReprtoryListAdapter.IsOnClick onClick) {
        this.onClick = onClick;
    }

    public void setBackEditTextContent(GoodsReprtoryListAdapter.ReproBackEditTextContent backEditTextContent) {
        this.backEditTextContent = backEditTextContent;

    }

    public GoodsReprtoryListAdapter(Context context, ArrayList<ChooseGoodsListEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.mDatas = mDatas;
        this.context = context;
        this.onClickListener = onClickListener;
        inputCompleteWatcher = new InputCompleteWatcher(new InputCompleteWatcher.InputCompleteListener() {
            @Override
            public boolean onTextChange(boolean isFinish) {
                onClick.onItemStatus(isFinish);
                return false;
            }
        });
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_list_repertory;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        ChooseGoodsListEntity entity = getItem(position);
        SimpleDraweeView img = ViewHolder.get(convertView, R.id.item_rep_img);
        TextView name = ViewHolder.get(convertView, R.id.item_rep_name);
        TextView spec = ViewHolder.get(convertView, R.id.item_rep_spec);
        EditText et1 = ViewHolder.get(convertView, R.id.item_rep_et1);
        EditText e2 = ViewHolder.get(convertView, R.id.item_rep_et2);
//        inputCompleteWatcher.addEditText(et1);
        inputCompleteWatcher.addEditText(e2);
        if (entity != null) {
            name.setText(entity.getGoodsName() + entity.getGoodsSpec());
//            spec.setText(entity.getGoodsSpec());
            ImageUtils.setSmallImg(img, entity.getFirstPic());
        }


        id = entity.getGoodsId();
        et1.addTextChangedListener(new GoodsReprtoryListAdapter.EditTextListener(id));
        et1.addTextChangedListener(new MyTextWatcher(entity) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    entity.setDamageCount(s.toString());
                    inputCompleteWatcher.addEditText(et1);
                    for (ChooseGoodsListEntity e : mDatas) {
                        if (e.getGoodsId().equals(entity.getGoodsId())) {
                            mDatas.remove(e);
                            e = entity;
                            mDatas.add(e);
                            listener.onSetDataFinished(mDatas);

                            break;
                        }
                    }
                } else {
                    entity.setDamageCount(0 + "");

                }
            }
        });


        e2.addTextChangedListener(new MyTextWatcher(entity) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null) {
                    entity.setSaleCount("");
                } else {
                    entity.setSaleCount(s.toString());
                }
                for (ChooseGoodsListEntity e : mDatas) {
                    if (e.getGoodsId().equals(entity.getGoodsId())) {
                        mDatas.remove(e);
                        e = entity;
                        mDatas.add(e);
                        listener.onSetDataFinished(mDatas);
                        break;
                    }
                }
            }
        });
    }


    abstract class MyTextWatcher implements TextWatcher {
        public ChooseGoodsListEntity entity;

        public MyTextWatcher(ChooseGoodsListEntity entity) {
            this.entity = entity;
        }
    }

    private OnSetDataFinishedListener listener;

    public void setDataFinishedListener(OnSetDataFinishedListener listener) {
        this.listener = listener;
    }

    public interface OnSetDataFinishedListener {
        void onSetDataFinished(ArrayList<ChooseGoodsListEntity> mDatas);
    }

    public interface IsOnClick {
        void onItemStatus(boolean isFinish);
    }

    public interface ReproBackEditTextContent {
        void backEditContent(String id, String content);
    }

    // 货损
    public class EditTextListener implements TextWatcher {
        private String id;

        public EditTextListener(String id) {
            this.id = id;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            backEditTextContent.backEditContent(id, s.toString());
        }
    }

}
