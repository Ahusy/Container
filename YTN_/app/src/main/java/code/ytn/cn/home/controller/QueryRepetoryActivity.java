package code.ytn.cn.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.home.adapter.QueryRepetoryAdapter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.ChooseGoodsEntity;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.EXTRA_TYPE;
import static code.ytn.cn.common.CommonConstant.FROM_ACT_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;

/**
 * 库存查看/缺货商品查看
 */
public class QueryRepetoryActivity extends ToolBarActivity implements View.OnClickListener {

    private String cabinetId;
    @ViewInject(R.id.query_repetory_lv)
    private ListView lv;
    @ViewInject(R.id.query_repetory_ll)
    private LinearLayout ll;
    @ViewInject(R.id.query_repetory_title)
    private LinearLayout title;
    @ViewInject(R.id.text1)
    private TextView leftText;
    @ViewInject(R.id.text2)
    private TextView rightText;
    private String etText;
    private QueryRepetoryAdapter adapter;

    public static void startQueryRepetoryActivity(Context context,String cabinetId,int exeType){
        Intent intent = new Intent(context,QueryRepetoryActivity.class);
        intent.putExtra(CABINET_ID,cabinetId);
        intent.putExtra(EXTRA_TYPE,exeType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_repetory);
        ViewInjectUtils.inject(this);
        int extra = getIntent().getIntExtra(EXTRA_TYPE, FROM_ACT_ONE);
        if (extra == FROM_ACT_ONE) {
            setCenterTitle(getString(R.string.title_counter_pri));
            initData();
            getData();
        } else {
            // 缺货商品查看
            setCenterTitle(getString(R.string.title_oos_goods_look));
            leftText.setText(R.string.text_value);
            rightText.setText(R.string.title_at_count);
            cabinetId = getIntent().getStringExtra(CABINET_ID);
            queryStock();
        }

    }

    // 初始化数据
    private void initData() {
        mBtnTitleRightSerach.setVisibility(View.VISIBLE);
        mBtnTitleRightSerach.setOnClickListener(this);
        cabinetId = getIntent().getStringExtra(CABINET_ID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_button_serach: // 搜索
                DialogUtils.showQueryRepetoryDialog(this, (v1, editText) -> {
                    etText = editText.getText().toString().trim();
                    getData();
                    DialogUtils.closeDialog();
                }, v1 -> DialogUtils.closeDialog());
                break;
        }
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        ChooseGoodsEntity entity = Parsers.getChooseGoodsEntity(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (entity != null) {
                    List<ChooseGoodsListEntity> listEntities = entity.getListEntities();
                    if (listEntities != null && listEntities.size() > 0) {
                        adapter = new QueryRepetoryAdapter(this, listEntities, this, false);
                        lv.setAdapter(adapter);
                    } else {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        ToastUtil.shortShow(this, "暂无数据");
                    }
                }

                break;
            case REQUEST_NET_TWO:
                if (entity != null) {
                    List<ChooseGoodsListEntity> listEntities = entity.getListEntities();
                    if (listEntities != null && listEntities.size() > 0) {
                        adapter = new QueryRepetoryAdapter(this, listEntities, this, true);
                        lv.setAdapter(adapter);
                    } else {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        ToastUtil.shortShow(this, "暂无数据");
                    }
                }
                break;
        }

    }

    // 库存查看
    private void getData() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("cabinet_id", cabinetId);
        params.put("goods_name", etText);
        requestHttpData(Constants.Urls.URL_POST_CABINET_GOODSLIST, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    // 缺货商品查看
    private void queryStock() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("cabinet_id", cabinetId);
        requestHttpData(Constants.Urls.URL_POST_GOODS_WARNING, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }
}
