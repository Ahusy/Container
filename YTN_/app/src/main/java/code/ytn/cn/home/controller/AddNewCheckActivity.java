package code.ytn.cn.home.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import code.ytn.cn.home.adapter.AddNewCheckAdapter;
import code.ytn.cn.home.adapter.ChooseCabinAndCounterAdapter;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.ChooseGoodsEntity;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.network.entity.CounterEntity;
import code.ytn.cn.network.entity.CounterListEntity;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.DisplayUtils;
import code.ytn.cn.utils.NoDoubleClickListener;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_FOUR;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_THREE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;
import static code.ytn.cn.common.CommonConstant.STATUS_FOUR;
import static code.ytn.cn.common.CommonConstant.STATUS_ONE;
import static code.ytn.cn.common.CommonConstant.STORE_ID;

/**
 * 添加新盘点2-14
 */
public class AddNewCheckActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.addnew_check_name)
    private TextView addNewName;
    @ViewInject(R.id.addnew_check_comit)
    private TextView addNewComit;
    @ViewInject(R.id.addnew_allot_lv)
    private ListView lv;
    @ViewInject(R.id.right_button)
    private ImageView right;
    @ViewInject(R.id.left_button)
    private ImageView left;
    @ViewInject(R.id.addnew_check_damage)
    private String id;
    private String mGoods;
    private int mDamageCount;
    private int msaleCount;
    private int count;
    private String damageGoods;
    private List<CounterListEntity> entities;
    private String name;
    private String cabinetId;
    private String cabinetName;
    private AlertDialog dialog;
    private TextView contentTv;
    private TextView titleTv;
    private String rank;
    private String storeId;
    private List<ChooseGoodsListEntity> listEntities;
    private ArrayList<ChooseGoodsListEntity> list = new ArrayList<>();
    private ChooseGoodsListEntity goodsListEntity;
    private String damageList;
    private boolean isDamage;

    public static void startAddNewCheckActivity(Context context,String cabinetName,String cabinetId,String storeId){
        Intent intent = new Intent(context,AddNewCheckActivity.class);
        intent.putExtra(CABINET_NAME,cabinetName);
        intent.putExtra(CABINET_ID,cabinetId);
        intent.putExtra(STORE_ID,storeId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_check);
        ViewInjectUtils.inject(this);
        rank = UserCenter.getRank(this);
        setCenterTitle(getString(R.string.title_addcheck));
        initClick();
        queryGoodsList();
        chooseList();

        addNewComit.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                toJson();
                list.clear();
                for (int i = 0; i < listEntities.size(); i++) {
                    if (listEntities.get(i).getSaleCount() != null && !"".equals(listEntities.get(i).getSaleCount().trim())) {
                        goodsListEntity = listEntities.get(i);
                        list.add(goodsListEntity);
                        damageList = goodsListEntity.getDamageCount();
                        if (!isDamage) {
                            if (damageList != null && !damageList.equals("")) {
                                isDamage = true;
                            } else {
                                isDamage = false;
                            }
                        }
                    }
                }
                if (list != null && list.size() > 0) {
                    toJson();
                    chooseList();
                    if (isDamage) {
                        showAddNewCheckDialog();
                    } else {
                        commit();
                    }
                } else {
                    ToastUtil.showToast(AddNewCheckActivity.this, "请输入要盘点的商品数量");
                }
            }
        });

    }

    private void initClick() {
        right.setVisibility(View.GONE);
        id = getIntent().getStringExtra(CABINET_ID);
        name = getIntent().getStringExtra(CABINET_NAME);
        storeId = getIntent().getStringExtra(STORE_ID);
        addNewName.setText(name);
    }

    /**
     * 隐藏软键盘
     */
    private void HideKeyBoard() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(AddNewCheckActivity.this.getCurrentFocus()
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
                    addNewComit.setEnabled(false);
                    DialogUtils.showOneBtnDialog(this, result.getResultMsg(), v -> {
//                        clearGoods();
                        finish();
                        DialogUtils.closeDialog();
                    });
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
            case REQUEST_NET_THREE:
                break;
            case REQUEST_NET_FOUR:
                ChooseGoodsEntity goodsEntity = Parsers.getChooseGoodsEntity(data);
                if (goodsEntity != null) {
                    listEntities = goodsEntity.getListEntities();
                    if (listEntities != null && listEntities.size() > 0) {
                        AddNewCheckAdapter adapter = new AddNewCheckAdapter(this, (ArrayList<ChooseGoodsListEntity>) listEntities);
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

    public void toJson() {

        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayDamage = new JSONArray();
        mDamageCount = 0;
        msaleCount = 0;
        count = 0;
        for (int i = 0; i < list.size(); i++) {
            JSONObject tmpObj = null;
            tmpObj = new JSONObject();

            JSONObject tmpObjDamage = null;
            tmpObjDamage = new JSONObject();
            try {
                tmpObj.put("goods_id", list.get(i).getGoodsId());
                tmpObj.put("goods_name", list.get(i).getGoodsName());
                tmpObj.put("damage_count", list.get(i).getDamageCount());
                tmpObj.put("sale_count", list.get(i).getSaleCount());
                tmpObj.put("goods_count", list.get(i).getStock());
                jsonArray.put(tmpObj);
                mGoods = jsonArray.toString();

                tmpObjDamage.put("goods_id", list.get(i).getGoodsId());
                tmpObjDamage.put("out_num", list.get(i).getDamageCount());
                jsonArrayDamage.put(tmpObjDamage);
                damageGoods = jsonArrayDamage.toString();
                // 获取输入商品数量
                String damageCount = list.get(i).getDamageCount();
                if (damageCount != null && !damageCount.equals("")) {
                    int damage = Integer.parseInt(damageCount);
                    // 顺坏商品总数
                    mDamageCount += damage;
                }
                String saleCount = list.get(i).getSaleCount();
                int sale = Integer.parseInt(saleCount);
                // 在售商品总数
                msaleCount += sale;
                // 品种数
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 提交盘点
    private void commit() {
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("cabinet_id", id);
        params.put("goods_list", mGoods);
        params.put("user_id", UserCenter.getUserId(this));
        params.put("damage_total", String.valueOf(mDamageCount));
        params.put("sale_total", String.valueOf(msaleCount));
        params.put("sale_class_total", String.valueOf(count));
        requestHttpData(Constants.Urls.URL_POST_GET_REFER_CHECKORDER, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    // 选择调入仓
    private void chooseList() {
//        showProgressDialog();
        IdentityHashMap<String,String> params = new IdentityHashMap<>();
        if (!rank.equals(STATUS_FOUR) && !rank.equals(STATUS_ONE)) {
            params.put("worker_id", UserCenter.getUserId(this));
        } else {
            params.put("store_id", storeId);
        }
        params.put("chant_id", UserCenter.getChantId(this));
        params.put("rank", UserCenter.getRank(this));
        params.put("depot_type", "2");
        params.put("latitude", "1");
        params.put("longitude", "1");
        requestHttpData(Constants.Urls.URL_POST_CABINET_MANAGE, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    // 调拨
    private void allot() {
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("outCabinet_id", id);
        params.put("outCabinet_name", name);
        params.put("inCabinet_id", cabinetId);
        params.put("inCabinet_name", cabinetName);
        params.put("worker_id", UserCenter.getUserId(this));
        params.put("worker_name", UserCenter.getName(this));
        params.put("goods_list", damageGoods);
        params.put("chant_id", UserCenter.getChantId(this));
        params.put("depot_type", "2");
        requestHttpData(Constants.Urls.URL_POST_GET_COMMIT_REQUISITION, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
    }

    private void queryGoodsList() {
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("cabinet_id", id);
        requestHttpData(Constants.Urls.URL_POST_CABINET_GOODSLIST, REQUEST_NET_FOUR, FProtocol.HttpMethod.POST, params);
    }

    private void showAddNewCheckDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_addnew_button, null);
        dialog = new AlertDialog.Builder(this).setView(view).create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        titleTv = (TextView) view.findViewById(R.id.dialog_addnew_title);
        contentTv = (TextView) view.findViewById(R.id.dialog_addnew_content);
        TextView btnTv = (TextView) view.findViewById(R.id.dialog_addnew_btn);

        contentTv.setOnClickListener(v -> {
            showPopupWindow();
            contentTv.setHeight(200);
        });

        btnTv.setOnClickListener(v -> {
            if (contentTv.getText().toString().equals(getString(R.string.choose_bank_card))) {
                ToastUtil.showToast(AddNewCheckActivity.this, getString(R.string.toast_choose_cang));
                return;
            }
            dialog.dismiss();
            allot();
            commit();
        });

        dialog.setCancelable(true);
        dialog.show();

    }

    private void showPopupWindow() {
        View view = View.inflate(AddNewCheckActivity.this, R.layout.popup_window, null);
        ListView mPopListView = (ListView) view.findViewById(R.id.pop_list_view);
        PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.update();
        popupWindow.setOnDismissListener(() -> contentTv.setHeight(70));
        popupWindow.showAsDropDown(titleTv, DisplayUtils.dip2px(this, 10), 0);
        ChooseCabinAndCounterAdapter mListViewAdapter = new ChooseCabinAndCounterAdapter(this, entities, this, popupWindow);
        mPopListView.setAdapter(mListViewAdapter);
        mPopListView.setOnItemClickListener((parent, view1, position, id1) -> {
            contentTv.setHeight(70);
            cabinetId = entities.get(position).getCabinetId();
            cabinetName = entities.get(position).getCabinetName();
            contentTv.setText(cabinetName);
            popupWindow.dismiss();
        });
    }

    @Override
    public void onClick(View v) {

    }
}
