package code.ytn.cn.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;

import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.common.CommonConstant;
import code.ytn.cn.home.adapter.OutStorageListAdapter;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.OutGoodsEntity;
import code.ytn.cn.network.entity.OutStorageListEntity;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.DEPOT_NAME;
import static code.ytn.cn.common.CommonConstant.OPEN_DEPOT;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_SUCCESS;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;

/**
 * 出货管理2-8
 */
public class OutGoodsActivity extends ToolBarActivity {

    @ViewInject(R.id.goout_goods_lv)
    private FootLoadingListView lv;
    @ViewInject(R.id.goout_goods_news)
    private TextView goodsNews;
    private List<OutStorageListEntity> entities;
    private String cabinetId;
    private OutStorageListAdapter adapter;
    private String cabinetName;
    private String depotName;

    public static void startGooutGoodsActivity(Context context, String id, String openDepot, String name, String depotName) {
        Intent intent = new Intent(context, OutGoodsActivity.class);
        intent.putExtra(CABINET_ID, id);
        intent.putExtra(OPEN_DEPOT, openDepot);
        intent.putExtra(CABINET_NAME, name);
        intent.putExtra(DEPOT_NAME, depotName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goout_goods);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_goout));
        cabinetId = getIntent().getStringExtra(CABINET_ID);
        cabinetName = getIntent().getStringExtra(CABINET_NAME);
        String openDepot = getIntent().getStringExtra(OPEN_DEPOT);
        depotName = getIntent().getStringExtra(DEPOT_NAME);
        if (openDepot != null && !openDepot.equals(REQUEST_NET_SUCCESS)) {
            goodsNews.setVisibility(View.VISIBLE);
        } else {
            goodsNews.setVisibility(View.GONE);
        }
        // 加载数据
        loadData(false);

        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(true);
            }
        });

        lv.setOnItemClickListener((parent, view, position, id1) -> {
            if (entities.get(position).getStatus().equals(CommonConstant.STATUS_ONE)) {
                ExecuteOutActivity.startExecuteOutActivity(this,
                        entities.get(position).getOutOrderCode(),
                        entities.get(position).getTransTime(),
                        entities.get(position).getCabinetName(),
                        entities.get(position).getProduName(),
                        entities.get(position).getCabinetId(),
                        entities.get(position).getRelationCode(),
                        entities.get(position).getOperName(),
                        entities.get(position).getOutId(),
                        entities.get(position).getDepotType());
            } else if (entities.get(position).getStatus().equals(CommonConstant.STATUS_TWO)) {
                OutDetailActivity.startOutDetailActivity(this,
                        entities.get(position).getOutOrderCode(),
                        entities.get(position).getTransTime(),
                        entities.get(position).getCabinetName(),
                        entities.get(position).getProduName(),
                        entities.get(position).getCabinetId(),
                        entities.get(position).getRelationCode(),
                        entities.get(position).getOperName(),
                        entities.get(position).getOutId(),
                        entities.get(position).getDepotType(),
                        entities.get(position).getStatus());
            }
        });

        goodsNews.setOnClickListener(v -> NewOutActivity.startNewOutActivity(this,cabinetId,cabinetName,depotName));

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(false);
    }

    private void loadData(boolean isMore) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        int page = 1;
        int request = REQUEST_NET_ONE;
        String url = Constants.Urls.URL_POST_OUT_STORAGELIST;
        if (isMore) {
            request = REQUEST_NET_TWO;
            page = adapter.getPage() + 1;
        }
        params.put("cabinet_id", cabinetId);
        params.put("chant_id", UserCenter.getChantId(this));
        params.put(CommonConstant.PAGESIZE, CommonConstant.PAGE_SIZE_10);
        params.put(CommonConstant.PAGENUM, String.valueOf(page));
        requestHttpData(url, request, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        lv.setOnRefreshComplete();
        OutGoodsEntity pageEntity = Parsers.getOutGoods(data);
        // 奖励金累积
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (pageEntity != null) {
                    entities = pageEntity.getOutStorageListEntities();
                    if (entities != null && entities.size() > 0) {
                        if (entities != null && entities.size() > 0) {
                            adapter = new OutStorageListAdapter(this, entities);
                            lv.setAdapter(adapter);
                            if (pageEntity.getTotalPage() > adapter.getPage()) {
                                lv.setCanAddMore(true);
                            } else {
                                lv.setCanAddMore(false);
                            }
                        } else {
                            if (adapter != null) {
                                adapter.clear();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                break;
            case REQUEST_NET_TWO://加载更多
                if (pageEntity != null) {
                    List<OutStorageListEntity> listEntities = pageEntity.getOutStorageListEntities();
                    if (listEntities != null && listEntities.size() > 0) {
                        adapter.addDatas(listEntities);
                        adapter.notifyDataSetChanged();
                        if (pageEntity.getTotalPage() > adapter.getPage()) {
                            lv.setCanAddMore(true);
                        } else {
                            lv.setCanAddMore(false);
                        }
                    }
                }
                break;
        }
    }
}
