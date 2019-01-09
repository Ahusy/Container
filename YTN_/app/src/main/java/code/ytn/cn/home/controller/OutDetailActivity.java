package code.ytn.cn.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.home.adapter.OutDetailAdapter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.OutGoodsDetailEntity;
import code.ytn.cn.network.entity.OutGoodsListEntity;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.NUM;
import static code.ytn.cn.common.CommonConstant.OP_NAME;
import static code.ytn.cn.common.CommonConstant.OUT_ID;
import static code.ytn.cn.common.CommonConstant.PRO_NAME;
import static code.ytn.cn.common.CommonConstant.RELATION_CODE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;
import static code.ytn.cn.common.CommonConstant.STATUS;
import static code.ytn.cn.common.CommonConstant.STATUS_ONE;
import static code.ytn.cn.common.CommonConstant.STATUS_THREE;
import static code.ytn.cn.common.CommonConstant.STATUS_TWO;
import static code.ytn.cn.common.CommonConstant.TIME;
import static code.ytn.cn.common.CommonConstant.TYPE;

/**
 * 出入单详情
 */
public class OutDetailActivity extends ToolBarActivity {
    @ViewInject(R.id.goout_detail_num)
    private TextView detailNum;
    @ViewInject(R.id.goout_detail_name)
    private TextView detailName;
    @ViewInject(R.id.goout_detail_time)
    private TextView detailTime;
    @ViewInject(R.id.goout_detazil_zd)
    private TextView detailZd;
    @ViewInject(R.id.goout_detail_cz)
    private TextView detailCz;
    @ViewInject(R.id.goout_detail_lv)
    private ListView detailLv;
    @ViewInject(R.id.goout_detail_select)
    private TextView select;
    private String outId;

    public static void startOutDetailActivity(Context context, String num, String time, String cabinetName, String proName, String cabinetId, String relationCode, String opName, String outId, String type, String status) {
        Intent intent = new Intent(context, OutDetailActivity.class);
        intent.putExtra(NUM, num);
        intent.putExtra(TIME, time);
        intent.putExtra(CABINET_NAME, cabinetName);
        intent.putExtra(PRO_NAME, proName);
        intent.putExtra(CABINET_ID, cabinetId);
        intent.putExtra(RELATION_CODE, relationCode);
        intent.putExtra(OP_NAME, opName);
        intent.putExtra(OUT_ID, outId);
        intent.putExtra(TYPE, type);
        intent.putExtra(STATUS, status);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_out_detail);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_detail));
        initData();
        queryDetail();
    }

    private void initData() {
        String code = getIntent().getStringExtra(NUM);
        String time = getIntent().getStringExtra(TIME);
        String cabinetName = getIntent().getStringExtra(CABINET_NAME);
        String proName = getIntent().getStringExtra(PRO_NAME);
        String cabiId = getIntent().getStringExtra(CABINET_ID);
        String relaCode = getIntent().getStringExtra(RELATION_CODE);
        String opName = getIntent().getStringExtra(OP_NAME);
        outId = getIntent().getStringExtra(OUT_ID);
        String status = getIntent().getStringExtra(STATUS);
        switch (status) {
            case STATUS_ONE:
                select.setBackground(getResources().getDrawable(R.drawable.pending));
                break;
            case STATUS_TWO:
                select.setBackground(getResources().getDrawable(R.drawable.off_the_stocks));
                break;
            case STATUS_THREE:
                select.setBackground(getResources().getDrawable(R.drawable.pending));
                break;
        }
        detailNum.setText(code);
        detailTime.setText(time);
        detailCz.setText(opName);
        detailZd.setText(proName);
        detailName.setText(cabinetName);

    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        OutGoodsDetailEntity entity = Parsers.getOutGoodsDetailEntity(data);
        switch (requestCode) {
            case REQUEST_NET_TWO:
                if (entity != null) {
                    List<OutGoodsListEntity> entities = entity.getListEntities();
                    if (entities != null && entities.size() > 0) {
                        OutDetailAdapter adapter = new OutDetailAdapter(this, entities);
                        detailLv.setAdapter(adapter);
                    }
                }
                break;
        }
    }

    // 查询出库详情
    private void queryDetail() {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("out_id", outId);
        requestHttpData(Constants.Urls.URL_POST_OUT_ORDER_DETAIL, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

}
