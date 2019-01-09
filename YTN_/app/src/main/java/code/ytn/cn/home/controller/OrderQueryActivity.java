package code.ytn.cn.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;

import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.home.adapter.OrderQueryAdapter;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.OrderListEntity;
import code.ytn.cn.network.entity.OrderQueryEntity;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.VerifyUtils;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.EXTRA_TYPE;
import static code.ytn.cn.common.CommonConstant.FROM_ACT_ONE;
import static code.ytn.cn.common.CommonConstant.PAGENUM;
import static code.ytn.cn.common.CommonConstant.PAGESIZE;
import static code.ytn.cn.common.CommonConstant.PAGE_SIZE_10;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;
import static code.ytn.cn.common.CommonConstant.STATUS_FOUR;
import static code.ytn.cn.common.CommonConstant.STATUS_ONE;
import static code.ytn.cn.common.CommonConstant.STORE_ID;

/**
 * 订单查询
 */
public class OrderQueryActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.order_query_name)
    private TextView orderName;
    @ViewInject(R.id.order_query_lv)
    private FootLoadingListView orderLv;
    @ViewInject(R.id.ll_cabinet_name)
    private RelativeLayout ll;
    @ViewInject(R.id.order_img_rl)
    private RelativeLayout imgRl;
    @ViewInject(R.id.no_wifi)
    private FrameLayout noWifi;
    private String id;
    private List<OrderListEntity> entities;
    private OrderQueryAdapter adapter;
    private int exeType;
    private String orderNum;
    private String orderUser;
    private String storeId;

    public static void startOrderQueryActivity(Context context,String id,String name,int exeType){
        Intent intent = new Intent(context,OrderQueryActivity.class);
        intent.putExtra(CABINET_ID,id);
        intent.putExtra(CABINET_NAME,name);
        intent.putExtra(EXTRA_TYPE,exeType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_query);
        ViewInjectUtils.inject(this);
        // 初始化数据
        initData();
        // 订单列表
        loadData(false);
        orderLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(true);
            }
        });

        orderLv.setOnItemClickListener((parent, view, position, id1) ->
                OrderQueryDetailActivity.startOrderQueryDetailActivity(this,
                entities.get(position).getOrderCode(),
                entities.get(position).getNum(),
                entities.get(position).getPayTime(),
                entities.get(position).getStatus(),
                entities.get(position).getUserId(),
                entities.get(position).getPayMoney(),
                entities.get(position).getPayType(),
                entities.get(position).getErrorStatus()));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData(false);
    }

    // 订单列表
    private void loadData(boolean isMore) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        int page = 1;
        int request = REQUEST_NET_ONE;
        String url;
        if (exeType == FROM_ACT_ONE) {
            url = Constants.Urls.URL_POST_GET_GET_ORDERLIST;
            if (isMore) {
                request = REQUEST_NET_TWO;
                page = adapter.getPage() + 1;
            }
            params.put("cabinet_id", id);

        } else {
            url = Constants.Urls.URL_POST_STORE_ORDER_LIST;
            if (isMore) {
                request = REQUEST_NET_TWO;
                page = adapter.getPage() + 1;
            }
            params.put("store_id", storeId);
        }
        params.put("flow_no", orderNum);
        params.put("user_phone", orderUser);
        params.put(PAGESIZE, PAGE_SIZE_10);
        params.put(PAGENUM, String.valueOf(page));
        requestHttpData(url, request, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        orderLv.setOnRefreshComplete();
        OrderQueryEntity pageEntity = Parsers.getOrderQueryEntity(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (pageEntity != null) {
                    entities = pageEntity.getListEntities();
                    if (entities != null && entities.size() > 0) {
                        if (entities != null && entities.size() > 0) {
                            adapter = new OrderQueryAdapter(this, entities, this);
                            orderLv.setAdapter(adapter);
                            if (pageEntity.getTotalPage() > adapter.getPage()) {
                                orderLv.setCanAddMore(true);
                            } else {
                                orderLv.setCanAddMore(false);
                            }
                        } else {
                            if (adapter != null) {
                                adapter.clear();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        orderLv.setVisibility(View.GONE);
                        ll.setVisibility(View.GONE);
                        imgRl.setVisibility(View.VISIBLE);
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            case REQUEST_NET_TWO://加载更多
                if (pageEntity != null) {
                    List<OrderListEntity> listEntities = pageEntity.getListEntities();
                    if (listEntities != null && listEntities.size() > 0) {
                        adapter.addDatas(listEntities);
                        adapter.notifyDataSetChanged();
                        if (pageEntity.getTotalPage() > adapter.getPage()) {
                            orderLv.setCanAddMore(true);
                        } else {
                            orderLv.setCanAddMore(false);
                        }
                    }
                }
                break;
        }
    }

    // 初始化数据
    private void initData() {
        String rank = UserCenter.getRank(this);
        if (rank.equals(STATUS_FOUR) || rank.equals(STATUS_ONE)) {
            ll.setVisibility(View.GONE);
        }
        exeType = getIntent().getIntExtra(EXTRA_TYPE, FROM_ACT_ONE);
        setCenterTitle(getString(R.string.title_order));
        mBtnTitleRightSerach.setVisibility(View.VISIBLE);
        mBtnTitleRightSerach.setOnClickListener(this);
        imgRl.setOnClickListener(this);
        LinearLayout noWifiLayout = (LinearLayout) noWifi.findViewById(R.id.loading_ll);
        noWifiLayout.setOnClickListener(this);
        id = getIntent().getStringExtra(CABINET_ID);
        storeId = getIntent().getStringExtra(STORE_ID);
        String name = getIntent().getStringExtra(CABINET_NAME);
        orderName.setText(name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_button_serach:
                DialogUtils.showQueryOrderDialog(this, (v1, editText1, editText2) -> {
                    orderNum = editText1.getText().toString().trim();
                    orderUser = editText2.getText().toString().trim();
                    if (!orderUser.equals("") && !VerifyUtils.checkPhoneNumber(orderUser)) {
                        ToastUtil.shortShow(this, "请输入正确的手机号");
                        return;
                    }
                    loadData(false);
                    DialogUtils.closeDialog();
                }, view -> { // 取消
                    DialogUtils.closeDialog();
                });
                break;
            case R.id.order_img_rl: // 重新加载数据
                loadData(false);
                break;
            case R.id.loading_ll:
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                break;
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        if (entities != null){
            entities.clear();
        }
        closeProgressDialog();
        ll.setVisibility(View.GONE);
        orderLv.setVisibility(View.GONE);
        noWifi.setVisibility(View.VISIBLE);
        super.mistake(requestCode, status, errorMessage);
    }
}
