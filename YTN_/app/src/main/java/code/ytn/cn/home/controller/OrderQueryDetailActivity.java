package code.ytn.cn.home.controller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.home.adapter.DialogErrorAdapter;
import code.ytn.cn.home.adapter.OrderQueryDetailtAdapter;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.network.entity.OrderQueryDetailEntity;
import code.ytn.cn.network.entity.OrderQueryDetailListEntity;
import code.ytn.cn.shop.event.CountEvent;
import code.ytn.cn.utils.DataUtils;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.ViewInjectUtils;
import code.ytn.cn.widget.ClearEditText;

import static code.ytn.cn.common.CommonConstant.CODE;
import static code.ytn.cn.common.CommonConstant.ERROR_STATUS;
import static code.ytn.cn.common.CommonConstant.MONEY;
import static code.ytn.cn.common.CommonConstant.NUM;
import static code.ytn.cn.common.CommonConstant.ORDER_EIGHT;
import static code.ytn.cn.common.CommonConstant.ORDER_FIVE;
import static code.ytn.cn.common.CommonConstant.ORDER_FOUR;
import static code.ytn.cn.common.CommonConstant.ORDER_NINE;
import static code.ytn.cn.common.CommonConstant.ORDER_ONE;
import static code.ytn.cn.common.CommonConstant.ORDER_SEVEN;
import static code.ytn.cn.common.CommonConstant.ORDER_SIX;
import static code.ytn.cn.common.CommonConstant.ORDER_THREE;
import static code.ytn.cn.common.CommonConstant.ORDER_TWO;
import static code.ytn.cn.common.CommonConstant.ORDER_ZERO;
import static code.ytn.cn.common.CommonConstant.PAY_TYPE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_SUCCESS;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_THREE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;
import static code.ytn.cn.common.CommonConstant.STATUS;
import static code.ytn.cn.common.CommonConstant.STATUS_ZERO;
import static code.ytn.cn.common.CommonConstant.TIME;
import static code.ytn.cn.common.CommonConstant.USER_ID;

/**
 * 订单详情
 */
