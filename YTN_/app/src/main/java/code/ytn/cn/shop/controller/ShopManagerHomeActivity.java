package code.ytn.cn.shop.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.ArrayList;
import java.util.IdentityHashMap;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.home.controller.CounterListActivity;
import code.ytn.cn.home.controller.OrderQueryActivity;
import code.ytn.cn.home.controller.ZxingActivity;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.CounterListEntity;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.network.entity.ShopHomeDetailEntity;
import code.ytn.cn.utils.CameraCanUseUtils;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.SafeSharePreferenceUtil;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CLOCKIN;
import static code.ytn.cn.common.CommonConstant.EXTRA_TYPE;
import static code.ytn.cn.common.CommonConstant.FROM_ACT_TWO;
import static code.ytn.cn.common.CommonConstant.LIST;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_SUCCESS;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_THREE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;
import static code.ytn.cn.common.CommonConstant.STATUS_ONE;
import static code.ytn.cn.common.CommonConstant.STATUS_TWO;
import static code.ytn.cn.common.CommonConstant.STORE_CABINET;
import static code.ytn.cn.common.CommonConstant.STORE_ID;
import static code.ytn.cn.common.CommonConstant.STORE_NAME;
import static code.ytn.cn.common.CommonConstant.TO_ORDER;
import static code.ytn.cn.common.CommonConstant.TXD_COUNT;
import static code.ytn.cn.common.CommonConstant.T_COUNT;
import static code.ytn.cn.common.CommonConstant.YTO_GOODS;
import static code.ytn.cn.common.CommonConstant.YTO_MONEY;
import static code.ytn.cn.common.CommonConstant.YTO_ORDER;
import static code.ytn.cn.common.CommonConstant.YXD_COUNT;

/**
 * 店长首页
 */
