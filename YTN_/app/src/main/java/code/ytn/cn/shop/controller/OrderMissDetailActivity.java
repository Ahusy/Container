package code.ytn.cn.shop.controller;

import android.os.Bundle;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.view.ListViewForInner;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.OrderMissDetailEntity;
import code.ytn.cn.shop.adapter.OrderMissDetailAdapter;
import code.ytn.cn.utils.VerifyUtils;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.ORDER_EIGHT;
import static code.ytn.cn.common.CommonConstant.ORDER_FIVE;
import static code.ytn.cn.common.CommonConstant.ORDER_FOUR;
import static code.ytn.cn.common.CommonConstant.ORDER_NINE;
import static code.ytn.cn.common.CommonConstant.ORDER_ONE;
import static code.ytn.cn.common.CommonConstant.ORDER_SEVEN;
import static code.ytn.cn.common.CommonConstant.ORDER_SIX;
import static code.ytn.cn.common.CommonConstant.ORDER_THREE;
import static code.ytn.cn.common.CommonConstant.ORDER_TWO;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;

/**
 * 消单详情
 */
public class OrderMissDetailActivity extends ToolBarActivity {

    @ViewInject(R.id.miss_order_detail_num)
    private TextView missDetailNum;
    @ViewInject(R.id.miss_order_detail_time)
    private TextView missDetailTime;
    @ViewInject(R.id.miss_order_detail_user)
    private TextView missDetailUser;
    @ViewInject(R.id.miss_order_detail_pay_type)
    private TextView missDetailPayType;
    @ViewInject(R.id.miss_order_detail_status)
    private TextView missDetailStatus;
    @ViewInject(R.id.miss_order_detail_return_money_num)
    private TextView missDetailRetNum;
    @ViewInject(R.id.miss_order_detail_cz_person)
    private TextView missDetailPerson;
    @ViewInject(R.id.miss_order_detail_cz_time)
    private TextView missDetailCzTime;
    @ViewInject(R.id.miss_order_detail_goods_num)
    private TextView missDetailGoodsNum;
    @ViewInject(R.id.miss_order_detail_goods_pay_money)
    private TextView missDetailPayMoney;
    @ViewInject(R.id.miss_order_detail_return_money)
    private TextView missDetailRetMoney;
    @ViewInject(R.id.miss_order_detail_lv)
    private ListViewForInner lv;
    private String flowNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_miss_detail);
        ViewInjectUtils.inject(this);
        setCenterTitle("消单明细");
        initData();
        getCancelOrderDetails();
    }


    private void initData() {
        flowNo = getIntent().getStringExtra("flow_no");
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        switch (requestCode) {
            case REQUEST_NET_ONE:
                OrderMissDetailEntity detailEntity = Parsers.getOrderMissDetailEntity(data);
                if (detailEntity != null) {
                    String payType = detailEntity.getPayType();
                    String status = detailEntity.getStatus();
                    String payTypeName = "";
                    String statusName = "";
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
                    // 订单状态：1，待支付；2，已支付；3，已发货； 4.已完成；5、取消；6：退款；7：未购物；8：支付失败；9：已销单
                    switch (status) {
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
                    missDetailNum.setText(detailEntity.getOrderFlowNo());
                    missDetailTime.setText(detailEntity.getPayTime());
                    boolean phone = VerifyUtils.checkPhoneNumber(detailEntity.getUserPhone());
                    if (phone) {
                        String userPhone = detailEntity.getUserPhone();
                        String mobile = userPhone.substring(0, 3) + "****" + userPhone.substring(7, userPhone.length());
                        missDetailUser.setText(mobile);
                    } else {
                        missDetailUser.setText(detailEntity.getUserPhone());
                    }
                    missDetailPayType.setText(payTypeName);
                    missDetailStatus.setText(statusName);
                    missDetailRetNum.setText(detailEntity.getFlowNo());
                    missDetailPerson.setText(UserCenter.getName(this));
                    missDetailCzTime.setText(detailEntity.getCreateDate());
                    missDetailGoodsNum.setText(detailEntity.getGoodsNum());
                    missDetailPayMoney.setText(detailEntity.getOrderTotal());
                    missDetailRetMoney.setText(detailEntity.getRefundAmt());
                    List<OrderMissDetailEntity.GoodsListEntity> listEntities = detailEntity.getListEntities();
                    if (listEntities != null && listEntities.size() > 0) {
                        OrderMissDetailAdapter adapter = new OrderMissDetailAdapter(this, listEntities);
                        lv.setAdapter(adapter);
                    }
                }
                break;
        }

    }

    // 消单详情
    private void getCancelOrderDetails() {
        showProgressDialog();
        IdentityHashMap<String,String> params = new IdentityHashMap<>();
        params.put("flow_no", flowNo);
        requestHttpData(Constants.Urls.URL_POST_GET_CANCEL_ORDER_DETAIL, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }
}
