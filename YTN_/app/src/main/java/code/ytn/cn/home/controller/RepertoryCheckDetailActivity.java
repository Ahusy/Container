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
import code.ytn.cn.home.adapter.ReperCheckDetailAdapter;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.ReperCheckDetailEntity;
import code.ytn.cn.network.entity.ReperCheckDetailListEntity;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CHECK_ID;
import static code.ytn.cn.common.CommonConstant.NUM;
import static code.ytn.cn.common.CommonConstant.OPERATOR;
import static code.ytn.cn.common.CommonConstant.RELATION_ID;
import static code.ytn.cn.common.CommonConstant.RELATION_NAME;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.TIME;

/**
 * 盘点详情
 */
public class RepertoryCheckDetailActivity extends ToolBarActivity {

    @ViewInject(R.id.repertory_num)
    private TextView repNum;
    @ViewInject(R.id.repertory_name)
    private TextView repName;
    @ViewInject(R.id.repertory_time)
    private TextView repTime;
    @ViewInject(R.id.repertory_person_zd)
    private TextView personZd;
    @ViewInject(R.id.repertory_lv)
    private ListView lv;
    private String checkId;

    public static void startRepertoryCheckDetailActivity(Context context, String num, String relationName, String time, String checkId, String relationId, String operator) {
        Intent intent = new Intent(context, RepertoryCheckDetailActivity.class);
        intent.putExtra(NUM, num);
        intent.putExtra(RELATION_NAME, relationName);
        intent.putExtra(TIME, time);
        intent.putExtra(CHECK_ID, checkId);
        intent.putExtra(RELATION_ID, relationId);
        intent.putExtra(OPERATOR, operator);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repertory_check_detail);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_detail));
        initData();
        checkOrderDetail();
    }

    private void initData() {
        String num = getIntent().getStringExtra(NUM);
        String name = getIntent().getStringExtra(RELATION_NAME);
        String time = getIntent().getStringExtra(TIME);
        checkId = getIntent().getStringExtra(CHECK_ID);
        String relaId = getIntent().getStringExtra(RELATION_ID);
        String operator = getIntent().getStringExtra(OPERATOR);
        repNum.setText(num);
        repName.setText(name);
        repTime.setText(time);
        personZd.setText(operator);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        ReperCheckDetailEntity entity = Parsers.getReperCheckDeatil(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (entity != null) {
                    List<ReperCheckDetailListEntity> entities = entity.getListEntities();
                    if (entities != null && entities.size() > 0) {
                        if (entities != null && entities.size() > 0) {
                            ReperCheckDetailAdapter adapter = new ReperCheckDetailAdapter(this, entities);
                            lv.setAdapter(adapter);
                        }
                    } else {
                        ToastUtil.shortShow(this, getString(R.string.toast_no_context));
                    }

                    break;
                }
        }
    }

    // 盘点详情
    private void checkOrderDetail() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("chant_id", UserCenter.getChantId(this));
        params.put("check_id", checkId);
        requestHttpData(Constants.Urls.URL_POST_GET_CHECK_ORDERLIST_DETAIL, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

}
