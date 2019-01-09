package code.ytn.cn.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import code.ytn.cn.home.adapter.ExecuteInputAdapter;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.network.entity.InGoodsListEntity;
import code.ytn.cn.network.entity.InputGoodsDetailEntity;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.NoDoubleClickListener;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.ID;
import static code.ytn.cn.common.CommonConstant.NUM;
import static code.ytn.cn.common.CommonConstant.PRO_NAME;
import static code.ytn.cn.common.CommonConstant.RELATION_CODE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;
import static code.ytn.cn.common.CommonConstant.TIME;

/**
 * 执行入库2-6
 */
public class ExecuteInputActivity extends ToolBarActivity implements ExecuteInputAdapter.IsOnClick {

    @ViewInject(R.id.execute_input_num)
    private TextView exeNum;
    @ViewInject(R.id.execute_input_name)
    private TextView exeName;
    @ViewInject(R.id.execute_input_time)
    private TextView exeTime;
    @ViewInject(R.id.execute_input_person_zd)
    private TextView exeZd;
    //    @ViewInject(R.id.execute_input_person_cz)
//    private TextView exeCz;
    @ViewInject(R.id.execute_input_lv)
    private ListView lv;
    @ViewInject(R.id.execute_input_comit)
    private TextView exeComit;
    private String code;
    private List<InGoodsListEntity> entities;
    private ExecuteInputAdapter adapter;
    private String id;
    private String cabinetId;
    private String relaCode;
    private String cabinetName;
    private String mGoods;

    public static void startExecuteInputActivity(Context context, String num, String time, String cabiName, String proName, String cabinetID, String id, String relaCode) {
        Intent intent = new Intent(context, ExecuteInputActivity.class);
        intent.putExtra(NUM, num);
        intent.putExtra(TIME, time);
        intent.putExtra(CABINET_NAME, cabiName);
        intent.putExtra(PRO_NAME, proName);
        intent.putExtra(CABINET_ID, cabinetID);
        intent.putExtra(ID, id);
        intent.putExtra(RELATION_CODE, relaCode);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_execute_input);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_extcu_input));
        initData();
        queryDetail();
    }

    // 初始化数据
    private void initData() {
        code = getIntent().getStringExtra(NUM);
        String time = getIntent().getStringExtra(TIME);
        cabinetName = getIntent().getStringExtra(CABINET_NAME);
        String proName = getIntent().getStringExtra(PRO_NAME);
        id = getIntent().getStringExtra(ID);
        cabinetId = getIntent().getStringExtra(CABINET_ID);
        relaCode = getIntent().getStringExtra(RELATION_CODE);

        exeNum.setText(code);
        exeName.setText(cabinetName);
        exeTime.setText(time);
        exeZd.setText(proName);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        InputGoodsDetailEntity entity = Parsers.getInputGoodsDetail(data);
        switch (requestCode) {
            case REQUEST_NET_TWO:
                if (entity != null) {
                    entities = entity.getListEntities();
                    if (entities != null && entities.size() > 0) {
                        if (entities != null && entities.size() > 0) {
                            adapter = new ExecuteInputAdapter(this, entities);
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
                break;
            case REQUEST_NET_ONE:
                Entity result = Parsers.getResult(data);
                if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
                    exeComit.setEnabled(false);
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
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("in_id", id);
        requestHttpData(Constants.Urls.URL_POST_IN_ORDERLIST_DETAIL, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    // 执行入库
    private void inStorage() {
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("code", code);
        params.put("relation_code", relaCode);
        params.put("depot_name", cabinetName);
        params.put("depot_id", cabinetId);
        params.put("operator", UserCenter.getName(this));
        params.put("goods_list", mGoods);
        params.put("chant_id", UserCenter.getChantId(this));
        params.put("depot_type", "1");
        requestHttpData(Constants.Urls.URL_POST_GET_IN_STORAGE, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }


    /**
     * 隐藏软键盘
     */
    private void HideKeyBoard() {

        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(ExecuteInputActivity.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public void toJson() {

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < entities.size(); i++) {
            JSONObject tmpObj = null;
            tmpObj = new JSONObject();
            try {
                tmpObj.put("goods_id", entities.get(i).getGoodsId());
                tmpObj.put("goods_num", entities.get(i).getTinNum());
                jsonArray.put(tmpObj);
                mGoods = jsonArray.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemStatus(boolean isFinish) {
        exeComit.setEnabled(isFinish);
        exeComit.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                toJson();
                // 输入框有一个为空就return掉.
                if (!isFinish) {
                    return;
                }
                adapter.notifyDataSetChanged();
                HashMap<String, Boolean> hashMap = adapter.getHashMap();
                for (HashMap.Entry<String, Boolean> entry : hashMap.entrySet()) {
                    Boolean value = entry.getValue();
                    if (!value) {
                        ToastUtil.shortShow(ExecuteInputActivity.this, getString(R.string.toast_count_in_no));
                        return;
                    }
                }
                // 执行入库
                inStorage();
            }
        });
    }
}
