package code.ytn.cn.shop.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;

import java.text.SimpleDateFormat;
import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.common.CommonConstant;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.OrderMissEntity;
import code.ytn.cn.shop.adapter.OrderMissAdapter;
import code.ytn.cn.shop.view.PickTime.DatePickDialog;
import code.ytn.cn.shop.view.PickTime.bean.DateType;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;
import static code.ytn.cn.common.CommonConstant.STORE_ID;

/**
 * 消单管理
 */
public class OrderMissActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.order_miss_lv)
    private FootLoadingListView lv;
    @ViewInject(R.id.img_rl)
    private RelativeLayout imgRl;
    @ViewInject(R.id.no_wifi)
    private FrameLayout noWifi;
    private OrderMissAdapter adapter;
    private String storeId;
    private String timeBegin;
    private String timeOver;
    private List<OrderMissEntity.OrderMissListEntity> listEntities;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_miss);
        ViewInjectUtils.inject(this);
        setCenterTitle("消单管理");
        initData();
        // 消单列表
        getCancelOrderList(false);

        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getCancelOrderList(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getCancelOrderList(true);
            }
        });

        lv.setOnItemClickListener((parent, view, position, id) ->
                startActivity(new Intent(OrderMissActivity.this,OrderMissDetailActivity.class)
        .putExtra("flow_no",listEntities.get(position).getFlowNo())));
    }

    private void initData() {
        mBtnTitleRightSerach.setVisibility(View.VISIBLE);
        mBtnTitleRightSerach.setOnClickListener(this);
        imgRl.setOnClickListener(this);
        LinearLayout noWifiLayout = (LinearLayout) noWifi.findViewById(R.id.loading_ll);
        noWifiLayout.setOnClickListener(this);
        storeId = getIntent().getStringExtra(STORE_ID);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_button_serach: // 查询
                DialogUtils.closeDialog();
                DialogUtils.showOrderMissDialog(this, "消单查询", (v1, begin) -> {
                    DatePickDialog dialog = new DatePickDialog(this);
                    //设置上下年分限制
                    dialog.setYearLimt(0);
                    //设置标题
                    dialog.setTitle("选择时间");
                    //设置类型
                    dialog.setType(DateType.TYPE_YMD);
                    //设置消息体的显示格式，日期格式
                    dialog.setMessageFormat("yyyy-MM-dd");
                    //设置选择回调
                    dialog.setOnChangeLisener(null);
                    //设置点击确定按钮回调
                    dialog.setOnSureLisener(date -> {
                        startTime = date.getTime();
                        long millis = System.currentTimeMillis();
                        if (millis < startTime) {
                            ToastUtil.showToast(OrderMissActivity.this, "日期不能大于今天");
                            return;
                        }
                        begin.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                    });
                    dialog.show();
                }, (v1, over) -> {
                    DatePickDialog dialog = new DatePickDialog(this);
                    //设置上下年分限制
                    dialog.setYearLimt(0);
                    //设置标题
                    dialog.setTitle("选择时间");
                    //设置类型
                    dialog.setType(DateType.TYPE_YMD);
                    //设置消息体的显示格式，日期格式
                    dialog.setMessageFormat("yyyy-MM-dd");
                    //设置选择回调
                    dialog.setOnChangeLisener(null);
                    //设置点击确定按钮回调
                    dialog.setOnSureLisener(date -> {
                        long millis = System.currentTimeMillis();
                        if (millis < date.getTime()) {
                            ToastUtil.showToast(OrderMissActivity.this, "日期不能大于今天");
                            return;
                        }
                        if(startTime > date.getTime()){
                            ToastUtil.showToast(OrderMissActivity.this,"开始日期不能大于结束日期");
                            return;
                        }
                        over.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                    });
                    dialog.show();
                }, (v1, begin, over) -> {
                    timeBegin = begin.getText().toString().trim();
                    timeOver = over.getText().toString().trim();
                    if (StringUtil.isEmpty(timeBegin)) {
                        ToastUtil.showToast(this, "请选择开始日期");
                        return;
                    }
                    getCancelOrderList(false);
                    DialogUtils.closeDialog();
                }, v1 -> DialogUtils.closeDialog());
                break;
            case R.id.img_rl: // 点击重新加载
                getCancelOrderList(false);
                break;
            case R.id.loading_ll:
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCancelOrderList(false);
    }

    // 消单列表
    private void getCancelOrderList(boolean isMore) {
        showProgressDialog();
        IdentityHashMap<String,String> params = new IdentityHashMap<>();
        int page = 1;
        int request = REQUEST_NET_ONE;
        String url = Constants.Urls.URL_POST_GET_CANCEL_ORDER_LIST;
        if (isMore) {
            request = REQUEST_NET_TWO;
            page = adapter.getPage() + 1;
        }

        params.put("store_id",storeId);
        params.put("begin_date",timeBegin);
        params.put("end_date",timeOver);
        params.put(CommonConstant.PAGESIZE,CommonConstant.PAGE_SIZE_10);
        params.put(CommonConstant.PAGENUM,String.valueOf(page));
        requestHttpData(url,request, FProtocol.HttpMethod.POST,params);

    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        lv.setOnRefreshComplete();
        OrderMissEntity pageEntity = Parsers.getOrderMissEntity(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (pageEntity != null) {
                    listEntities = pageEntity.getListEntities();
                    if (listEntities != null && listEntities.size() > 0) {
                        if (listEntities != null && listEntities.size() > 0) {
                            adapter = new OrderMissAdapter(this, listEntities);
                            lv.setAdapter(adapter);
                            noWifi.setVisibility(View.GONE);
                            if (pageEntity.getTotalPage() > adapter.getPage()) {
                                lv.setCanAddMore(true);
                            } else {
                                lv.setCanAddMore(false);
                            }
                        }else{
                            if(adapter != null){
                                adapter.clear();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }else{
                        lv.setVisibility(View.GONE);
                        imgRl.setVisibility(View.VISIBLE);
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            case REQUEST_NET_TWO:
                if(pageEntity != null){
                    List<OrderMissEntity.OrderMissListEntity> listEntities = pageEntity.getListEntities();
                    if(listEntities != null && listEntities.size() > 0){
                        adapter.addDatas(listEntities);
                        adapter.notifyDataSetChanged();
                        if(pageEntity.getTotalPage() > adapter.getPage()){
                            lv.setCanAddMore(true);
                        }else {
                            lv.setCanAddMore(false);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        closeProgressDialog();
        if (listEntities != null){
            listEntities.clear();
        }
        lv.setVisibility(View.GONE);
        noWifi.setVisibility(View.VISIBLE);
        super.mistake(requestCode, status, errorMessage);
    }
}