public class ShopManagerHomeActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.shop_name)
    private TextView shopName;
    @ViewInject(R.id.shop_status)
    private TextView shopStatus;
    @ViewInject(R.id.shop_counter)
    private TextView shopCounter;
    @ViewInject(R.id.yesterday_order)
    private TextView yesdayOrder;
    @ViewInject(R.id.yesterday_sell_goods)
    private TextView yesdaySellGoods;
    @ViewInject(R.id.yesterday_deal_limit)
    private TextView yesdayDealLimit;
    @ViewInject(R.id.today_order)
    private TextView todayOrder;
    @ViewInject(R.id.today_deal_limit)
    private TextView todayDealLimit;
    @ViewInject(R.id.shop_order_query)
    private ImageView imgOrderQuery;
    @ViewInject(R.id.shop_is_store)
    private ImageView imgIsStore;
    @ViewInject(R.id.shop_goods_sell)
    private ImageView imgGoodsSell;
    @ViewInject(R.id.shop_query_counter)
    private ImageView imgQueryCounter;
    @ViewInject(R.id.inc_btn)
    private FloatingActionButton incBtn;
    @ViewInject(R.id.inc_left)
    private TextView incLeft;
    private String storeId;
    @ViewInject(R.id.rigth_text)
    private TextView rightText;
    @ViewInject(R.id.left_button)
    private ImageView left;
    @ViewInject(R.id.today_miss_order)
    private TextView tmissOrder;
    @ViewInject(R.id.yesterday_miss_order)
    private TextView ymissOrder;
    @ViewInject(R.id.today_sell_goods)
    private TextView todaySellGoods;
    @ViewInject(R.id.shop_look_miss_order)
    private ImageView shopMissOrder;

    private String status = ""; // 开闭店
    private String inStatus = ""; // 签入
    private ArrayList<CounterListEntity> list = new ArrayList<>();

    public static void startShopManagerHomeActivity(Context context, String storeName, String storeCabinet, String ytoOrder, String ytoGoods, String ytoMoney, String toOrder, ArrayList<CounterListEntity> list, String yxdCount, String txdCount, String tCount, String storeId){
        Intent intent = new Intent(context,ShopManagerHomeActivity.class);
        intent.putExtra(STORE_NAME,storeName);
        intent.putExtra(STORE_CABINET,storeCabinet);
        intent.putExtra(YTO_ORDER,ytoOrder);
        intent.putExtra(YTO_GOODS,ytoGoods);
        intent.putExtra(YTO_MONEY,ytoMoney);
        intent.putExtra(TO_ORDER,toOrder);
        intent.putExtra(LIST,list);
        intent.putExtra(YXD_COUNT,yxdCount);
        intent.putExtra(TXD_COUNT,txdCount);
        intent.putExtra(T_COUNT,tCount);
        intent.putExtra(STORE_ID,storeId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_manager_home);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_one_home));
        initData();
        getStoreDetail();
    }

    private void initData() {
        String clockIn = SafeSharePreferenceUtil.getString(CLOCKIN, getString(R.string.sp_sing_in));
        if (clockIn.equals(STATUS_ONE)) {
            incLeft.setText(getString(R.string.sp_sing_in));
        } else {
            incLeft.setText(getString(R.string.sp_sing_out));
        }

        storeId = getIntent().getStringExtra(STORE_ID);
        SafeSharePreferenceUtil.saveString("storeId", storeId);
        shopName.setText(getIntent().getStringExtra(STORE_NAME));
        shopCounter.setText(getIntent().getStringExtra(STORE_CABINET));
        yesdayOrder.setText(getIntent().getStringExtra(YTO_ORDER));
        yesdaySellGoods.setText(getIntent().getStringExtra(YTO_GOODS));
        yesdayDealLimit.setText("￥" + getIntent().getStringExtra(YTO_MONEY));
        ymissOrder.setText(getIntent().getStringExtra(YXD_COUNT));
        tmissOrder.setText(getIntent().getStringExtra(TXD_COUNT));
        todaySellGoods.setText(getIntent().getStringExtra(T_COUNT));
        shopStatus.setOnClickListener(this);
        imgOrderQuery.setOnClickListener(this);
        imgQueryCounter.setOnClickListener(this);
        incBtn.setOnClickListener(this);
        imgIsStore.setOnClickListener(this);
        imgGoodsSell.setOnClickListener(this);
        rightText.setOnClickListener(this);
        shopMissOrder.setOnClickListener(this);
        incLeft.setOnClickListener(this);
        list = (ArrayList<CounterListEntity>) getIntent().getSerializableExtra(LIST);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop_order_query: // 订单查询
                startActivity(new Intent(this, OrderQueryActivity.class).putExtra("store_id", storeId).putExtra(EXTRA_TYPE, FROM_ACT_TWO));
                break;
            case R.id.shop_query_counter: // 查看货柜
                startActivity(new Intent(this, CounterListActivity.class).putExtra("store_id", storeId).putExtra(EXTRA_TYPE, FROM_ACT_TWO));
                break;
            case R.id.inc_left:
                if (incLeft.getText().toString().equals(getString(R.string.sp_sing_in))) {
                    inStatus = STATUS_TWO;
                    SafeSharePreferenceUtil.saveString(CLOCKIN, STATUS_TWO);
                    clockIn();
                } else if (incLeft.getText().toString().equals(getString(R.string.sp_sing_out))) {
                    inStatus = STATUS_ONE;
                    SafeSharePreferenceUtil.saveString(CLOCKIN, STATUS_ONE);
                    clockIn();
                }

                break;
            case R.id.inc_btn:
                getCream();
                //调用相机权限判定
                if (CameraCanUseUtils.isCameraCanUse()) {
                    startActivity(new Intent(this, ZxingActivity.class).putExtra(LIST, list));
                } else {
                    ToastUtil.showToast(this, "没相机权限，请到应用程序权限管理开启权限");
                    //跳转至app设置
                    getAppDetailSettingIntent();
                    return;
                }
                break;
            case R.id.shop_is_store: // 操作开店
                if (shopStatus.getText().toString().equals(getString(R.string.text_open_store_ing))) {
                    DialogUtils.showTwoBtnDialog(this, "操作闭店", "是否确认操作闭店？\n闭店后货柜将无法销售并生成日结单", (View v1) -> {
                                status = STATUS_TWO;
                                changeStatus();
                                DialogUtils.closeDialog();
                            },
                            v2 -> DialogUtils.closeDialog());
                } else {
                    DialogUtils.showTwoBtnDialog(this, "操作开店", "是否确认操作开店？", v1 -> {
                                status = STATUS_ONE;
                                changeStatus();
                                DialogUtils.closeDialog();
                            },
                            v2 -> DialogUtils.closeDialog());
                }
                break;
            case R.id.shop_goods_sell: // 商品售卖推荐
                startActivity(new Intent(this, SalesRankActivity.class).putExtra(STORE_ID, storeId));
                break;
            case R.id.shop_look_miss_order: // 查看消单
                startActivity(new Intent(this, OrderMissActivity.class).putExtra(STORE_ID, storeId));
                break;
        }
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        switch (requestCode) {
            case REQUEST_NET_ONE:
                ShopHomeDetailEntity entity = Parsers.getShopHomeDetailEntity(data);
                if (entity != null) {
                    String Estatus = entity.getStatus();
                    String statusName = "";
                    todayOrder.setText(entity.getOrderCount());
                    todayDealLimit.setText("￥" + entity.getTotal());
                    if (Estatus.equals(STATUS_ONE)) {
                        shopStatus.setBackground(getResources().getDrawable(R.drawable.counter_bt_footer_green));
                        imgIsStore.setImageResource(R.drawable.close_store);
                        shopStatus.setText(R.string.text_open_store_ing);
                    } else {
                        imgIsStore.setImageResource(R.drawable.open_store);
                        shopStatus.setBackground(getResources().getDrawable(R.drawable.counter_bt_footer_red));
                        shopStatus.setText(R.string.text_close_store_ing);
                    }
                }
                break;
            case REQUEST_NET_THREE:
                Entity result = Parsers.getResult(data);
//                getStoreDetail();
                if (result.getResultCode().equals(REQUEST_NET_SUCCESS)) {
                    ToastUtil.shortShow(this, result.getResultMsg());
                    String clockIn = SafeSharePreferenceUtil.getString(CLOCKIN, "");
                    if (clockIn.equals(STATUS_ONE)) {
                        incLeft.setText(getString(R.string.sp_sing_in));
//                        UserCenter.cleanLoginInfo(this);
//                        startActivity(new Intent(this, LoginActivity.class));
//                        ExitManager.instance.exit();
                    } else {
                        incLeft.setText(getString(R.string.sp_sing_out));
                    }
                } else {
                    ToastUtil.showToast(this, result.getResultMsg());
                }
                break;
            case REQUEST_NET_TWO:
                getStoreDetail();
                Entity entity1 = Parsers.getResult(data);
                if (REQUEST_NET_SUCCESS.equals(entity1.getResultCode())) {
                    ToastUtil.showToast(this, entity1.getResultMsg());
                } else {
                    ToastUtil.showToast(this, entity1.getResultMsg());
                }
        }
    }

    private void getStoreDetail() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("store_id", storeId);
        params.put("user_id", UserCenter.getUserId(this));
        requestHttpData(Constants.Urls.URL_POST_GET_STORE_DETAIL, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    // 开店闭店
    private void changeStatus() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("store_id", storeId);
        params.put("status", status);
        requestHttpData(Constants.Urls.URL_POST_CHANGE_STAUTS, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    // 打卡
    private void clockIn() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("user_id", UserCenter.getUserId(this));
        params.put("status", inStatus);
        requestHttpData(Constants.Urls.URL_POST_STORE_SIGN, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStoreDetail();
    }

    private void getCream() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //权限发生了改变 true  //  false 小米
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this).setTitle("title")
                        .setPositiveButton("ok", (dialog, which) -> {
                            // 请求授权
                            ActivityCompat.requestPermissions(ShopManagerHomeActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                        }).setNegativeButton("cancel", (dialog, which) -> {

                }).create().show();

            } else {
                ActivityCompat.requestPermissions(ShopManagerHomeActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            }

        }
    }

    /**
     * @param permissions  请求的权限
     * @param grantResults 请求权限返回的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            // camear 权限回调
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 表示用户授权
                ToastUtil.showToast(this, getString(R.string.permission_sure));
            } else {
                //用户拒绝权限
                ToastUtil.showToast(this, getString(R.string.permission_no));
            }
        }
    }

    /**
     * 相机权限设置
     * 跳转至设置页面
     */
    @SuppressLint("ObsoleteSdkInt")
    private void getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }
}
