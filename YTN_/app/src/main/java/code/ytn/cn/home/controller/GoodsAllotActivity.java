package code.ytn.cn.home.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.common.CommonConstant;
import code.ytn.cn.event.ChooseGoods;
import code.ytn.cn.home.adapter.ChooseCabinAndCounterAdapter;
import code.ytn.cn.home.adapter.GoodsAllotAdapter;
import code.ytn.cn.login.utils.ChooseRadio;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.network.entity.CounterEntity;
import code.ytn.cn.network.entity.CounterListEntity;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.DisplayUtils;
import code.ytn.cn.utils.NoDoubleClickListener;
import code.ytn.cn.utils.SafeSharePreferenceUtil;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.ALLOT;
import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.CHOOSE_ID;
import static code.ytn.cn.common.CommonConstant.CHOOSE_NAME;
import static code.ytn.cn.common.CommonConstant.EXTRA_FROM;
import static code.ytn.cn.common.CommonConstant.LIST;
import static code.ytn.cn.common.CommonConstant.NAME;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_SUCCESS;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;
import static code.ytn.cn.common.CommonConstant.STATUS_FOUR;
import static code.ytn.cn.common.CommonConstant.STATUS_ONE;
import static code.ytn.cn.common.CommonConstant.STATUS_TWO;
import static code.ytn.cn.common.CommonConstant.STORE_ID;
import static code.ytn.cn.common.CommonConstant.TYPE;

/**
 * 货品调拨2-11
 */
public class GoodsAllotActivity extends ToolBarActivity implements View.OnClickListener, GoodsAllotAdapter.IsONClick {

    @ViewInject(R.id.goods_allot_out_name)
    private TextView allotOutName;
    @ViewInject(R.id.goods_allot_ll)
    private LinearLayout ll;
    @ViewInject(R.id.goods_allot_input_name)
    private TextView allotInputName;
    @ViewInject(R.id.goods_allot_lv)
    private ListView lv;
    @ViewInject(R.id.goods_allot_choose)
    private TextView allotChoose;
    @ViewInject(R.id.goods_allot_comit)
    private TextView allotComit;
    @ViewInject(R.id.rg)
    private RadioGroup rg;
    @ViewInject(R.id.goods_allot_input_typeo)
    private RadioButton typeo;
    @ViewInject(R.id.goods_allot_input_typet)
    private RadioButton typet;
    private String id;
    ArrayList<ChooseGoodsListEntity> goodsList = new ArrayList<>();
    private ChooseGoods event;
    private String type = STATUS_ONE;
    private List<CounterListEntity> entities;
    private String name;
    private String choseName;
    private String cabinetName;
    @ViewInject(R.id.left_button)
    private ImageView left;
    private String mGoods;
    private String storeId;
    private String rank;

    public static void startGoodsAllotActivity(Context context,String storeId,String id,String name){
        Intent intent = new Intent(context,GoodsAllotActivity.class);
        intent.putExtra(STORE_ID,storeId);
        intent.putExtra(CABINET_ID,id);
        intent.putExtra(CABINET_NAME,name);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_allot);
        ViewInjectUtils.inject(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setCenterTitle(getString(R.string.title_goods_allot));
        rank = UserCenter.getRank(this);
        initClick();
        chooseList();

        String mType = ChooseRadio.getType(this);
        if (mType.equals(STATUS_ONE)) {
            typeo.setChecked(true);
            chooseList();
        }
        if (mType.equals(STATUS_TWO)) {
            typet.setChecked(true);
            chooseList();
        }
    }


