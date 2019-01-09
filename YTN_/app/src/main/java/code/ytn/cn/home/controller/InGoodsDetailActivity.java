package code.ytn.cn.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.home.adapter.InputDetailtAdapter;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.InGoodsListEntity;
import code.ytn.cn.network.entity.InputGoodsDetailEntity;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.ID;
import static code.ytn.cn.common.CommonConstant.NUM;
import static code.ytn.cn.common.CommonConstant.PRO_NAME;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.STATUS;
import static code.ytn.cn.common.CommonConstant.STATUS_ONE;
import static code.ytn.cn.common.CommonConstant.STATUS_THREE;
import static code.ytn.cn.common.CommonConstant.STATUS_TWO;
import static code.ytn.cn.common.CommonConstant.TIME;

/**
 * 入库详情
 */
public class InGoodsDetailActivity extends ToolBarActivity {

    @ViewInject(R.id.input_detail_num)
    private TextView detailNum;
    @ViewInject(R.id.input_detail_name)
    private TextView detailName;
    @ViewInject(R.id.input_detail_time)
    private TextView detailTime;
    @ViewInject(R.id.input_detazil_zd)
    private TextView detailZd;
    @ViewInject(R.id.input_detail_cz)
    private TextView detailCz;
    @ViewInject(R.id.input_detail_lv)
    private ListView detailLv;
    @ViewInject(R.id.input_detail_select)
    private TextView select;
    private String id;

    public static void startInGoodsDetailActivity(Context context,String num,String time,String cabinetName,String proName,String id,String stauts){
        Intent intent = new Intent(context,InGoodsDetailActivity.class);
        intent.putExtra(NUM,num);
        intent.putExtra(TIME,time);
        intent.putExtra(CABINET_NAME,cabinetName);
        intent.putExtra(PRO_NAME,proName);
        intent.putExtra(ID,id);
        intent.putExtra(STATUS,stauts);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_out_goods_detail);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_detail));
        initData();
        inOrderList();
    }

    // 初始化数据
    private void initData() {
        String code = getIntent().getStringExtra(NUM);
        String time = getIntent().getStringExtra(TIME);
        String cabinetName = getIntent().getStringExtra(CABINET_NAME);
        String proName = getIntent().getStringExtra(PRO_NAME);
        id = getIntent().getStringExtra(ID);
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
        detailName.setText(cabinetName);
        detailTime.setText(time);
        detailZd.setText(proName);
        detailCz.setText(UserCenter.getName(this));
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        InputGoodsDetailEntity entity = Parsers.getInputGoodsDetail(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (entity != null) {
                    List<InGoodsListEntity> entities = entity.getListEntities();
                    if (entities != null && entities.size() > 0) {
                        InputDetailtAdapter adapter = new InputDetailtAdapter(this, entities);
                        detailLv.setAdapter(adapter);
                    }
                } else {
                    ToastUtil.shortShow(this, getString(R.string.toast_no_context));

                }
                break;
        }
    }

    // 查询详情
    private void inOrderList() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("in_id", id);
        requestHttpData(Constants.Urls.URL_POST_IN_ORDERLIST_DETAIL, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

}
