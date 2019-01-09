package code.ytn.cn.shop.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.ArrayList;
import java.util.IdentityHashMap;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.home.controller.ZxingActivity;
import code.ytn.cn.login.controller.LoginActivity;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.CounterListEntity;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.network.entity.MessageEntity;
import code.ytn.cn.network.entity.ShopHomeEntity;
import code.ytn.cn.network.entity.ShopHomeListEntity;
import code.ytn.cn.network.entity.UpdateEntity;
import code.ytn.cn.shop.adapter.ShopManagerHomeListAdapter;
import code.ytn.cn.shop.view_.BadgeView;
import code.ytn.cn.update.controller.UpdateActivity;
import code.ytn.cn.utils.CameraCanUseUtils;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.ExitManager;
import code.ytn.cn.utils.SafeSharePreferenceUtil;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CLOCKIN;
import static code.ytn.cn.common.CommonConstant.LIST;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_FOUR;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_SUCCESS;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_THREE;
import static code.ytn.cn.common.CommonConstant.STATUS_ONE;
import static code.ytn.cn.common.CommonConstant.STATUS_TWO;

/**
 * 店长首页列表
 */
public class ShopManagerHomeListActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.shop_manager_home_lv)
    private ListView lv;
    @ViewInject(R.id.left_button)
    private ImageView left;
    @ViewInject(R.id.inc_btn)
    private FloatingActionButton incBtn;
    @ViewInject(R.id.inc_left)
    private TextView incLeft;
    @ViewInject(R.id.left_text)
    private TextView leftText;
    @ViewInject(R.id.right_button_bell)
    private ImageView bell;
    @ViewInject(R.id.shop_img_rl)
    private RelativeLayout imgRl;
    private ArrayList<ShopHomeEntity> entities;
    @ViewInject(R.id.loading)
    private FrameLayout loading;

    public static final long DIFF_DEFAULT_BACK_TIME = 2000;

    private long mBackTime = -1;
    private String inStatus = "";
    private ArrayList<CounterListEntity> listEntities;
    private BadgeView badgeView;
    private LocalBroadcastManager instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_manager_home_list);
        isHasFragment = true;
        checkUpdateVersion();
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_shop_home));
        setLeftText("退出");
        // 初始化数据
        initData();
        // 初始化角标
        initBagView();
        // 加载数据
        loadData();
        // 查询消息
        queryMessage();
        // 注册广播
        registerReceiver();

        lv.setOnItemClickListener((parent, view, position, id) ->
                ShopManagerHomeActivity.startShopManagerHomeActivity(this,
                        entities.get(position).getStoreName(),
                        entities.get(position).getOperCabinet(),
                        entities.get(position).getYtotalOrder(),
                        entities.get(position).getYtotalGoods(),
                        entities.get(position).getYtotalMoney(),
                        entities.get(position).getTorder(),
                        listEntities,
                        entities.get(position).getYxdCount(),
                        entities.get(position).getTxdCount(),
                        entities.get(position).gettCount(),
                        entities.get(position).getStoreId()));
    }

    // 初始化角标
    private void initBagView() {
        badgeView = new BadgeView(this, bell);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 设置显示位置
        badgeView.setShadowLayer(2, -1, -1, Color.WHITE);
    }

    // 初始化数据
    private void initData() {
        mBtnTitleRightBell.setVisibility(View.VISIBLE);
        String clockIn = SafeSharePreferenceUtil.getString(CLOCKIN, getString(R.string.sp_sing_in));
        if (clockIn.equals(STATUS_ONE)) {
            incLeft.setText(getString(R.string.sp_sing_in));
        } else {
            incLeft.setText(getString(R.string.sp_sing_out));
        }
        left.setVisibility(View.GONE);
        incBtn.setOnClickListener(this);
        leftText.setOnClickListener(this);
        incLeft.setOnClickListener(this);
        mBtnTitleRightBell.setOnClickListener(this);
        imgRl.setOnClickListener(this);
        LinearLayout loadingLayout = (LinearLayout) loading.findViewById(R.id.loading_ll);
        loadingLayout.setOnClickListener(this);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void success(int requestCode, String data) {
        super.success(requestCode, data);
        if (REQUEST_UPDATE_VERSION_CODE == requestCode) {
            UpdateEntity update = Parsers.getUpdate(data);
            if (update != null) {
                if (REQUEST_NET_SUCCESS.equals(update.getResultCode())) {
                    String version = update.getNewVersion() == null ? "" : update.getNewVersion();
                    String url = update.getDownloadUrl() == null ? "" : update.getDownloadUrl();
                    update.setType(update.getType());
                    UpdateActivity.startUpdateActiviy(this, version, url, update.getType());
                }
            } else {
                ToastUtil.showToast(this, "检查更新失败");
            }
        } else {
            closeProgressDialog();
            ShopHomeListEntity pageEntity = Parsers.getShopHomeListEntity(data);
            switch (requestCode) {
                case REQUEST_NET_ONE:
                    if (pageEntity != null) {
                        entities = (ArrayList<ShopHomeEntity>) pageEntity.getShopHomeEntities();
                        listEntities = (ArrayList<CounterListEntity>) pageEntity.getCounterListEntities();
                        if (entities != null && entities.size() > 0) {
                            ShopManagerHomeListAdapter adapter = new ShopManagerHomeListAdapter(this, entities, this);
                            lv.setAdapter(adapter);
                        }
                        loading.setVisibility(View.GONE);
                    } else {
                        lv.setVisibility(View.GONE);
                        imgRl.setVisibility(View.VISIBLE);
                    }
                    break;
                case REQUEST_NET_THREE:
                    Entity entity = Parsers.getResult(data);
                    if (REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
                        ToastUtil.showToast(this, entity.getResultMsg());
                        String clockIn = SafeSharePreferenceUtil.getString(CLOCKIN, "");
                        if (clockIn.equals(STATUS_ONE)) {
                            incLeft.setText(getString(R.string.sp_sing_in));
                        } else {
                            incLeft.setText(getString(R.string.sp_sing_out));
                        }
                    } else {
                        ToastUtil.showToast(this, entity.getResultMsg());
                    }
                    break;
                case REQUEST_NET_FOUR:
                    MessageEntity messageEntity = Parsers.getMessageEntity(data);
                    if (messageEntity != null) {
                        if (messageEntity.getUnreadCount() != null && !messageEntity.getUnreadCount().equals("0")) {
                            if (Integer.parseInt(messageEntity.getUnreadCount()) > 99) {
                                badgeView.setText("99+");
                            }
                            badgeView.setText(messageEntity.getUnreadCount());
                            badgeView.show();

                        } else {
                            badgeView.hide();
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inc_btn:
                getCream();
                //调用相机权限判定
                if (CameraCanUseUtils.isCameraCanUse()) {
                    startActivity(new Intent(this, ZxingActivity.class)
                            .putExtra(LIST, listEntities));
                } else {
                    ToastUtil.showToast(this, "没相机权限，请到应用程序权限管理开启权限");
                    //跳转至app设置
                    getAppDetailSettingIntent();
                    return;
                }
                break;
            case R.id.inc_left: // 签入签出
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
            case R.id.left_text:
                DialogUtils.showTwoBtnExDialog(this,
                        getString(R.string.login_logout_title),
                        getString(R.string.login_logout_content),
                        null,
                        v1 -> {
                            DialogUtils.closeDialog();
                            UserCenter.cleanLoginInfo(this);
                            startActivity(new Intent(this, LoginActivity.class));
                            ExitManager.instance.exit();
                        },
                        v12 -> DialogUtils.closeDialog());
                break;
            case R.id.right_button_bell: // 通知
                startActivity(new Intent(ShopManagerHomeListActivity.this, MessageActivity.class));
                break;
            case R.id.shop_img_rl:
                loadData();
                break;
            case R.id.loading_ll:
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                break;
        }
    }

    // 注册广播接收器
    private void registerReceiver() {
        instance = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("msg");
        instance.registerReceiver(mAdDownLoadReceiver, intentFilter);
    }

    private BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String change = intent.getStringExtra("change");
            if ("message".equals(change)) {
                new Handler().post(() -> queryMessage());
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance.unregisterReceiver(mAdDownLoadReceiver);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
        queryMessage();
        String clockIn = SafeSharePreferenceUtil.getString(CLOCKIN, getString(R.string.sp_sing_in));
        if (clockIn.equals(STATUS_ONE)) {
            incLeft.setText(getString(R.string.sp_sing_in));
        } else {
            incLeft.setText(getString(R.string.sp_sing_out));
        }
    }

    // 签到
    private void clockIn() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("user_id", UserCenter.getUserId(this));
        params.put("status", inStatus);
        requestHttpData(Constants.Urls.URL_POST_STORE_SIGN, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
    }

    // 门店列表
    private void loadData() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("user_id", UserCenter.getUserId(this));
        requestHttpData(Constants.Urls.URL_POST_GET_STORE_LIST, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    // 查询消息
    private void queryMessage() {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("worker_id", UserCenter.getUserId(this));
        requestHttpData(Constants.Urls.URL_POST_QUERY_MESSAGE_LIST, REQUEST_NET_FOUR, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void onBackPressed() {
        long nowTime = System.currentTimeMillis();
        long diff = nowTime - mBackTime;
        if (diff >= DIFF_DEFAULT_BACK_TIME) {
            mBackTime = nowTime;
            Toast.makeText(getApplicationContext(), R.string.toast_back_again_exit, Toast.LENGTH_SHORT).show();
        } else {
            ExitManager.instance.exit();
        }
    }

    private void getCream() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //权限发生了改变 true  //  false 小米
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this).setTitle("title")
                        .setPositiveButton("ok", (dialog, which) -> {
                            // 请求授权
                            ActivityCompat.requestPermissions(ShopManagerHomeListActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, 1);
                        }).setNegativeButton("cancel", (dialog, which) -> {
                }).create().show();
            } else {
                ActivityCompat.requestPermissions(ShopManagerHomeListActivity.this,
                        new String[]{Manifest.permission.CAMERA}, 1);
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

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        if (entities != null) {
            entities.clear();
        }
        closeProgressDialog();
        loading.setVisibility(View.VISIBLE);
        lv.setVisibility(View.GONE);
        super.mistake(requestCode, status, errorMessage);
    }
}