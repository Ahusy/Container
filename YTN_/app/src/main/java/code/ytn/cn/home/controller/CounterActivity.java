package code.ytn.cn.home.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;

import code.ytn.cn.BaiMapActivity;
import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.BlockEntity;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.network.entity.StatusEntity;
import code.ytn.cn.shop.controller.GoodsAdjustPriceActivity;
import code.ytn.cn.shop.controller.ValetOrderActivity;
import code.ytn.cn.shop.view_.BadgeView;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.DisplayUtils;
import code.ytn.cn.utils.SafeSharePreferenceUtil;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.ADDRESS;
import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.DEPOT_NAME;
import static code.ytn.cn.common.CommonConstant.DISTANCE;
import static code.ytn.cn.common.CommonConstant.FROM_ACT_ONE;
import static code.ytn.cn.common.CommonConstant.FROM_ACT_TWO;
import static code.ytn.cn.common.CommonConstant.LACK_NUM;
import static code.ytn.cn.common.CommonConstant.NUM;
import static code.ytn.cn.common.CommonConstant.OPEN_DEPOT;
import static code.ytn.cn.common.CommonConstant.REQUEST_ACT_FIVE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_FIVE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_SUCCESS;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_THREE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;
import static code.ytn.cn.common.CommonConstant.STATUS;
import static code.ytn.cn.common.CommonConstant.STATUS_FOUR;
import static code.ytn.cn.common.CommonConstant.STATUS_ONE;
import static code.ytn.cn.common.CommonConstant.STATUS_THREE;
import static code.ytn.cn.common.CommonConstant.STATUS_TWO;
import static code.ytn.cn.common.CommonConstant.STORE_ID;

/**
 * 货柜管理2-4
 */
