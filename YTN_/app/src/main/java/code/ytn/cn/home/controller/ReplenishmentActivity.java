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

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.common.CommonConstant;
import code.ytn.cn.home.adapter.ReplenishmentAdapter;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.ChooseGoodsEntity;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.NoDoubleClickListener;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;

/**
 * 申请补货
 */
public class ReplenishmentActivity extends ToolBarActivity {

    @ViewInject(R.id.replment_name)
    private TextView replmentName;
    @ViewInject(R.id.replment_lv)
    private ListView lv;
    @ViewInject(R.id.replment_comit)
    private TextView replmentCommit;
    private String cabinetId;
    private String cabinetName;
    private String mGoods;
    private List<ChooseGoodsListEntity> listEntities;
    private ArrayList<ChooseGoodsListEntity> list = new ArrayList<>();

    public static void startReplenishmentActivity(Context context, String cabinetId, String cabinetName) {
        Intent intent = new Intent(context, ReplenishmentActivity.class);
        intent.putExtra(CABINET_ID, cabinetId);
        intent.putExtra(CABINET_NAME, cabinetName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replenishment);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_replment_apply));
        initData();
        queryGoodsList();
        replmentCommit.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                list.clear();
                for (int i = 0; i < listEntities.size(); i++) {
                    if (listEntities.get(i).getReplmentCount() != null && !"".equals(listEntities.get(i).getReplmentCount().trim())) {
                        ChooseGoodsListEntity goodsListEntity = listEntities.get(i);
                        list.add(goodsListEntity);
                    }
                }
                if (list != null && list.size() > 0) {
                    toJson();
                    commit();
                } else {
                    ToastUtil.showToast(ReplenishmentActivity.this, "请输入需要补货的商品数量");
                }
            }
        });

    }

    private void initData() {
        cabinetId = getIntent().getStringExtra(CABINET_ID);
        cabinetName = getIntent().getStringExtra(CABINET_NAME);
        replmentName.setText(cabinetName);

    }

    /**
     * 隐藏软键盘
     */
    private void HideKeyBoard() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ReplenishmentActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        switch (requestCode) {
            case REQUEST_NET_ONE:
                Entity result = Parsers.getResult(data);
                if (result.getResultCode().equals(CommonConstant.REQUEST_NET_SUCCESS)) {
                    replmentCommit.setEnabled(false);
                    DialogUtils.showOneBtnDialog(this, result.getResultMsg(), v -> {
                        finish();
                        DialogUtils.closeDialog();
                    });
                } else {
                    ToastUtil.shortShow(this, result.getResultMsg());
                }
                break;
            case REQUEST_NET_TWO:
                ChooseGoodsEntity goodsEntity = Parsers.getChooseGoodsEntity(data);
                if (goodsEntity != null) {
                    listEntities = goodsEntity.getListEntities();
                    if (listEntities != null && listEntities.size() > 0) {
                        ReplenishmentAdapter adapter = new ReplenishmentAdapter(this, listEntities);
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
                    }
                }
                break;
        }
    }

    // 提交补货
    private void commit() {
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("cabinet_id", cabinetId);
        params.put("cabinet_name", cabinetName);
        params.put("create_worker_name", UserCenter.getName(this));
        params.put("goods_list", mGoods);
        params.put("create_worker_id", UserCenter.getUserId(this));
        requestHttpData(Constants.Urls.URL_POST_ADD_GOODS, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    // 查询商品
    private void queryGoodsList() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("cabinet_id", cabinetId);
        requestHttpData(Constants.Urls.URL_POST_CABINET_GOODSLIST, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    public void toJson() {

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject tmpObj = null;
            tmpObj = new JSONObject();
            try {
                tmpObj.put("goods_id", list.get(i).getGoodsId());
                tmpObj.put("goods_num", list.get(i).getReplmentCount());
                jsonArray.put(tmpObj);
                mGoods = jsonArray.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
