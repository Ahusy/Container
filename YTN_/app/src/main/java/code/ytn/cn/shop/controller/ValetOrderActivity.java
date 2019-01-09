package code.ytn.cn.shop.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.home.controller.ZxingActivity;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.ChannelEntity;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.network.entity.ItemResultEntity;
import code.ytn.cn.shop.adapter.ValetOrderAdapter;
import code.ytn.cn.shop.adapter.ValetOrderChannelAdapter;
import code.ytn.cn.shop.event.PriceAndCountEvent;
import code.ytn.cn.utils.CameraCanUseUtils;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.DisplayUtils;
import code.ytn.cn.utils.VerifyUtils;
import code.ytn.cn.utils.ViewInjectUtils;
import code.ytn.cn.widget.ClearEditText;

import static code.ytn.cn.common.CommonConstant.ALLOT;
import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.EXTRA_FROM;
import static code.ytn.cn.common.CommonConstant.LIST;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_FOUR;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_SUCCESS;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_THREE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;

/**
 * 代客下单
 */
public class ValetOrderActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.valet_order_phone)
    private ClearEditText valetPhone;
    @ViewInject(R.id.valet_order_recy)
    private RecyclerView valetRecy;
    @ViewInject(R.id.valet_order_add_goods)
    private TextView valetAddGoods;
    @ViewInject(R.id.valet_order_comit)
    private TextView valetCommit;
    @ViewInject(R.id.valet_price)
    private TextView valetPrice;
    @ViewInject(R.id.valet_qdNum)
    private TextView valetQdNum;
    @ViewInject(R.id.valet_ll)
    private LinearLayout ll;
    private List<ChooseGoodsListEntity> list = new ArrayList();
    private List<ChooseGoodsListEntity> showList = new ArrayList<>();
    private ValetOrderAdapter adapter;
    private String cabinetId;
    private String goodsList;
    private ChooseGoodsListEntity codeEntity;
    private List<ChannelEntity.ChannelListEntity> channelListEntities;
    private String channel;
    private ItemResultEntity itemsResult;
    private String itemResultList;

    public static void startValetOrderActivity(Context context,String cabinetId){
        Intent intent = new Intent(context,ValetOrderActivity.class);
        intent.putExtra(CABINET_ID,cabinetId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valet_order);
        ViewInjectUtils.inject(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setCenterTitle("代客下单");
        mBtnTitleRight.setVisibility(View.VISIBLE);
        initClick();
        // 获取渠道
        getCinibitChannel();
    }

    private void initClick() {
        mBtnTitleRight.setOnClickListener(this);
        valetAddGoods.setOnClickListener(this);
        valetCommit.setOnClickListener(this);
        ll.setOnClickListener(this);
        cabinetId = getIntent().getStringExtra(CABINET_ID);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        valetRecy.setLayoutManager(manager);
        // 设置动画
        valetRecy.setItemAnimator(new DefaultItemAnimator());
        // 系统隔线
        valetRecy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_button: // 扫一扫
                getCream();
                //调用相机权限判定
                if (CameraCanUseUtils.isCameraCanUse()) {
                    startActivityForResult(new Intent(this, ZxingActivity.class).putExtra(CABINET_ID, cabinetId).putExtra(EXTRA_FROM, ALLOT), 1);
                } else {
                    ToastUtil.showToast(this, "没相机权限，请到应用程序权限管理开启权限");
                    //跳转至app设置
                    getAppDetailSettingIntent();
                    return;
                }
                break;
            case R.id.valet_order_add_goods: // 添加商品
                startActivityForResult(new Intent(this, AddGoodsActivity.class).putExtra(CABINET_ID, cabinetId), 1);
                break;
            case R.id.valet_order_comit: // 确定
                toJson();
                if (valetRecy.getChildCount() == 0) {
                    ToastUtil.showToast(this, "请选择要下单的商品");
                } else if (!VerifyUtils.checkPhoneNumber(valetPhone.getText().toString().trim())) {
                    ToastUtil.showToast(this, "请输入正确的手机号码");
                } else if(valetQdNum.getText().toString().equals("")){
                    ToastUtil.showToast(this,"请选择渠道");
                } else{
                    // 查询优惠
                    getItemsResult();
//                    DialogUtils.showCommit(this, showList, valetPhone.getText().toString(), valetPrice.getText().toString(),"","", v1 -> {
//                        commit();
//                    }, v1 -> DialogUtils.closeDialog());
                }
                break;
            case R.id.valet_ll:
                showPopupWindow();
                break;
        }
    }

    // 选择渠道
    private void showPopupWindow() {
        View view = View.inflate(ValetOrderActivity.this, R.layout.popup_window, null);
        ListView mPopListView = (ListView) view.findViewById(R.id.pop_list_view);

        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.update();
        popupWindow.showAsDropDown(valetQdNum, DisplayUtils.dip2px(this, 50), 15);

        ValetOrderChannelAdapter mListViewAdapter = new ValetOrderChannelAdapter(this, channelListEntities, this, popupWindow);
        mPopListView.setAdapter(mListViewAdapter);
        mPopListView.setOnItemClickListener((parent, view1, position, id) -> {
            valetQdNum.setText(channelListEntities.get(position).getPayName());
            channel = channelListEntities.get(position).getChannel();
            popupWindow.dismiss();
        });

    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        Entity result = Parsers.getResult(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
                    ToastUtil.showToast(this, result.getResultMsg());
                    DialogUtils.closeDialog();
                    finish();
                } else {
                    ToastUtil.showToast(this, result.getResultMsg());
                }
                break;
            case REQUEST_NET_TWO:
                ChannelEntity channelEntity = Parsers.getChannelEntity(data);
                if (channelEntity != null){
                    channelListEntities = channelEntity.getListEntities();
                }
                break;
            case REQUEST_NET_FOUR:
                itemsResult = Parsers.getItemsResult(data);
                if (itemsResult != null){
                    toItemResultJson();
                    List<ItemResultEntity.ItemResultListEntity> listEntities = itemsResult.getListEntities();
                    DialogUtils.showCommit(this, listEntities, valetPhone.getText().toString(), itemsResult.getActTotal(), itemsResult.getDscTotal(), itemsResult.getCouponName(), v1 -> {
                        commit();
                    }, v1 -> DialogUtils.closeDialog());
                }
                break;
        }
    }

    @Subscribe
    public void PriceAndCountEvent(PriceAndCountEvent event) {
        BigDecimal bd = new BigDecimal(event.getPrice());
        double price = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        valetPrice.setText(price + "元");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) { // 选择商品
            list = (List<ChooseGoodsListEntity>) data.getSerializableExtra(LIST);
            if (list != null && list.size() > 0) {
                if (showList.size() == 0) {
                    showList.addAll(list);
                } else {
                    ArrayList<ChooseGoodsListEntity> tmplList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < showList.size(); j++) {
                            ChooseGoodsListEntity cgle = showList.get(j);
                            if (list.get(i).getGoodsId().equals(cgle.getGoodsId())) {
                                cgle.setNum(cgle.getNum() + 1);
                            }
                        }
                    }
                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < showList.size(); j++) {
                            ChooseGoodsListEntity cgi = list.get(i);
                            ChooseGoodsListEntity cgj = showList.get(j);
                            if (!cgi.getGoodsId().equals(cgj.getGoodsId())) {
                                boolean isHave = false;
                                boolean isShowHave = false;
                                for (int k = 0; k < tmplList.size(); k++) {
                                    ChooseGoodsListEntity cgk = tmplList.get(k);
                                    if (cgk.getGoodsId().equals(cgi.getGoodsId())) {
                                        isHave = true;
                                    }
                                }
                                if (!isHave) {
                                    for (int q = 0; q < showList.size(); q++) {
                                        ChooseGoodsListEntity cgq = showList.get(q);
                                        if (cgq.getGoodsId().equals(cgi.getGoodsId())) {
                                            isShowHave = true;
                                        }
                                    }
                                }
                                if (!isHave && !isShowHave) {
                                    tmplList.add(cgi);
                                }
                            }
                        }
                    }
                    showList.addAll(tmplList);
                    tmplList.clear();
                }
                adapter = new ValetOrderAdapter(this, showList);
                valetRecy.setAdapter(adapter);
            }
            adapter.setOnItemClickLitener(new ValetOrderAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    DialogUtils.showTwoBtnDialog(ValetOrderActivity.this, "提示", "确定删除此商品吗？", v -> {
                        showList.remove(position);
                        adapter.notifyDataSetChanged();
                        DialogUtils.closeDialog();
                        if (adapter.getItemCount() == 0) {
                            valetPrice.setText("0.0");
                        }
                    }, v -> DialogUtils.closeDialog());
                }
            });
        } else if (requestCode == 1 && resultCode == 2) {  // 扫码商品
            codeEntity = (ChooseGoodsListEntity) data.getSerializableExtra(LIST);
            boolean isAdd = false;
            for (int j = 0; j < showList.size(); j++) {
                ChooseGoodsListEntity cgle = showList.get(j);
                if (codeEntity.getGoodsId().equals(cgle.getGoodsId())) {
                    cgle.setNum(cgle.getNum() + 1);
                    isAdd = true;
                }
            }
            if (!isAdd) {
                boolean isShowHave = false;
                for (int j = 0; j < showList.size(); j++) {
                    ChooseGoodsListEntity cgj = showList.get(j);
                    if (codeEntity.getGoodsId().equals(cgj.getGoodsId())) {
                        isShowHave = true;
                    }
                }
                if (!isShowHave) {
                    showList.add(codeEntity);
                }
            }
            adapter = new ValetOrderAdapter(this, showList);
            valetRecy.setAdapter(adapter);
        }
    }

    private void toJson() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < showList.size(); i++) {
            JSONObject tmp = null;
            tmp = new JSONObject();
            try {
                tmp.put("goods_id", showList.get(i).getGoodsId());
                tmp.put("num", String.valueOf(showList.get(i).getNum()));
                jsonArray.put(tmp);
                goodsList = jsonArray.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void toItemResultJson() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < itemsResult.getListEntities().size(); i++) {
            JSONObject tmp = null;
            tmp = new JSONObject();
            try {
                tmp.put("goods_id", itemsResult.getListEntities().get(i).getGoodsId());
                tmp.put("num", String.valueOf(itemsResult.getListEntities().get(i).getNum()));
                tmp.put("price",itemsResult.getListEntities().get(i).getCabinetPrice());
                tmp.put("dsc_price",itemsResult.getListEntities().get(i).getDscPrice());
                jsonArray.put(tmp);
                itemResultList = jsonArray.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 提交
    private void commit() {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("user_phone", valetPhone.getText().toString());
        params.put("cabinet_id", cabinetId);
        params.put("channel",channel);
        params.put("total",itemsResult.getTotal());
        params.put("dsc_total",itemsResult.getDscTotal());
        params.put("coupon_code",itemsResult.getCouponCode());
        params.put("coupon_name",itemsResult.getCouponName());
        params.put("goods_list", itemResultList);
        requestHttpData(Constants.Urls.URL_POST_REPLACE_PLACE_ORDER, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    // 获取渠道
    private void getCinibitChannel() {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("cabinet_id", cabinetId);
        requestHttpData(Constants.Urls.URL_POST_GET_CABINET_CHANNEL, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    // 获取金额
    private void getTotalMoney() {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("user_phone",valetPhone.getText().toString());
        params.put("channel",channel);
        params.put("cabinet_id", cabinetId);
        params.put("goods_list", goodsList);
        requestHttpData(Constants.Urls.URL_POST_GET_TOTAL_MONEY, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
    }

    // 查询优惠
    private void getItemsResult() {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("user_phone",valetPhone.getText().toString());
        params.put("channel",channel);
        params.put("cabinet_id", cabinetId);
        params.put("goods_list", goodsList);
        requestHttpData(Constants.Urls.URL_POST_GET_ITEM_RESULT, REQUEST_NET_FOUR, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * checkSelfPermission 检测有没有 权限
     * PackageManager.PERMISSION_GRANTED 有权限
     * PackageManager.PERMISSION_DENIED  拒绝权限
     */
    private void getCream() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //权限发生了改变 true  //  false 小米
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this).setTitle("title")
                        .setPositiveButton("ok", (dialog, which) -> {
                            // 请求授权
                            ActivityCompat.requestPermissions(ValetOrderActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                        }).setNegativeButton("cancel", (dialog, which) -> {

                }).create().show();

            } else {
                ActivityCompat.requestPermissions(ValetOrderActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
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
