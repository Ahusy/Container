package code.ytn.cn.home.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.network.entity.CounterListEntity;
import code.ytn.cn.network.entity.OpenCounterEntity;
import code.ytn.cn.utils.DialogUtils;

import static code.ytn.cn.common.CommonConstant.ALLOT;
import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.EXTRA_FROM;
import static code.ytn.cn.common.CommonConstant.LIST;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_SUCCESS;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;

/**
 * 扫一扫
 */
public class ZxingActivity extends ToolBarActivity {

    private TextView tv;
    private String openCode;
    private OpenCounterEntity entity;
    private List<CounterListEntity> list = new ArrayList<>();
    private CounterListEntity listEntity;
    private int from;
    private String cabinetId;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing);
        tv = (TextView) findViewById(R.id.zx_tv);
        setCenterTitle(getString(R.string.title_open_the_door));
        mBtnTitleLeft.setVisibility(View.GONE);
        initData();
        startActivityForResult(new Intent(ZxingActivity.this, CaptureActivity.class), 0);

    }

    private void initData() {
        list = (List<CounterListEntity>) getIntent().getSerializableExtra(LIST);
        from = getIntent().getIntExtra(EXTRA_FROM, 0);
        cabinetId = getIntent().getStringExtra(CABINET_ID);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
//            https://wxapp.antke.cn/test/abc?id=yitunnel&deviceId=00000048
//            https://wxapp.antke.cn/test/abc?id=yitunnel&deviceId=SC20180013
//            https://api.yitunnel.com?id=yitunnel&deviceId=SC20180013
//            https://wxapp.antke.cn/test/abc?id=yitunnel&deviceId=SC20180013
            result = bundle.getString("result");
            Log.i("============= ", "onActivityResult: " + result);
            if (ALLOT == from) {  // 商品调价
                orderGoods();
            } else {
                if (result.contains("id=yitunnel")) {
                    String[] split = result.split("&deviceId=");
                    openCode = split[1];
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            listEntity = list.get(i);
                            Log.i("=======", "onActivityResult: " + list.get(i).getCabinetCode());
                            boolean contains = listEntity.getCabinetCode().contains(openCode);
                            if (contains) {
                                open();
                                return;
                            }
                        }
                    }
                    ToastUtil.shortShow(this, "非货柜管理员");
                    finish();
                } else {
                    ToastUtil.shortShow(this, getString(R.string.toast_counter_qrcode));
                    finish();
                }
            }

        } else if (resultCode == RESULT_CANCELED) {
            tv.setText(R.string.text_can_error);
            ToastUtil.showToast(this, "扫描出错");
            finish();
        }

    }


    @Override
    public void success(int requestCode, String data) {
        super.success(requestCode, data);
        closeProgressDialog();
        entity = Parsers.getOpenCounterEntity(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
                    DialogUtils.showOneBtnDialog(this, entity.getResultMsg(), v -> {
                        CounterActivity.startCounterActivity(this, listEntity.getCabinetCode(),
                                listEntity.getCabinetName(),
                                listEntity.getAddress(),
                                listEntity.getCabinetId(),
                                listEntity.getStatus(),
                                listEntity.getDistance(),
                                listEntity.getStoreId(),
                                listEntity.getGoodsNum(),
                                listEntity.getOpenDepot(),
                                listEntity.getDepotName());
                        finish();
                        DialogUtils.closeDialog();
                    });
                } else {
                    finish();
                }
                break;
            case REQUEST_NET_TWO: // 调价
                ChooseGoodsListEntity chooseGoodsListEntity = Parsers.getChooseGoodsListEntity(data);
                if (REQUEST_NET_SUCCESS.equals(chooseGoodsListEntity.getResultCode())) {
                    Intent intent = new Intent();
                    intent.putExtra(LIST, chooseGoodsListEntity);
                    setResult(2, intent);
                    finish();
                } else {
                    ToastUtil.showToast(this, chooseGoodsListEntity.getResultMsg());
                    finish();
                }
                break;
        }

    }

    // 开门
    private void open() {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("worker_id", UserCenter.getUserId(this));
        params.put("device_id", openCode);
        requestHttpData(Constants.Urls.URL_POST_OPEN, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    // 商品条码
    private void orderGoods() {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("barcode", result);
        params.put("cabinet_id", cabinetId);
        requestHttpData(Constants.Urls.URL_POST_GET_GOODS_DETAIL, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
