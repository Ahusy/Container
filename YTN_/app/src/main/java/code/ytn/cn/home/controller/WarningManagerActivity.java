package code.ytn.cn.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.home.adapter.WarningManAdapter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.ChooseGoodsEntity;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;
import static code.ytn.cn.common.CommonConstant.STATUS_ZERO;

/**
 * 库存预警管理页
 */
public class WarningManagerActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.rigth_text)
    private TextView rightText;
    @ViewInject(R.id.warning_name)
    private TextView warName;
    @ViewInject(R.id.warning_lv)
    private ListView lv;
    @ViewInject(R.id.warning_commit)
    private TextView warCommit;
    @ViewInject(R.id.left_button)
    private ImageView leftBtn;
    private String cabinetId;
    private WarningManAdapter adapter;
    private List<ChooseGoodsListEntity> entities;
    private String mGoods;
    private boolean isClick = false;

    public static void startWarningManagerActivity(Context context,String cabinetId,String cabinetName){
        Intent intent = new Intent(context,WarningManagerActivity.class);
        intent.putExtra(CABINET_ID,cabinetId);
        intent.putExtra(CABINET_NAME,cabinetName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_manager);
        ViewInjectUtils.inject(this);
        setCenterTitle("库存预警管理");
        setRightText("清空阈值");
        initData();
        queryGoodsList();
    }

    private void initData() {
        rightText.setOnClickListener(this);
        warCommit.setOnClickListener(this);
        leftBtn.setOnClickListener(this);
        cabinetId = getIntent().getStringExtra(CABINET_ID);
        String name = getIntent().getStringExtra(CABINET_NAME);
        warName.setText(name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rigth_text: // 清空阈值
                for (int i = 0; i < entities.size(); i++) {
                    entities.get(i).setStockWarning("");
                }
                adapter.notifyDataSetChanged();
                isClick = true;
                ToastUtil.shortShow(this, "清除成功");
                break;
            case R.id.warning_commit: // 确定
                for (int i = 0; i < entities.size(); i++) {
                    if ((entities.get(i).getStockWarning() != null && !entities.get(i).getStockWarning().equals("")) && entities.get(i).getStockWarning().equals("0")) {
                        ToastUtil.showToast(this, "预警阈值不能为0");
                        return;
                    }
                }
                commit();
                break;
            case R.id.left_button:
                if (isClick) {
                    DialogUtils.showTwoBtnDialog(this, "提示", "是否保存所设置的预警阈值?", v1 -> {
                        commit();
                        DialogUtils.closeDialog();
                    }, v1 -> {
                        finish();
                        DialogUtils.closeDialog();
                    });
                } else {
                    finish();
                }
                break;
        }
    }

    private void toJson() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < entities.size(); i++) {
            JSONObject tmpObj = null;
            tmpObj = new JSONObject();
            try {
                tmpObj.put("goods_id", entities.get(i).getGoodsId());
                tmpObj.put("stock_warning", entities.get(i).getStockWarning());
                jsonArray.put(tmpObj);
                mGoods = jsonArray.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        ChooseGoodsEntity entity = Parsers.getChooseGoodsEntity(data);
        Entity result = Parsers.getResult(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (entity != null) {
                    entities = entity.getListEntities();
                    adapter = new WarningManAdapter(this, entities);
                    lv.setAdapter(adapter);
                } else {
                    ToastUtil.shortShow(this, "暂无数据");
                }
                break;
            case REQUEST_NET_TWO:
                if (result.getResultCode().equals(STATUS_ZERO)) {
                    ToastUtil.shortShow(this, result.getResultMsg());
                    finish();
                } else {
                    ToastUtil.shortShow(this, result.getResultMsg());
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isClick) {
            DialogUtils.showTwoBtnDialog(this, "提示", "是否保存所设置的预警阈值?", v -> {
                commit();
                DialogUtils.closeDialog();
            }, v -> {
                finish();
                DialogUtils.closeDialog();
            });
        } else {
            finish();
        }
    }

    // 查询商品
    private void queryGoodsList() {
        showProgressDialog();
        IdentityHashMap<String,String> params = new IdentityHashMap<>();
        params.put("cabinet_id", cabinetId);
        requestHttpData(Constants.Urls.URL_POST_CABINET_GOODSLIST, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    // 提交
    private void commit() {
        toJson();
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("cabinet_id", cabinetId);
        params.put("goods_list", mGoods);
        requestHttpData(Constants.Urls.URL_POST_UPDATE_STOCK_WARNING, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

}