public class OrderQueryDetailActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.order_query_detail_num)
    private TextView detailNum;
    @ViewInject(R.id.order_query_detail_time)
    private TextView detailTime;
    @ViewInject(R.id.order_query_detail_name)
    private TextView detailName;
    @ViewInject(R.id.order_query_detail_count)
    private TextView detailCount;
    @ViewInject(R.id.order_query_detail_lv)
    private ListView detailLv;
    @ViewInject(R.id.order_query_detail_select)
    private TextView detailSelect;
    @ViewInject(R.id.order_query_detail_price)
    private TextView detailPrice;
    @ViewInject(R.id.order_query_detail_btm_select)
    private TextView detailBtmSelect;
    @ViewInject(R.id.order_query_detail_btm_cancel)
    private TextView detailBtmCancel;
    @ViewInject(R.id.order_query_detail_status)
    private TextView orderStatus;
    @ViewInject(R.id.order_query_detail_pay)
    private TextView orderPay;
    @ViewInject(R.id.order_query_detail_abnormal)
    private TextView orderAbnormal;
    @ViewInject(R.id.order_query_detail_yhq_name)
    private TextView orderYhqName;
    @ViewInject(R.id.order_query_detail_yhq_price)
    private TextView orderYhqPrice;
    @ViewInject(R.id.order_query_detail_price1)
    private TextView detailPrice1;
    @ViewInject(R.id.rl)
    private RelativeLayout rl;
    private String code;
    private List<OrderQueryDetailListEntity> entities;
    private int count;
    private ClearEditText dialogEt;
    private String goodsList;
    private Dialog dialog;

    public static void startOrderQueryDetailActivity(Context context,String code,String num,String time,String stauts,String userId,String money,String payType,String errorStatus){
        Intent intent = new Intent(context,OrderQueryDetailActivity.class);
        intent.putExtra(CODE,code);
        intent.putExtra(NUM,num);
        intent.putExtra(TIME,time);
        intent.putExtra(STATUS,stauts);
        intent.putExtra(USER_ID,userId);
        intent.putExtra(MONEY,money);
        intent.putExtra(PAY_TYPE,payType);
        intent.putExtra(ERROR_STATUS,errorStatus);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_query_detail);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_order_detail));
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        getOrderDetail();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        code = getIntent().getStringExtra(CODE);
        String num = getIntent().getStringExtra(NUM);
        String time = getIntent().getStringExtra(TIME);
        String userId = getIntent().getStringExtra(USER_ID);
        String status = getIntent().getStringExtra(STATUS);
        String money = getIntent().getStringExtra(MONEY);
        String payType = getIntent().getStringExtra(PAY_TYPE);
        String errorStatus = getIntent().getStringExtra(ERROR_STATUS);
        String statusName = "";
        String payTypeName = "";
        String errorStatusName = "";

        switch (status) {
            // 订单状态：1，待支付；2，已支付；3，已发货； 4.已完成；5、取消；6：退款；7：未购物；8：支付失败；9：已销单
            case ORDER_ONE:
                statusName = getString(R.string.order_unpaid);
                break;
            case ORDER_TWO:
                statusName = getString(R.string.order_have_paid);
                break;
            case ORDER_THREE:
                statusName = getString(R.string.order_shipped);
                break;
            case ORDER_FOUR:
                statusName = getString(R.string.order_off_the_stocks);
                break;
            case ORDER_FIVE:
                statusName = getString(R.string.order_cancel);
                break;
            case ORDER_SIX:
                statusName = getString(R.string.order_refund);
                break;
            case ORDER_SEVEN:
                statusName = getString(R.string.order_no_shopping);
                break;
            case ORDER_EIGHT:
                statusName = getString(R.string.order_pay_error);
                break;
            case ORDER_NINE:
                statusName = getString(R.string.order_miss);
                break;
        }
        // 支付方式：1，支付宝；2， 微信；3，积分；4，积分+微信；5，积分+支付宝；6，银行卡
        switch (payType) {
            case ORDER_ONE:
                payTypeName = getString(R.string.order_alipay);
                break;
            case ORDER_TWO:
                payTypeName = getString(R.string.order_wecat);
                break;
            case ORDER_THREE:
                payTypeName = getString(R.string.order_integral);
                break;
            case ORDER_FOUR:
                payTypeName = getString(R.string.order_wx_integral);
                break;
            case ORDER_FIVE:
                payTypeName = getString(R.string.order_alipay_integral);
                break;
            case ORDER_SIX:
                payTypeName = getString(R.string.order_bank_card);
                break;
        }

        // 异常状态：0、订单无异常 1、订单异常；2、发起补缴；3，已补缴；4，发起退款；5，已退款；6，客服下单
        switch (errorStatus) {
            case ORDER_ZERO:
                errorStatusName = getString(R.string.order_no_error);
                break;
            case ORDER_ONE:
                errorStatusName = getString(R.string.order_error);
                break;
            case ORDER_TWO:
                errorStatusName = getString(R.string.order_a_payment_of);
                break;
            case ORDER_THREE:
                errorStatusName = getString(R.string.order_have_to_pay_up);
                break;
            case ORDER_FOUR:
                errorStatusName = getString(R.string.order_Initiate_a_refund);
                break;
            case ORDER_FIVE:
                errorStatusName = getString(R.string.order_have_a_refund);
                break;
            case ORDER_SIX:
                errorStatusName = getString(R.string.order_the_order_of_the_service);
                break;
        }

        detailNum.setText(code);
        detailTime.setText(time);
        detailName.setText(userId);
        detailCount.setText(num);
        detailPrice.setText(getString(R.string.text_total)+money + getString(R.string.text_yuan));
        orderStatus.setText(statusName);
        orderPay.setText(payTypeName);
        orderAbnormal.setText(errorStatusName);
        detailBtmCancel.setOnClickListener(this);
        detailBtmSelect.setOnClickListener(this);
        if (errorStatus.equals(STATUS_ZERO) || errorStatus.equals(ORDER_SIX)) {
            detailSelect.setBackground(getResources().getDrawable(R.drawable.order_ok));
            detailBtmSelect.setEnabled(true);
        } else {
            detailSelect.setBackground(getResources().getDrawable(R.drawable.order_no));
            detailBtmSelect.setEnabled(false);
        }

        try {
            boolean isToday = DataUtils.IsToday(time);
            if (isToday && !errorStatus.equals(ORDER_FOUR) && !errorStatus.equals(ORDER_FIVE)
                    && (status.equals(ORDER_THREE) || status.equals(ORDER_TWO) || status.equals(ORDER_FOUR)) && Double.parseDouble(money) > 0) {
                detailBtmCancel.setEnabled(true);
            } else {
                detailBtmCancel.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        OrderQueryDetailEntity entity = Parsers.getOrderQueryDetailEntity(data);
        Entity entity1 = Parsers.getResult(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (entity != null) {
                    entities = entity.getListEntities();
                    if (entities != null && entities.size() > 0) {
                        if (entities != null && entities.size() > 0) {
                            OrderQueryDetailtAdapter adapter = new OrderQueryDetailtAdapter(this, entities, this);
                            detailLv.setAdapter(adapter);
                            if (!StringUtil.isEmpty(entity.getCouponCode())){
                                orderYhqName.setText(entity.getCouponName());
                                orderYhqPrice.setText("-"+entity.getDscTotal()+"元");
                                detailPrice1.setText("优惠金额:￥"+entity.getDscTotal()+"元");
                            }else{
                                rl.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
                    ToastUtil.shortShow(this, getString(R.string.toast_no_context));

                }
                break;
            case REQUEST_NET_TWO:
                if (REQUEST_NET_SUCCESS.equals(entity1.getResultCode())) {
                    ToastUtil.showToast(this, entity1.getResultMsg());
                    detailBtmCancel.setEnabled(false);
                } else {
                    detailBtmCancel.setEnabled(false);
                    ToastUtil.showToast(this, entity1.getResultMsg());
                }
                break;
            case REQUEST_NET_THREE:
                if (REQUEST_NET_SUCCESS.equals(entity1.getResultCode())) {
                    ToastUtil.showToast(this, entity1.getResultMsg());
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    detailSelect.setBackground(getResources().getDrawable(R.drawable.order_no));
                    detailBtmSelect.setEnabled(false);
                } else {
                    ToastUtil.showToast(this, entity1.getResultMsg());
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_query_detail_btm_select:
                orderErrorDialog();
                break;
            case R.id.order_query_detail_btm_cancel:
                DialogUtils.showTwoBtnDialog(this, getString(R.string.text_order_cancel), "请确认是否执行消单操作？\n\n操作成功后将针对此订单生退款单",
                        v1 -> { // 确定消单
                            cancleOrder();
                            DialogUtils.closeDialog();
                        }, v2 -> DialogUtils.closeDialog());
                break;
        }
    }

    private void orderErrorDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_order_error, null);
        dialog = new AlertDialog.Builder(this).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        ListView dialogLv = (ListView) view.findViewById(R.id.dialog_lv);
        dialogEt = (ClearEditText) view.findViewById(R.id.dialog_et);
        TextView dialogCommit = (TextView) view.findViewById(R.id.dialog_comit);
        dialogLv.setFocusable(true);

        dialog.setCancelable(true);
        dialog.show();
        DialogErrorAdapter dialogAdapter = new DialogErrorAdapter(this, entities);
        dialogLv.setAdapter(dialogAdapter);

        dialogCommit.setOnClickListener(v -> {
            if (dialogEt.getText().toString().trim().equals("")) {
                ToastUtil.showToast(this, "请输入异常描述。");
                if (count == 0) {
                    ToastUtil.showToast(this, "请选择异常商品");
                }
            } else {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < entities.size(); i++) {
                    JSONObject tmpObj = null;
                    tmpObj = new JSONObject();
                    if (entities.get(i).isCheck()) {
                        try {
                            tmpObj.put("goods_name", entities.get(i).getGoodsName());
                            jsonArray.put(tmpObj);
                            goodsList = jsonArray.toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                // 提交订单异常
                submitAbnormal();
            }
        });

    }


    @Subscribe
    public void CountEvent(CountEvent event) {
        count = event.getCount();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // 提交订单异常
    private void submitAbnormal() {
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("flow_no", code);
        params.put("user_id", UserCenter.getUserId(this));
        params.put("message", dialogEt.getText().toString().trim());
        params.put("goods_list", goodsList);
        requestHttpData(Constants.Urls.URL_POST_SUBMIT_ABNORMAL, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
    }

    // 订单消单
    private void cancleOrder() {
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("flow_no", code);
        requestHttpData(Constants.Urls.URL_POST_CANCLE_ORDER, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    // 订单详情
    private void getOrderDetail() {
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("flow_no", code);
        requestHttpData(Constants.Urls.URL_POST_ORDER_DETAILS, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

}
