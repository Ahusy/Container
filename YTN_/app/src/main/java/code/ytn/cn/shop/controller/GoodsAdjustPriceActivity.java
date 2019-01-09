package code.ytn.cn.shop.controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
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
import code.ytn.cn.home.controller.ZxingActivity;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.ChooseGoodsEntity;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.shop.adapter.GoodsAdjustPriceAdapter;
import code.ytn.cn.utils.CameraCanUseUtils;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.ALLOT;
import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.EXTRA_FROM;
import static code.ytn.cn.common.CommonConstant.LIST;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_SUCCESS;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;

/**
 * 商品调价
 */
public class GoodsAdjustPriceActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.goods_adjust_price_lv)
    private ListView lv;
    @ViewInject(R.id.view)
    private View view;
    @ViewInject(R.id.adjust_price_comit)
    private TextView adjustCommit;
    private List<ChooseGoodsListEntity> entities;
    private GoodsAdjustPriceAdapter adapter;
    private ArrayList<ChooseGoodsListEntity> list = new ArrayList<>();
    private ChooseGoodsListEntity goods;
    private String cabinetId;
    private String tojsonList;

    public static void startGoodsAdjustPriceActivity(Context context,String cabinetId){
        Intent intent = new Intent(context,GoodsAdjustPriceActivity.class);
        intent.putExtra(CABINET_ID,cabinetId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_adjust_price);
        ViewInjectUtils.inject(this);
        setCenterTitle("商品调价");
        mBtnTitleRight.setVisibility(View.VISIBLE);
        initClick();
        queryGoodsList();
    }

    private void initClick() {
        mBtnTitleRight.setOnClickListener(this);
        adjustCommit.setOnClickListener(this);
        cabinetId = getIntent().getStringExtra(CABINET_ID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_button: // 扫一扫
                getCream();
                //调用相机权限判定
                if (CameraCanUseUtils.isCameraCanUse()) {
                    startActivityForResult(new Intent(this, ZxingActivity.class).putExtra("cabinet_id", cabinetId).putExtra(EXTRA_FROM, ALLOT), 2);
                } else {
                    ToastUtil.showToast(this, "没相机权限，请到应用程序权限管理开启权限");
                    //跳转至app设置
                    getAppDetailSettingIntent();
                    return;
                }

                break;
            case R.id.adjust_price_comit: // 提交
                list.clear();
                for (int i = 0; i < entities.size(); i++) {
                    if (entities.get(i).getAdjustPrice() != null && !"".equals(entities.get(i).getAdjustPrice().trim())) {
                        ChooseGoodsListEntity goodsListEntity = entities.get(i);
                        list.add(goodsListEntity);
                    }
                }
                if (list != null && list.size() > 0) {
                    toJson();
                    DialogUtils.showAdjustComit(this, list, v1 -> commit(),
                            v1 -> DialogUtils.closeDialog());
                } else {
                    ToastUtil.showToast(this, "请输入要改价商品的价格");
                    return;
                }
                break;
        }
    }

    private void toJson() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                JSONObject tmp = null;
                tmp = new JSONObject();
                tmp.put("goods_id", list.get(i).getGoodsId());
                tmp.put("modify_price", list.get(i).getAdjustPrice());
                jsonArray.put(tmp);
                tojsonList = jsonArray.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        ChooseGoodsEntity entity = Parsers.getChooseGoodsEntity(data);
        Entity result = Parsers.getResult(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (entity != null) {
                    entities = entity.getListEntities();
                    adapter = new GoodsAdjustPriceAdapter(this, (ArrayList<ChooseGoodsListEntity>) entities);
                    lv.setAdapter(adapter);
                } else {
                    ToastUtil.shortShow(this, "暂无数据");
                }

                break;
            case REQUEST_NET_TWO:
                if (result.getResultCode().equals(REQUEST_NET_SUCCESS)) {
                    ToastUtil.showToast(this, result.getResultMsg());
                    DialogUtils.closeDialog();
                    finish();
                } else {
                    ToastUtil.showToast(this, result.getResultMsg());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 2) {
            goods = (ChooseGoodsListEntity) data.getSerializableExtra(LIST);
            if (goods != null) {
                DialogUtils.showSaoAdjustPrice(this, goods.getFirstPic(),
                        goods.getGoodsName() + " " + goods.getGoodsSpec(),
                        "标准售价：￥" + goods.getStandPrice(),
                        "货柜售价：￥" + goods.getGoodsPrice(), goods.getGoodsPrice(),
                        (v, editText, textView) -> {
                            for (int i = 0; i < entities.size(); i++) {
                                if (goods.getGoodsId().equals(entities.get(i).getGoodsId())) {
                                    entities.get(i).setAdjustPrice(editText.getText().toString().trim());
                                    lv.smoothScrollToPosition(i);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            DialogUtils.closeDialog();
                        }, v -> DialogUtils.closeDialog());
            }
        }
    }

    // 查询货柜商品
    private void queryGoodsList() {
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("cabinet_id", cabinetId);
        requestHttpData(Constants.Urls.URL_POST_CABINET_GOODSLIST, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    // 提交
    private void commit() {
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("goods_list", tojsonList);
        params.put("worker_id", UserCenter.getUserId(this));
        params.put("cabinet_id", cabinetId);
        requestHttpData(Constants.Urls.URL_POST_MODIFY_PRICE, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*  checkSelfPermission 检测有没有 权限
        PackageManager.PERMISSION_GRANTED 有权限
        PackageManager.PERMISSION_DENIED  拒绝权限*/
    private void getCream() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //权限发生了改变 true  //  false 小米
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this).setTitle("title")
                        .setPositiveButton("ok", (dialog, which) -> {
                            // 请求授权
                            ActivityCompat.requestPermissions(GoodsAdjustPriceActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                        }).setNegativeButton("cancel", (dialog, which) -> {

                }).create().show();

            } else {
                ActivityCompat.requestPermissions(GoodsAdjustPriceActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            }

        }
    }

    /**
     * @param requestCode
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
