package code.ytn.cn.home.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import code.ytn.cn.home.adapter.CounterAdapter;
import code.ytn.cn.login.controller.LoginActivity;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.CounterEntity;
import code.ytn.cn.network.entity.CounterListEntity;
import code.ytn.cn.network.entity.MessageEntity;
import code.ytn.cn.network.entity.UpdateEntity;
import code.ytn.cn.shop.controller.MessageActivity;
import code.ytn.cn.shop.view_.BadgeView;
import code.ytn.cn.update.controller.UpdateActivity;
import code.ytn.cn.utils.CameraCanUseUtils;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.ExitManager;
import code.ytn.cn.utils.PermissionUtils;
import code.ytn.cn.utils.SafeSharePreferenceUtil;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.REQUEST_NET_FOUR;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_SUCCESS;
import static code.ytn.cn.common.CommonConstant.STATUS_FOUR;
import static code.ytn.cn.common.CommonConstant.STATUS_ONE;

/**
 * 货柜管理list2-1
 */
public class CounterListActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.counter_lv)
    private ListView lv;
    @ViewInject(R.id.toolbar_title)
    private TextView title;
    @ViewInject(R.id.rigth_text)
    private TextView right;
    @ViewInject(R.id.left_text)
    private TextView leftText;
    @ViewInject(R.id.inc_btn)
    private FloatingActionButton btn;
    @ViewInject(R.id.counter_img_rl)
    private RelativeLayout imgRl;
    @ViewInject(R.id.no_wifi)
    private FrameLayout noWifi;

    @ViewInject(R.id.right_button_bell)
    private ImageView bell;
    private BadgeView badgeView;
    private LocalBroadcastManager instance;

    public static final long DIFF_DEFAULT_BACK_TIME = 2000;

    private long mBackTime = -1;
    private ArrayList<CounterListEntity> entities;
    private String storeId;
    private String rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        rank = UserCenter.getRank(this);
        if (!rank.equals(STATUS_FOUR) && !rank.equals(STATUS_ONE)) {
            // 隐藏返回按钮
            mBtnTitleLeft.setVisibility(View.GONE);
            mBtnTitleRightBell.setVisibility(View.VISIBLE);
            setLeftText("退出");
            isHasFragment = true;
            checkUpdateVersion();
            // 初始化角标
            initBagView();
            // 查询消息
            queryMessage();
            // 注册广播
//            registerReceiver();
        }
        // 初始化数据
        initData();
        // 加载数据
        loadData();

        lv.setOnItemClickListener((parent, view, position, id) -> {
            getCream();
            CounterActivity.startCounterActivity(this,
                    entities.get(position).getCabinetCode(),
                    entities.get(position).getCabinetName(),
                    entities.get(position).getAddress(),
                    entities.get(position).getCabinetId(),
                    entities.get(position).getStatus(),
                    entities.get(position).getDistance(),
                    storeId,
                    entities.get(position).getGoodsNum(),
                    entities.get(position).getOpenDepot(),
                    entities.get(position).getDepotName());
        });
    }

    private void initBagView() {
        mBtnTitleRightBell.setOnClickListener(this);
        badgeView = new BadgeView(this, bell);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 设置显示位置
        badgeView.setShadowLayer(2, -1, -1, Color.WHITE);
    }

    private void initData() {
        storeId = getIntent().getStringExtra("store_id");
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_counter));
//        setRightText(getString(R.string.right_title_language)+":"+getString(R.string.right_title_chin));

        // 获取权限
        PermissionUtils.requestPermissions(this, REQUEST_PERMISSION_CODE, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE);
        title.setOnClickListener(this);
        right.setOnClickListener(this);
        leftText.setOnClickListener(this);
        btn.setOnClickListener(this);
        imgRl.setOnClickListener(this);
        LinearLayout noWifiLayput = (LinearLayout) noWifi.findViewById(R.id.loading_ll);
        noWifiLayput.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        if (!rank.equals(STATUS_FOUR) && !rank.equals(STATUS_ONE)) {
            queryMessage();
        }
    }

    // 注册广播接收器