    private void initClick() {
        allotChoose.setOnClickListener(this);
        allotComit.setOnClickListener(this);
        allotInputName.setOnClickListener(this);
        left.setOnClickListener(this);
        name = getIntent().getStringExtra(CABINET_NAME);
        storeId = getIntent().getStringExtra(STORE_ID);
        id = getIntent().getStringExtra(CABINET_ID);

        allotOutName.setText(name);
        GoodsAllotAdapter adapter = new GoodsAllotAdapter(this, goodsList, this);
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
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            if (typeo.isChecked()) {
                type = STATUS_ONE; // 货柜
                ChooseRadio.setType(GoodsAllotActivity.this, type);
                SafeSharePreferenceUtil.clearDataByKey(GoodsAllotActivity.this, CHOOSE_NAME);
                allotInputName.setText(choseName);
                chooseList();
            } else if (typet.isChecked()) {
                type = STATUS_TWO; // 仓
                ChooseRadio.setType(GoodsAllotActivity.this, type);
                SafeSharePreferenceUtil.clearDataByKey(GoodsAllotActivity.this, CHOOSE_NAME);
                allotInputName.setText(choseName);
                chooseList();
            }
        });

        choseName = SafeSharePreferenceUtil.getString(CHOOSE_NAME, "");
        allotInputName.setText(choseName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ChooseGoods stickyEvent = EventBus.getDefault().getStickyEvent(ChooseGoods.class);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void setData(ChooseGoods event) {
        this.event = event;
        setsss();
    }

    // 处理传过来的数据
    private void setsss() {
        List<ChooseGoodsListEntity> listEntities = event.getListEntities();
        for (int i = 0; i < listEntities.size(); i++) {
            ChooseGoodsListEntity goodsListEntity = listEntities.get(i);
            goodsList.add(goodsListEntity);
        }
    }

    // 隐藏软键盘
    private void HideKeyBoard() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(GoodsAllotActivity.this.getCurrentFocus()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        switch (requestCode) {
            case REQUEST_NET_ONE:
                Entity result = Parsers.getResult(data);
                if (result.getResultCode().equals(CommonConstant.REQUEST_NET_SUCCESS)) {
                    allotComit.setEnabled(false);
                    DialogUtils.showOneBtnDialog(this, result.getResultMsg(), v -> {
                        finish();
                        DialogUtils.closeDialog();
                    });
                    SafeSharePreferenceUtil.clearDataByKey(this, CHOOSE_NAME);
                    clearGoods();
                } else {
                    ToastUtil.shortShow(this, result.getResultMsg());
                }
                break;

            case REQUEST_NET_TWO:
                CounterEntity entity = Parsers.getCounter(data);
                if (entity != null) {
                    entities = entity.getCounterListEntities();
                    if (entities != null && entities.size() > 0) {
                    } else {
//                            ToastUtil.shortShow(OrderListActivity.this,getString(R.string.order_null));
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_allot_choose: // 选择商品
                Intent intent = new Intent(this, ChooseGoodsActivity.class).putExtra(CABINET_ID, id).putExtra(EXTRA_FROM, ALLOT);
                intent.putExtra(LIST, goodsList);
                intent.putExtra(TYPE, type);
                intent.putExtra(STORE_ID, storeId);
                intent.putExtra(NAME, name);
                startActivity(intent);
                finish();
                break;
            case R.id.goods_allot_input_name:
                showPopupWindow();
                break;
            case R.id.left_button:
                SafeSharePreferenceUtil.clearDataByKey(this, CHOOSE_NAME);
                SafeSharePreferenceUtil.clearDataByKey(this, CHOOSE_ID);
                ChooseRadio.cleanType(this);
                clearGoods();
                finish();
                break;
        }
    }

    private void showPopupWindow() {
        View view = View.inflate(GoodsAllotActivity.this, R.layout.popup_window, null);
        ListView mPopListView = (ListView) view.findViewById(R.id.pop_list_view);

        PopupWindow popupWindow = new PopupWindow(view, DisplayUtils.dip2px(this, 150),
                DisplayUtils.dip2px(this, 240), true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.update();
        popupWindow.showAsDropDown(allotInputName, DisplayUtils.dip2px(this, 50), 15);
        ChooseCabinAndCounterAdapter mListViewAdapter = new ChooseCabinAndCounterAdapter(this, entities, this, popupWindow);
        mPopListView.setAdapter(mListViewAdapter);
        mPopListView.setOnItemClickListener((parent, view1, position, id) -> {
            SafeSharePreferenceUtil.saveString(CHOOSE_NAME, entities.get(position).getCabinetName());
            cabinetName = entities.get(position).getCabinetName();
            allotInputName.setText(cabinetName);
            SafeSharePreferenceUtil.saveString(CHOOSE_ID, entities.get(position).getCabinetId());
            popupWindow.dismiss();
        });
    }

    // 提交
    private void commit() {
        showProgressDialog();
        String choseId = SafeSharePreferenceUtil.getString(CHOOSE_ID, "");
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("outCabinet_id", id);
        params.put("outCabinet_name", name);
        params.put("inCabinet_id", choseId);
        params.put("inCabinet_name", allotInputName.getText().toString().trim());
        params.put("worker_id", UserCenter.getUserId(this));
        params.put("worker_name", UserCenter.getName(this));
        params.put("goods_list", mGoods);
        params.put("chant_id", UserCenter.getChantId(this));
        params.put("depot_type", type);
        requestHttpData(Constants.Urls.URL_POST_GET_COMMIT_REQUISITION, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    private void chooseList() {
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        if (!rank.equals(STATUS_FOUR) && !rank.equals(STATUS_ONE)) {
            params.put("worker_id", UserCenter.getUserId(this));
        } else {
            params.put("store_id", storeId);
        }
        params.put("rank", UserCenter.getRank(this));
        params.put("chant_id", UserCenter.getChantId(this));
        params.put("depot_type", type);
        params.put("latitude", "1");
        params.put("longitude", "1");
        requestHttpData(Constants.Urls.URL_POST_CABINET_MANAGE, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    public void toJson() {

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < goodsList.size(); i++) {
            JSONObject tmpObj = null;
            tmpObj = new JSONObject();
            try {
                tmpObj.put("goods_id", goodsList.get(i).getGoodsId());
                tmpObj.put("out_num", goodsList.get(i).getSaleCount());
                jsonArray.put(tmpObj);
                mGoods = jsonArray.toString();
                // 获取输入商品数量

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onItemStatus(boolean isFinish) {
        allotComit.setEnabled(isFinish);
        allotComit.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                toJson();
                if (isFinish == false) {
                    return;
                }
                for (int i = 0; i < goodsList.size(); i++) {
                    if (goodsList.get(i).getSaleCount().equals(REQUEST_NET_SUCCESS)) {
                        ToastUtil.shortShow(GoodsAllotActivity.this, "调出商品数量不能为0");
                        return;
                    }

                }

                if (goodsList == null || goodsList.size() == 0) {
                    ToastUtil.shortShow(GoodsAllotActivity.this, getString(R.string.toast_choose_goods));
                    return;
                }
                if (allotInputName.getText().toString().equals("") || allotInputName.getText().toString() == null) {
                    ToastUtil.shortShow(GoodsAllotActivity.this, getString(R.string.toast_choose_input_allot_counter_and_bin));
                    return;
                }
                if (allotInputName.getText().toString().equals(allotOutName.getText().toString())) {
                    ToastUtil.shortShow(GoodsAllotActivity.this, getString(R.string.toast_agin_choose));
                    return;
                }
                commit();
            }
        });

    }

    // 取消上次选中状态
    private void clearGoods() {
        if (goodsList != null && goodsList.size() > 0) {
            for (ChooseGoodsListEntity chooseGoodsListEntity : event.getListEntities()) {
                String goodsId = chooseGoodsListEntity.getGoodsId();
                SafeSharePreferenceUtil.saveBoolean(goodsId, false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearGoods();
        ChooseRadio.cleanType(this);
        SafeSharePreferenceUtil.clearDataByKey(this, CHOOSE_NAME);
        SafeSharePreferenceUtil.clearDataByKey(this, CHOOSE_ID);
        finish();
    }
}