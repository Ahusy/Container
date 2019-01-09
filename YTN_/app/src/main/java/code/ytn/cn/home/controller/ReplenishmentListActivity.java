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
import code.ytn.cn.home.adapter.ReplenishmentListAdapter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.ReplmentEntity;
import code.ytn.cn.network.entity.ReplmentListEntity;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;

/**
 * 申请补货列表
 */
public class ReplenishmentListActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.replment_add_lv)
    private FootLoadingListView lv;
    @ViewInject(R.id.replment_add_new)
    private TextView replmentNew;
    private String cabinetId;
    private String cabinetName;
    private ReplenishmentListAdapter adapter;
    private List<ReplmentListEntity> entities;

    public static void startReplenishmentListActivity(Context context, String id, String name) {
        Intent intent = new Intent(context, ReplenishmentListActivity.class);
        intent.putExtra(CABINET_ID, id);
        intent.putExtra(CABINET_NAME, name);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replenishment_list);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_replment_list));
        initData();
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

        lv.setOnItemClickListener((parent, view, position, id1) ->
                ReplmentDetailActivity.startReplmentDetailActivity(this,
                        entities.get(position).getId(),
                        entities.get(position).getReplenCode(),
                        entities.get(position).getCabinetName(),
                        entities.get(position).getCreateData(),
                        entities.get(position).getCreateUserName()));
    }

    private void initData() {
        cabinetId = getIntent().getStringExtra(CABINET_ID);
        cabinetName = getIntent().getStringExtra(CABINET_NAME);
        replmentNew.setOnClickListener(this);
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
        String url = Constants.Urls.URL_POST_ORDER_ADD_LIST;
        if (isMore) {
            request = REQUEST_NET_TWO;
            page = adapter.getPage() + 1;
        }
        params.put("cabinet_id", cabinetId);
        params.put(CommonConstant.PAGESIZE, CommonConstant.PAGE_SIZE_10);
        params.put(CommonConstant.PAGENUM, String.valueOf(page));
        requestHttpData(url, request, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        lv.setOnRefreshComplete();
        ReplmentEntity pageEntity = Parsers.getReplmentEntity(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (pageEntity != null) {
                    entities = pageEntity.getListEntities();
                    if (entities != null && entities.size() > 0) {
                        if (entities != null && entities.size() > 0) {
                            adapter = new ReplenishmentListAdapter(this, entities, this);
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
                    List<ReplmentListEntity> listEntities = pageEntity.getListEntities();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.replment_add_new:
                ReplenishmentActivity.startReplenishmentActivity(this,cabinetId,cabinetName);
                break;
        }
    }
}
