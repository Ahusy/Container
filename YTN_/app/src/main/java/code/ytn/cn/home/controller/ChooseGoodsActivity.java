package code.ytn.cn.home.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.viewinject.annotation.ViewInject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.event.ChooseGoods;
import code.ytn.cn.event.Count;
import code.ytn.cn.home.adapter.ChooseGoodsAdapter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.ChooseGoodsEntity;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.utils.SafeSharePreferenceUtil;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.ALLOT;
import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.EXTRA_FROM;
import static code.ytn.cn.common.CommonConstant.ID;
import static code.ytn.cn.common.CommonConstant.LIST;
import static code.ytn.cn.common.CommonConstant.NAME;
import static code.ytn.cn.common.CommonConstant.NEWINPUT;
import static code.ytn.cn.common.CommonConstant.NEW_OUT;
import static code.ytn.cn.common.CommonConstant.REPLMENT;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.STORE_ID;
import static code.ytn.cn.common.CommonConstant.TYPE;

/**
 * 选择商品2-12
 */
public class ChooseGoodsActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.choose_goods_comit)
    private TextView chooseComit;
    @ViewInject(R.id.choose_goods_lv)
    private RecyclerView chooseLv;
    @ViewInject(R.id.left_button)
    private ImageView left;
    private String id;
    private List<ChooseGoodsListEntity> entities;
    private List<ChooseGoodsListEntity> list = new ArrayList();
    private int from;
    private Count event;
    private String type;
    private String storeId;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_goods);
        list = (List<ChooseGoodsListEntity>) getIntent().getSerializableExtra(LIST);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_choose_goods));
        initClick();
        queryGoodsList();

    }

    private void initClick() {
        chooseComit.setOnClickListener(this);
        left.setOnClickListener(this);
        LinearLayoutManager mgs = new LinearLayoutManager(this);
        chooseLv.setLayoutManager(mgs);
        id = getIntent().getStringExtra(CABINET_ID);
        type = getIntent().getStringExtra(TYPE);
        storeId = getIntent().getStringExtra(STORE_ID);
        name = getIntent().getStringExtra(NAME);
//        depotName = getIntent().getStringExtra("depot_name");
        from = getIntent().getIntExtra(EXTRA_FROM, 0);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        int count = 0;
        ChooseGoodsEntity entity = Parsers.getChooseGoodsEntity(data);

        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (entity != null) {
                    entities = entity.getListEntities();

                    if (entities != null && entities.size() > 0) {
                        for (ChooseGoodsListEntity chooseGoodsListEntity : entities) {
                            String goodsId = chooseGoodsListEntity.getGoodsId();
                            boolean aBoolean = SafeSharePreferenceUtil.getBoolean(goodsId, false);
                            if (aBoolean) {
                                count++;
                            }
                        }
                        if (count > 0) {
                            chooseComit.setText(getString(R.string.comit_sure) + count + ")");
                        } else {

                        }
                        ChooseGoodsAdapter adapter = new ChooseGoodsAdapter(this, entities, list);
                        chooseLv.setAdapter(adapter);
                    }
                } else {
//                    ToastUtil.shortShow(this, "暂无数据");
                }

                break;
        }

    }

    // 查询货柜商品
    private void queryGoodsList() {
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("cabinet_id", id);
        requestHttpData(Constants.Urls.URL_POST_CABINET_GOODSLIST, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_goods_comit:
                if (ALLOT == from) {
                    List<ChooseGoodsListEntity> selectList = new ArrayList<>();
                    Intent intent = new Intent(ChooseGoodsActivity.this, GoodsAllotActivity.class);
                    for (ChooseGoodsListEntity chooseGoodsListEntity : entities) {
                        String goodsId = chooseGoodsListEntity.getGoodsId();

                        boolean aBoolean = SafeSharePreferenceUtil.getBoolean(goodsId, false);
                        if (aBoolean) {
                            selectList.add(chooseGoodsListEntity);
                        }

                    }
                    ChooseGoods goods = new ChooseGoods(selectList);
                    EventBus.getDefault().postSticky(goods);
                    intent.putExtra(TYPE, type);
                    intent.putExtra(STORE_ID, storeId);
                    intent.putExtra(CABINET_ID, id);
                    intent.putExtra(CABINET_NAME, name);
                    startActivity(intent);
                    finish();

                } else if (REPLMENT == from) { // 补货
                    List<ChooseGoodsListEntity> selectAddList = new ArrayList<>();
                    Intent intent = new Intent(ChooseGoodsActivity.this, ReplenishmentActivity.class);
                    for (ChooseGoodsListEntity chooseGoodsListEntity : entities) {
                        String goodsId = chooseGoodsListEntity.getGoodsId();

                        boolean aBoolean = SafeSharePreferenceUtil.getBoolean(goodsId, false);
                        if (aBoolean) {
                            selectAddList.add(chooseGoodsListEntity);
                        }
                    }
                    ChooseGoods goods = new ChooseGoods(selectAddList);
                    EventBus.getDefault().postSticky(goods);
                    intent.putExtra(ID, id);
                    intent.putExtra(NAME, name);
                    startActivity(intent);
                    finish();
                } else if (NEWINPUT == from) { // 新建入库
                    ArrayList<ChooseGoodsListEntity> selectAddList = new ArrayList<>();
                    for (ChooseGoodsListEntity chooseGoodsListEntity : entities) {
                        String goodsId = chooseGoodsListEntity.getGoodsId();

                        boolean aBoolean = SafeSharePreferenceUtil.getBoolean(goodsId, false);
                        if (aBoolean) {
                            selectAddList.add(chooseGoodsListEntity);
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra(LIST, selectAddList);
                    setResult(4, intent);
                    finish();
                } else if (NEW_OUT == from) { // 新建出库
                    ArrayList<ChooseGoodsListEntity> selectAddList = new ArrayList<>();
                    for (ChooseGoodsListEntity chooseGoodsListEntity : entities) {
                        String goodsId = chooseGoodsListEntity.getGoodsId();

                        boolean aBoolean = SafeSharePreferenceUtil.getBoolean(goodsId, false);
                        if (aBoolean) {
                            selectAddList.add(chooseGoodsListEntity);
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra(LIST, selectAddList);
                    setResult(4, intent);
                    finish();
                } else {
                    List<ChooseGoodsListEntity> selectAddList = new ArrayList<>();
                    Intent intent = new Intent(ChooseGoodsActivity.this, AddNewCheckActivity.class);
                    for (ChooseGoodsListEntity chooseGoodsListEntity : entities) {
                        String goodsId = chooseGoodsListEntity.getGoodsId();

                        boolean aBoolean = SafeSharePreferenceUtil.getBoolean(goodsId, false);
                        if (aBoolean) {
                            selectAddList.add(chooseGoodsListEntity);
                        }
                    }
                    ChooseGoods goods = new ChooseGoods(selectAddList);
                    EventBus.getDefault().postSticky(goods);
                    intent.putExtra(ID, id);
                    intent.putExtra(STORE_ID, storeId);
                    intent.putExtra(NAME, name);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.left_button:
                if (entities != null && entities.size() > 0) {
                    for (ChooseGoodsListEntity chooseGoodsListEntity : entities) {
                        String goodsId = chooseGoodsListEntity.getGoodsId();
                        SafeSharePreferenceUtil.saveBoolean(goodsId, false);
                    }
                }

                finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void Count(Count event) {
        this.event = event;
        chooseComit.setText(getString(R.string.comit_sure) + this.event.getCount() + ")");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 取消上次选中状态
        if (entities != null && entities.size() > 0) {
            for (ChooseGoodsListEntity chooseGoodsListEntity : entities) {
                String goodsId = chooseGoodsListEntity.getGoodsId();
                SafeSharePreferenceUtil.saveBoolean(goodsId, false);
            }
        }
    }
}

