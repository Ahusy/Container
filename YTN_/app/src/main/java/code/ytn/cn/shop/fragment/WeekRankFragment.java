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
 * Created by dell on 2018/7/5
 * 周排行
 */

public class WeekRankFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.week_frag_ll)
    private LinearLayout ll;
    @ViewInject(R.id.week_frag_arro)
    private ImageView arro;
    @ViewInject(R.id.week_frag_lv)
    private ListView lv;
    private String storeId;
    private LocalBroadcastManager instance;
    private String start;
    private String end;
    private String rank = ORDER_TYPE_DESC;
    private WeekRankAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_week_rank, container, false);
        ViewInjectUtils.inject(this, view);
        initData();
        queryWeekSisters();
        registerReceiver();
        return view;
    }

    // 周排行查询
    private void queryWeekSisters() {
        showProgressDialog();
        IdentityHashMap<String,String> params = new IdentityHashMap<>();
        params.put("store_id", storeId);
        params.put("start_time", start);
        params.put("end_time", end);
        params.put("type", "week");
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
                    if (rank.equals("asc")) {
                        arro.setImageDrawable(getResources().getDrawable(R.drawable.arrotop));
                    } else {
                        arro.setImageDrawable(getResources().getDrawable(R.drawable.arrobtm));
                    }
                    List<SalesRankEntity.SalesRankListEntity> listEntities = salesRankEntity.getListEntities();
                    if (listEntities != null && listEntities.size() > 0) {
                        adapter = new WeekRankAdapter(getActivity(), listEntities);
                        lv.setAdapter(adapter);
                    } else {
                        if (adapter != null) {
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                            ToastUtil.shortShow(getActivity(), "无售卖排行");
                        }
                    }
                }

                break;
        }
    }

    private void registerReceiver() {
        instance = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("weeks");
        instance.registerReceiver(mAdWownLoadReceiver, intentFilter);
    }

    private BroadcastReceiver mAdWownLoadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String change = intent.getStringExtra("change");
            start = intent.getStringExtra("start");
            end = intent.getStringExtra("end");
            if ("week".equals(change)) {
                new Handler().post(() ->
                        queryWeekSisters());
            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        instance.unregisterReceiver(mAdWownLoadReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.week_frag_ll:
                if (rank.equals(ORDER_TYPE_DESC)) {
                    rank = ORDER_TYPE_ASC;
                    queryWeekSisters();
                } else {
                    rank = ORDER_TYPE_DESC;
                    queryWeekSisters();
                }
                break;
        }
    }
}
