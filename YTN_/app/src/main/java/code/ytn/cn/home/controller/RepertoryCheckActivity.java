package code.ytn.cn.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import code.ytn.cn.home.adapter.RepertoryCheckAdapter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.CheckListEntity;
import code.ytn.cn.network.entity.RepertoryCheckEntity;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;
import static code.ytn.cn.common.CommonConstant.STORE_ID;

/**
 * 货柜盘点2-13
 */
public class RepertoryCheckActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.repertory_check_lv)
    private FootLoadingListView orderListView;
    @ViewInject(R.id.repertory_check_addNew)
    private TextView reperAddNew;
    private RepertoryCheckAdapter adapter;
    @ViewInject(R.id.right_button)
    private ImageView right;
    private List<CheckListEntity> entities;
    private String cabinetName;
    private String cabinetId;
    private String storeId;

    public static void startRepertoryCheckActivity(Context context,String cabinetId,String cabinetName,String storeId){
        Intent intent = new Intent(context,RepertoryCheckActivity.class);
        intent.putExtra(CABINET_ID,cabinetId);
        intent.putExtra(CABINET_NAME,cabinetName);
        intent.putExtra(STORE_ID,storeId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repertory_check);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_counter_check));
        initClick();
        cabinetName = getIntent().getStringExtra(CABINET_NAME);
        cabinetId = getIntent().getStringExtra(CABINET_ID);
        storeId = getIntent().getStringExtra(STORE_ID);
        loadData(false);
        orderListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(true);
            }
        });

        orderListView.setOnItemClickListener((parent, view, position, id1) ->
                RepertoryCheckDetailActivity.startRepertoryCheckDetailActivity(this,
                entities.get(position).getCheckCode(),
                entities.get(position).getRelaName(),
                entities.get(position).getCheckTime(),
                entities.get(position).getCheckId(),
                entities.get(position).getRelaId(),
                entities.get(position).getOperator()));
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
        String url = Constants.Urls.URL_POST_GET_CHECK_ORDERLIST;
        if (isMore) {
            request = REQUEST_NET_TWO;
            page = adapter.getPage() + 1;
        }
//        params.put("chant_id", UserCenter.getChantId(this));
        params.put("cabinet_id", cabinetId);
        params.put(CommonConstant.PAGESIZE, CommonConstant.PAGE_SIZE_10);
        params.put(CommonConstant.PAGENUM, String.valueOf(page));
        requestHttpData(url, request, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        orderListView.setOnRefreshComplete();
        RepertoryCheckEntity pageEntity = Parsers.getReperCheck(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (pageEntity != null) {
                    entities = pageEntity.getListEntities();
                    if (entities != null && entities.size() > 0) {
                        if (entities != null && entities.size() > 0) {
                            adapter = new RepertoryCheckAdapter(this, entities, this);
                            orderListView.setAdapter(adapter);
                            if (pageEntity.getTotalPage() > adapter.getPage()) {
                                orderListView.setCanAddMore(true);
                            } else {
                                orderListView.setCanAddMore(false);
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
                    List<CheckListEntity> listEntities = pageEntity.getListEntities();
                    if (listEntities != null && listEntities.size() > 0) {
                        adapter.addDatas(listEntities);
                        adapter.notifyDataSetChanged();
                        if (pageEntity.getTotalPage() > adapter.getPage()) {
                            orderListView.setCanAddMore(true);
                        } else {
                            orderListView.setCanAddMore(false);
                        }
                    }
                }
                break;
        }
    }

    private void initClick() {
        reperAddNew.setOnClickListener(this);
        right.setOnClickListener(this);
        right.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repertory_check_addNew:
                AddNewCheckActivity.startAddNewCheckActivity(this,cabinetName,cabinetId,storeId);
                break;
        }
    }


}