//    private void registerReceiver() {
//        instance = LocalBroadcastManager.getInstance(this);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("msg");
//        instance.registerReceiver(mAdDownLoadReceiver, intentFilter);
//    }
//
//    private BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String change = intent.getStringExtra("change");
//            if ("message".equals(change)) {
//                if (!rank.equals(STATUS_FOUR) && !rank.equals(STATUS_ONE)) {
//                    new Handler().post(() -> queryMessage());
//                }
//            }
//        }
//    };
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        instance.unregisterReceiver(mAdDownLoadReceiver);
//    }

    // 货柜列表
    private void loadData() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        if (!rank.equals(STATUS_FOUR) && !rank.equals(STATUS_ONE)) {
            params.put("worker_id", UserCenter.getUserId(this));
        } else {
            params.put("store_id", storeId);
        }
        params.put("depot_type", STATUS_ONE);
        params.put("chant_id", UserCenter.getChantId(this));
        params.put("rank", UserCenter.getRank(this));
        requestHttpData(Constants.Urls.URL_POST_CABINET_MANAGE, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    // 查询消息
    private void queryMessage() {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("worker_id", UserCenter.getUserId(this));
        requestHttpData(Constants.Urls.URL_POST_QUERY_MESSAGE_LIST, REQUEST_NET_FOUR, FProtocol.HttpMethod.POST, params);
    }

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
                ToastUtil.shortShow(this, "检查更新失败");
            }
        } else {
            CounterEntity pageEntity = Parsers.getCounter(data);
            switch (requestCode) {
                case REQUEST_NET_ONE:
                    if (pageEntity != null) {
                        entities = (ArrayList<CounterListEntity>) pageEntity.getCounterListEntities();
                        if (entities != null && entities.size() > 0) {
                            if (entities != null && entities.size() > 0) {
                                CounterAdapter adapter = new CounterAdapter(this, entities, this);
                                lv.setAdapter(adapter);
                            }
                        } else {
                            lv.setVisibility(View.GONE);
                            imgRl.setVisibility(View.VISIBLE);
                        }
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
//            case R.id.rigth_text:
//                LanguageAndAreaActivity.startLanguageActivity(this, this,CommonConstant.REQUEST_ACT_ONE);
//                break;
            case R.id.left_text: // 退出
                DialogUtils.showTwoBtnExDialog(this,
                        getString(R.string.login_logout_title),
                        getString(R.string.login_logout_content),
                        null,
                        v1 -> {
                            UserCenter.cleanLoginInfo(this);
                            DialogUtils.closeDialog();
                            startActivity(new Intent(this, LoginActivity.class));
                            ExitManager.instance.exit();
                        },
                        v12 -> DialogUtils.closeDialog());
                break;
            case R.id.inc_btn: // 扫码
                getCream();
                //调用相机权限判定
                if (CameraCanUseUtils.isCameraCanUse()) {
                    startActivity(new Intent(this, ZxingActivity.class).putExtra("list", entities));
                } else {
                    ToastUtil.showToast(this, "没相机权限，请到应用程序权限管理开启权限");
                    //跳转至app设置
                    getAppDetailSettingIntent();
                    return;
                }
                break;
            case R.id.counter_img_rl: // 重新加载
                loadData();
                break;
            case R.id.loading_ll:
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                break;
            case R.id.right_button_bell: // 通知
                startActivity(new Intent(CounterListActivity.this, MessageActivity.class));
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (!rank.equals(STATUS_FOUR) && !rank.equals(STATUS_ONE)) {
            long nowTime = System.currentTimeMillis();
            long diff = nowTime - mBackTime;
            if (diff >= DIFF_DEFAULT_BACK_TIME) {
                mBackTime = nowTime;
                Toast.makeText(getApplicationContext(), R.string.toast_back_again_exit, Toast.LENGTH_SHORT).show();
            } else {
                ExitManager.instance.exit();
                SafeSharePreferenceUtil.clearDataByKey(this, "status");
//                SafeSharePreferenceUtil.clearDataByKey(this, "latitude");
//                SafeSharePreferenceUtil.clearDataByKey(this, "longitude");
            }
        } else {
            finish();
        }
    }


    private void getCream() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //权限发生了改变 true  //  false 小米
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this).setTitle("title")
                        .setPositiveButton("ok", (dialog, which) -> {
                            // 请求授权
                            ActivityCompat.requestPermissions(CounterListActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                        }).setNegativeButton("cancel", (dialog, which) -> {

                }).create().show();

            } else {
                ActivityCompat.requestPermissions(CounterListActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
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
        lv.setVisibility(View.GONE);
        noWifi.setVisibility(View.VISIBLE);
        super.mistake(requestCode, status, errorMessage);
    }
}


