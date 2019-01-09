package code.ytn.cn.shop.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.ChooseGoodsEntity;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.shop.adapter.AddGoodsAdjustAdapter;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.LIST;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;

/**
 * 商品调价选择商品
 */
public class AddGoodsAdjustActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.add_goods_adjust_recy)
    private RecyclerView recy;
    @ViewInject(R.id.add_goods_adjust_cancel)
    private TextView cancel;
    @ViewInject(R.id.add_goods_adjust_comit)
    private TextView comit;
    private List<ChooseGoodsListEntity> entities;
    private ArrayList<ChooseGoodsListEntity> listGoods = new ArrayList<>();
    private String cabinetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods_adjust);
        ViewInjectUtils.inject(this);

        setCenterTitle("在售商品");
        initData();
        queryGoodsList();
    }

    private void initData() {
        cancel.setOnClickListener(this);
        comit.setOnClickListener(this);
        cabinetId = getIntent().getStringExtra("cabinet_id");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy.setLayoutManager(manager);
        // 设置动画
        recy.setItemAnimator(new DefaultItemAnimator());
        // 系统隔线
        recy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_goods_adjust_comit: // 确定
                for (int i = 0; i < entities.size(); i++) {
                    if (entities.get(i).isChecked()) {
                        ChooseGoodsListEntity listEntity = entities.get(i);
                        listGoods.add(listEntity);
                    }
                }
                Intent intent = new Intent();
                intent.putExtra(LIST, listGoods);
                setResult(4, intent);
                finish();
                break;
            case R.id.add_goods_adjust_cancel: // 取消
                finish();
                break;
        }
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        ChooseGoodsEntity entity = Parsers.getChooseGoodsEntity(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (entity != null) {
                    entities = entity.getListEntities();
                    AddGoodsAdjustAdapter adapter = new AddGoodsAdjustAdapter(this, entities);
                    recy.setAdapter(adapter);
                } else {
                    ToastUtil.shortShow(this, "暂无数据");
                }

                break;
        }
    }

    // 查询货柜商品
    private void queryGoodsList() {
        showProgressDialog();
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("cabinet_id", cabinetId);
        requestHttpData(Constants.Urls.URL_POST_CABINET_GOODSLIST, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }
}
