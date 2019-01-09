package code.ytn.cn.shop.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.BaseFragment;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.SalesRankEntity;
import code.ytn.cn.utils.SafeSharePreferenceUtil;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.ORDER_TYPE_ASC;
import static code.ytn.cn.common.CommonConstant.ORDER_TYPE_DESC;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_SUCCESS;

/**
 * 日排行
 */

public class TodayRankFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.today_frag_ll)
    private LinearLayout ll;
    @ViewInject(R.id.today_frag_arro)
    private ImageView arro;
    @ViewInject(R.id.today_frag_lv)
    private ListView lv;
    private String storeId;
    private LocalBroadcastManager instance;
    private String begin;
    private String over;
    private String rank = ORDER_TYPE_DESC;
    private TodayRankAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_today_rank, container, false);
        ViewInjectUtils.inject(this, view);
        initData();
        queryDaySisters();
        registerReceiver();
        return view;
    }

    // 查询日排行
    private void queryDaySisters() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("store_id", storeId);
        params.put("start_time", begin);
        params.put("end_time", over);
        params.put("type", "day");
        params.put("sort", rank);
        requestHttpData(Constants.Urls.URL_POST_GET_SALES_STATIS_LIST, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    private void initData() {
        storeId = SafeSharePreferenceUtil.getString("storeId", "");
        ll.setOnClickListener(this);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        SalesRankEntity salesRankEntity = Parsers.getSalesRankEntity(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (salesRankEntity.getResultCode().equals(REQUEST_NET_SUCCESS)) {
                    if (rank.equals(ORDER_TYPE_ASC)) {
                        arro.setImageDrawable(getResources().getDrawable(R.drawable.arrotop));
                    } else {
                        arro.setImageDrawable(getResources().getDrawable(R.drawable.arrobtm));
                    }
                    List<SalesRankEntity.SalesRankListEntity> listEntities = salesRankEntity.getListEntities();
                    if (listEntities != null && listEntities.size() > 0) {
                        adapter = new TodayRankAdapter(getActivity(), listEntities);
                        lv.setAdapter(adapter);
                    } else {
                        ToastUtil.shortShow(getActivity(), "无售卖排行");
                        if (adapter != null) {
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
        }
    }

    // 注册广播接收器
    private void registerReceiver() {
        instance = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("days");
        instance.registerReceiver(mAdDownLoadReceiver, intentFilter);
    }

    private BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String change = intent.getStringExtra("change");
            begin = intent.getStringExtra("begin");
            over = intent.getStringExtra("over");
            if ("day".equals(change)) {
                new Handler().post(() ->
                        queryDaySisters());
            }
        }
    };

    // 注销
    @Override
    public void onDetach() {
        super.onDetach();
        instance.unregisterReceiver(mAdDownLoadReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.today_frag_ll:
                if (rank.equals(ORDER_TYPE_DESC)) {
                    rank = ORDER_TYPE_ASC;
                    queryDaySisters();
                } else {
                    rank = ORDER_TYPE_DESC;
                    queryDaySisters();
                }
                break;
        }
    }
}
