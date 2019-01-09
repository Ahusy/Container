package code.ytn.cn.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.common.CommonConstant;
import code.ytn.cn.home.adapter.ExecuteOutAdapter;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.network.entity.OutGoodsDetailEntity;
import code.ytn.cn.network.entity.OutGoodsListEntity;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.NoDoubleClickListener;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.NUM;
import static code.ytn.cn.common.CommonConstant.OP_NAME;
import static code.ytn.cn.common.CommonConstant.OUT_ID;
import static code.ytn.cn.common.CommonConstant.PRO_NAME;
import static code.ytn.cn.common.CommonConstant.RELATION_CODE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;
import static code.ytn.cn.common.CommonConstant.TIME;
import static code.ytn.cn.common.CommonConstant.TYPE;

/**
 * 执行出库
 */
public class ExecuteOutActivity extends ToolBarActivity implements ExecuteOutAdapter.IsOnClick {

    @ViewInject(R.id.execute_out_num)
    private TextView exeNum;
    @ViewInject(R.id.execute_out_name)
    private TextView exeName;
    @ViewInject(R.id.execute_out_time)
    private TextView exeTime;
    @ViewInject(R.id.execute_out_person_zd)
    private TextView exeZd;
    @ViewInject(R.id.execute_out_person_cz)
    private TextView exeCz;
    @ViewInject(R.id.execute_out_lv)
    private ListView lv;
    @ViewInject(R.id.execute_out_comit)
    private TextView exeCommit;
    private String outId;
    private String cabinetId;
    private String cabinetName;
    private String relationCode;
    private List<OutGoodsListEntity> entities;
    private ExecuteOutAdapter adapter;
    private String type;
    private String mGoods;

    public static void startExecuteOutActivity(Context context,String num,String time,String cabinetName,String proName,String cabinetId,String relationCode,String opName,String outId,String type){
        Intent intent = new Intent(context,ExecuteOutActivity.class);
        intent.putExtra(NUM,num);
        intent.putExtra(TIME,time);
        intent.putExtra(CABINET_NAME,cabinetName);
        intent.putExtra(PRO_NAME,proName);
        intent.putExtra(CABINET_ID,cabinetId);
        intent.putExtra(RELATION_CODE,relationCode);
        intent.putExtra(OP_NAME,opName);
        intent.putExtra(OUT_ID,outId);
        intent.putExtra(TYPE,type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_goout);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_execu_goout));
        initData();
        queryDetail();
    }

    private void initData() {
        String code = getIntent().getStringExtra(NUM);
        String time = getIntent().getStringExtra(TIME);
        cabinetName = getIntent().getStringExtra(CABINET_NAME);
        String proName = getIntent().getStringExtra(PRO_NAME);
        String opName = getIntent().getStringExtra(OP_NAME);
        cabinetId = getIntent().getStringExtra(CABINET_ID);
        relationCode = getIntent().getStringExtra(RELATION_CODE);
        outId = getIntent().getStringExtra(OUT_ID);
        type = getIntent().getStringExtra(TYPE);
        exeNum.setText(code);
        exeName.setText(cabinetName);
        exeTime.setText(time);
        exeCz.setText(opName);
        exeZd.setText(proName);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        OutGoodsDetailEntity entity = Parsers.getOutGoodsDetailEntity(data);
        switch (requestCode) {
            case REQUEST_NET_TWO:
                if (entity != null) {
                    entities = entity.getListEntities();
                    if (entities != null && entities.size() > 0) {
                        if (entities != null && entities.size() > 0) {
                            adapter = new ExecuteOutAdapter(this, entities);
                            lv.setAdapter(adapter);
                            lv.setOnScrollListener(new AbsListView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(AbsListView view, int scrollState) {
                                    switch (scrollState) {
                                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 停止滚动
                                            break;
                                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: // 滚动
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
                break;
            case REQUEST_NET_ONE:
                Entity result = Parsers.getResult(data);
                if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
                    exeCommit.setEnabled(false);
                    DialogUtils.showOneBtnDialog(this, result.getResultMsg(), v -> {
                        finish();
                        DialogUtils.closeDialog();
                    });

                } else {
                    ToastUtil.shortShow(this, result.getResultMsg());
                }
                break;
        }
    }

    private void queryDetail() {
        IdentityHashMap<String,String> params = new IdentityHashMap<>();
        params.put("out_id", outId);
        requestHttpData(Constants.Urls.URL_POST_OUT_ORDER_DETAIL, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    public void toJson() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < entities.size(); i++) {
            JSONObject tmpObj = null;
            tmpObj = new JSONObject();
            try {
                tmpObj.put("goods_id", entities.get(i).getGoodsId());
                tmpObj.put("goods_num", entities.get(i).getTrueName());
                jsonArray.put(tmpObj);
                mGoods = jsonArray.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 隐藏软键盘
     */
    private void HideKeyBoard() {

        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(ExecuteOutActivity.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    private void outOrderDetail() {
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("chant_id", UserCenter.getChantId(this));
        params.put("depot_id", cabinetId);
        params.put("depot_name", cabinetName);
        params.put("relation_code", relationCode);
        params.put("goods_list", mGoods);
        params.put("depot_type", type);
        requestHttpData(Constants.Urls.URL_POST_GET_OUT_STORAGE, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void onItemStatus(boolean isFinish) {
        exeCommit.setEnabled(isFinish);
        exeCommit.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                toJson();
                if (!isFinish) {
                    return;
                }
                // 刷新适配器
                adapter.notifyDataSetChanged();
                // 遍历适配器里的map
                HashMap<String, Boolean> hashMap = adapter.getHashMap();
                for (HashMap.Entry<String, Boolean> entry : hashMap.entrySet()) {
                    Boolean value = entry.getValue();
                    if (!value) {
                        ToastUtil.showToast(ExecuteOutActivity.this, getString(R.string.toast_count_no));
                        return;
                    }
                    // 提交
                }
                outOrderDetail();

            }
        });
    }
}
