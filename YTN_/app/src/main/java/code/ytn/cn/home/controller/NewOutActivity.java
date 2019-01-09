package code.ytn.cn.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.common.CommonConstant;
import code.ytn.cn.home.adapter.NewOutAdapter;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.NoDoubleClickListener;
import code.ytn.cn.utils.SafeSharePreferenceUtil;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.DEPOT_NAME;
import static code.ytn.cn.common.CommonConstant.EXTRA_FROM;
import static code.ytn.cn.common.CommonConstant.LIST;
import static code.ytn.cn.common.CommonConstant.NEW_OUT;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;

/**
 * 新建出库单
 */
public class NewOutActivity extends ToolBarActivity implements View.OnClickListener, NewOutAdapter.IsONClick {

    @ViewInject(R.id.new_out_drk)
    private TextView newDrk;
    @ViewInject(R.id.new_out_dck)
    private TextView newDck;
    @ViewInject(R.id.new_out_lv)
    private ListView lv;
    @ViewInject(R.id.new_out_choose)
    private TextView newChoose;
    @ViewInject(R.id.new_out_commit)
    private TextView newCommit;
    @ViewInject(R.id.left_button)
    private ImageView left;
    private String cabinetId;
    private String cabinetName;
    List<ChooseGoodsListEntity> goodsList = new ArrayList<>();
    private String mGoods;

    public static void startNewOutActivity(Context context,String cabinetId,String cabinetName,String deoptName){
        Intent intent = new Intent(context,NewOutActivity.class);
        intent.putExtra(CABINET_ID,cabinetId);
        intent.putExtra(CABINET_NAME,cabinetName);
        intent.putExtra(DEPOT_NAME,deoptName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_out);
        ViewInjectUtils.inject(this);
        setCenterTitle("新建出库单");
        initData();
    }

    private void initData() {
        cabinetId = getIntent().getStringExtra(CABINET_ID);
        cabinetName = getIntent().getStringExtra(CABINET_NAME);
        String depotName = getIntent().getStringExtra(DEPOT_NAME);
        newDrk.setText(cabinetName);
        newDck.setText(depotName);
        newChoose.setOnClickListener(this);
        newCommit.setOnClickListener(this);
        left.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_out_choose: // 选择
                startActivityForResult(new Intent(this, ChooseGoodsActivity.class).putExtra(CABINET_ID, cabinetId).putExtra(EXTRA_FROM, NEW_OUT), 1);
                break;
            case R.id.left_button:
                clearGoods();
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
            goodsList = (List<ChooseGoodsListEntity>) data.getSerializableExtra(LIST);
            if (goodsList != null && goodsList.size() > 0) {
                NewOutAdapter adapter = new NewOutAdapter(this, goodsList);
                lv.setAdapter(adapter);
                lv.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        switch (scrollState) {
                            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:    //当停止滚动时
                                break;
                            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:    //滚动时
                                HideKeyBoard();
                                break;

                            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:   //手指抬起，但是屏幕还在滚动状态
                                break;
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    }
                });
                adapter.setOnClick(this);
            }
        }
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        Entity result = Parsers.getResult(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (result.getResultCode().equals(CommonConstant.REQUEST_NET_SUCCESS)) {
                    DialogUtils.showOneBtnDialog(this, result.getResultMsg(), v -> {
                        DialogUtils.closeDialog();
                        finish();
                        clearGoods();
                    });
                } else {
                    ToastUtil.showToast(this, result.getResultMsg());
                }
                break;
        }
    }

    @Override
    public void onItemStatus(boolean isFinish) {
        newCommit.setEnabled(isFinish);
        newCommit.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                commit();
            }
        });
    }

    private void toJson() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < goodsList.size(); i++) {
            JSONObject tmpObj = null;
            tmpObj = new JSONObject();
            try {
                tmpObj.put("goods_id", goodsList.get(i).getGoodsId());
                tmpObj.put("out_num", goodsList.get(i).getNewOutNUm());
                jsonArray.put(tmpObj);
                mGoods = jsonArray.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 隐藏软键盘
    private void HideKeyBoard() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(NewOutActivity.this.getCurrentFocus()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // 提交
    private void commit() {
        toJson();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("cabinet_id", cabinetId);
        params.put("cabinet_name", cabinetName);
        params.put("worker_id", UserCenter.getUserId(this));
        params.put("worker_name", UserCenter.getName(this));
        params.put("chant_id", UserCenter.getChantId(this));
        params.put("goods_list", mGoods);
        requestHttpData(Constants.Urls.URL_POST_NODEPOT_OUT_STORAGE, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    // 取消上次选中状态
    private void clearGoods() {
        if (goodsList != null && goodsList.size() > 0) {
            for (int i = 0; i < goodsList.size(); i++) {
                String goodsId = goodsList.get(i).getGoodsId();
                SafeSharePreferenceUtil.saveBoolean(goodsId, false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearGoods();
        finish();
    }
}