public class CounterActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.counter_num)
    private TextView counNum;
    @ViewInject(R.id.counter_name)
    private TextView counName;
    @ViewInject(R.id.counter_address)
    private TextView counAddress;
    @ViewInject(R.id.counter_distance)
    private TextView counDistance;
    @ViewInject(R.id.counter_select)
    private TextView counSelect;
    @ViewInject(R.id.counter_input)
    private ImageView counInput;
    @ViewInject(R.id.counter_goout)
    private ImageView counGoout;
    @ViewInject(R.id.counter_allot)
    private ImageView counAllot;
    @ViewInject(R.id.counter_check)
    private ImageView counCheck;
    @ViewInject(R.id.counter_replenishment)
    private ImageView counReplement;
    @ViewInject(R.id.counter_query)
    private ImageView counQuery;
    @ViewInject(R.id.counter_facility)
    private ImageView counFacility;
    @ViewInject(R.id.left_button)
    private ImageView left;
    @ViewInject(R.id.counter_place_order)
    private ImageView counPlaceOrder;
    @ViewInject(R.id.counter_goods_price)
    private ImageView counGoodsPrice;
    @ViewInject(R.id.counter_query_repertory)
    private ImageView counterQueryReper;
    @ViewInject(R.id.counter_ll)
    private RelativeLayout ll;
    @ViewInject(R.id.ll_map)
    private RelativeLayout llMap;
    @ViewInject(R.id.left_text)
    private TextView leftText;
    @ViewInject(R.id.counter_early_warning)
    private ImageView counWarning;
    @ViewInject(R.id.counter_look_stockout)
    private ImageView counStockout;
    private String cabinetId;
    private String cabinetName;
    public static final long DIFF_DEFAULT_BACK_TIME = 2000;

    private long mBackTime = -1;
    private String address;
    private String status;
    private String storeId;
    private String openDepot;
    private String depotName;

    public static void startCounterActivity(Context context,String num,String cabinetName,String address,String cabinetId,String status,String distance,String storeId,String lackNum,String openDepot,String depotName){
        Intent intent = new Intent(context,CounterActivity.class);
        intent.putExtra(NUM,num);
        intent.putExtra(CABINET_NAME,cabinetName);
        intent.putExtra(ADDRESS,address);
        intent.putExtra(CABINET_ID,cabinetId);
        intent.putExtra(STATUS,status);
        intent.putExtra(DISTANCE,distance);
        intent.putExtra(STORE_ID,storeId);
        intent.putExtra(LACK_NUM,lackNum);
        intent.putExtra(OPEN_DEPOT,openDepot);
        intent.putExtra(DEPOT_NAME,depotName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_activity);
        ViewInjectUtils.inject(this);
        String rank = UserCenter.getRank(this);
        if (!rank.equals("4") && !rank.equals("1")) {
            ll.setVisibility(View.GONE);
        }
        setCenterTitle(getString(R.string.title_counter));
        initClick();
        getStatus();

    }

    private void initClick() {
        counInput.setOnClickListener(this);
        counGoout.setOnClickListener(this);
        counAllot.setOnClickListener(this);
        counCheck.setOnClickListener(this);
        counReplement.setOnClickListener(this);
        counQuery.setOnClickListener(this);
        counFacility.setOnClickListener(this);
        rightText.setOnClickListener(this);
        llMap.setOnClickListener(this);
        counSelect.setOnClickListener(this);
        leftText.setOnClickListener(this);
        counGoodsPrice.setOnClickListener(this);
        counPlaceOrder.setOnClickListener(this);
        counterQueryReper.setOnClickListener(this);
        counWarning.setOnClickListener(this);
        counStockout.setOnClickListener(this);

        String num = getIntent().getStringExtra(NUM);
        cabinetName = getIntent().getStringExtra(CABINET_NAME);
        address = getIntent().getStringExtra(ADDRESS);
        cabinetId = getIntent().getStringExtra(CABINET_ID);
        String distance = getIntent().getStringExtra(DISTANCE);
        storeId = getIntent().getStringExtra(STORE_ID);
        openDepot = getIntent().getStringExtra(OPEN_DEPOT);
        depotName = getIntent().getStringExtra(DEPOT_NAME);
        counNum.setText(num);
        counName.setText(cabinetName);
        counAddress.setText(address);
        counDistance.setText(distance + getString(R.string.text_rice));
        String lackNum = getIntent().getStringExtra(LACK_NUM);
        // 初始化badgeView
        BadgeView badgeView = new BadgeView(this, counStockout);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 设置显示位置
        badgeView.setShadowLayer(2, -1, -1, Color.WHITE);
        badgeView.setBadgeMargin(DisplayUtils.dip2px(this,20));

        if (lackNum != null && !lackNum.equals("")) {
            if (Integer.parseInt(lackNum) > 99) {
                badgeView.setText("99+"+ "");
            } else {
                badgeView.setText(lackNum);
            }
            badgeView.show();
        } else {
            badgeView.hide();
        }
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        Entity entity = Parsers.getResult(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                BlockEntity blockEntity = Parsers.getBlockEntity(data);
                if (blockEntity.getResultCode().equals(REQUEST_NET_SUCCESS)) {
                    String status = blockEntity.getStatus();
                    this.status = status;
                    switch (status) {
                        case STATUS_ONE:
                            counSelect.setBackground(getResources().getDrawable(R.drawable.inuse));
                            break;
                        case STATUS_TWO:
                            counSelect.setBackground(getResources().getDrawable(R.drawable.blockup));
                            break;
                        case STATUS_THREE:
                            counSelect.setBackground(getResources().getDrawable(R.drawable.icon_block));
                            break;
                        case STATUS_FOUR:
                            counSelect.setBackground(getResources().getDrawable(R.drawable.bushu));
                    }
                    SafeSharePreferenceUtil.saveString(STATUS, status);

                    ToastUtil.shortShow(this, blockEntity.getResultMsg());
                } else {
                    ToastUtil.shortShow(this, blockEntity.getResultMsg());
                }
                break;
            case REQUEST_NET_TWO:
                if (REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
                    DialogUtils.showOneBtnDialog(this, entity.getResultMsg(), v -> {
                        finish();
                        DialogUtils.closeDialog();
                    });
                } else {
                    ToastUtil.shortShow(this, entity.getResultMsg());
                }
                break;
            case REQUEST_NET_THREE:
                Entity result = Parsers.getResult(data);
                if (result.getResultCode().equals(REQUEST_NET_SUCCESS)) {
                    ToastUtil.showToast(this, result.getResultMsg());
                } else {
                    ToastUtil.showToast(this, result.getResultMsg());
                }
                break;
            case REQUEST_ACT_FIVE:
                StatusEntity statusEntity = Parsers.getStatusEntity(data);
                status = statusEntity.getStatus();
                switch (status) {
                    case STATUS_ONE:
                        counSelect.setBackground(getResources().getDrawable(R.drawable.inuse));
                        break;
                    case STATUS_TWO:
                        counSelect.setBackground(getResources().getDrawable(R.drawable.blockup));
                        break;
                    case STATUS_THREE:
                        counSelect.setBackground(getResources().getDrawable(R.drawable.icon_block));
                        break;
                    case STATUS_FOUR:
                        counSelect.setBackground(getResources().getDrawable(R.drawable.bushu));
                }

                SafeSharePreferenceUtil.saveString(STATUS, status);
                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        getStatus();
    }

    // 关门
    private void close() {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("worker_id", UserCenter.getUserId(this));
        params.put("device_id", "closeCode");
        requestHttpData(Constants.Urls.URL_POST_CLOSE, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    // 锁定
    private void block() {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("status", status);
        params.put("cabinet_id", cabinetId);
        requestHttpData(Constants.Urls.URL_POST_GET_LOCK_CABINET, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    // 清除
    private void clear() {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("cabinet_id", cabinetId);
        requestHttpData(Constants.Urls.URL_POST_CLEAR_USER_INFO, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
    }

    // 状态
    private void getStatus() {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("cabinet_id", cabinetId);
        requestHttpData(Constants.Urls.URL_POST_GET_CABINET_STATUS, REQUEST_NET_FIVE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.counter_input: // 入库管理
                InputGoodsActivity.startInPutGoodsActivity(this,cabinetId,openDepot,cabinetName,depotName);
                break;
            case R.id.counter_goout: // 出库管理
                OutGoodsActivity.startGooutGoodsActivity(this,cabinetId,openDepot,cabinetName,depotName);
                break;
            case R.id.counter_allot: // 货品调拨
                GoodsAllotActivity.startGoodsAllotActivity(this,storeId,cabinetId,cabinetName);
                break;
            case R.id.counter_replenishment: // 申请补货
                ReplenishmentListActivity.startReplenishmentListActivity(this,cabinetId,cabinetName);
                break;
            case R.id.counter_check: // 库存盘点
                RepertoryCheckActivity.startRepertoryCheckActivity(this,cabinetId,cabinetName,storeId);
                break;
            case R.id.counter_query: // 订单查询
                OrderQueryActivity.startOrderQueryActivity(this,cabinetId,cabinetName,FROM_ACT_ONE);
                break;
            case R.id.counter_facility: // 设备管理
                FacilityActivity.startFacilityActivity(this,cabinetId,cabinetName,status);
                break;
            case R.id.ll_map: // 跳转地图页面
                BaiMapActivity.startBaiMapActivity(this,address);
                break;
            case R.id.counter_select:// 货柜状态
                if (status.equals(STATUS_ONE)) {
                    DialogUtils.showTwoBtnDialog(this, getString(R.string.hint), getString(R.string.dialog_block_counter), v1 -> {
                        block();
                        DialogUtils.closeDialog();
                    }, v2 -> DialogUtils.closeDialog());
                }
                if (status.equals(STATUS_THREE)) {
                    DialogUtils.showTwoBtnDialog(this, getString(R.string.hint), getString(R.string.dialog_deblocking), v1 -> {
                        block();
                        DialogUtils.closeDialog();
                    }, v2 -> DialogUtils.closeDialog());
                }
                break;
            case R.id.counter_place_order: // 代客下单
                ValetOrderActivity.startValetOrderActivity(this,cabinetId);
                break;
            case R.id.counter_goods_price: // 商品调价
                GoodsAdjustPriceActivity.startGoodsAdjustPriceActivity(this,cabinetId);
                break;
            case R.id.counter_query_repertory: // 查看库存
                QueryRepetoryActivity.startQueryRepetoryActivity(this,cabinetId,FROM_ACT_ONE);
                break;
            case R.id.counter_early_warning: // 库存预警设置
                WarningManagerActivity.startWarningManagerActivity(this,cabinetId,cabinetName);
                break;
            case R.id.counter_look_stockout: // 缺货商品查看
                QueryRepetoryActivity.startQueryRepetoryActivity(this,cabinetId,FROM_ACT_TWO);
                break;
        }
    }

}
