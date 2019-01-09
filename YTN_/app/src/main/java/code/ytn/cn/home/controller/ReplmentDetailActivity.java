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
import code.ytn.cn.home.adapter.ReplmentDetailAdapter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.ReplmentDetailEntity;
import code.ytn.cn.network.entity.ReplmentListDetailEntity;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.ID;
import static code.ytn.cn.common.CommonConstant.NUM;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.TIME;
import static code.ytn.cn.common.CommonConstant.USER_NAME;

/**
 * 补货详情
 */
public class ReplmentDetailActivity extends ToolBarActivity {

    @ViewInject(R.id.replment_detail_num)
    private TextView repDetailNum;
    @ViewInject(R.id.replment_detail_select)
    private TextView repDetailSelect;
    @ViewInject(R.id.replment_detail_name)
    private TextView repDetailName;
    @ViewInject(R.id.replment_detail_time)
    private TextView repDetailTime;
    @ViewInject(R.id.replment_detail_person)
    private TextView repDetailPerson;
    @ViewInject(R.id.replment_detail_lv)
    private ListView lv;
    private String id;

    public static void startReplmentDetailActivity(Context context,String id,String num,String cabinetName,String time,String userName){
        Intent intent = new Intent(context,ReplmentDetailActivity.class);
        intent.putExtra(ID,id);
        intent.putExtra(NUM,num);
        intent.putExtra(CABINET_NAME,cabinetName);
        intent.putExtra(TIME,time);
        intent.putExtra(USER_NAME,userName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replment_detail);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_replment_detail));
        initData();
        getReplmentDetail();
    }

    private void initData() {
        id = getIntent().getStringExtra(ID);
        String name = getIntent().getStringExtra(CABINET_NAME);
        String num = getIntent().getStringExtra(NUM);
        String userName = getIntent().getStringExtra(USER_NAME);
        String time = getIntent().getStringExtra(TIME);

        repDetailNum.setText(num);
        repDetailName.setText(name);
        repDetailTime.setText(time);
        repDetailPerson.setText(userName);

    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        ReplmentDetailEntity entity = Parsers.getReplmentDetailEntity(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (entity != null) {
                    List<ReplmentListDetailEntity> entities = entity.getListDetailEntities();
                    if (entities != null && entities.size() > 0) {
                        if (entities != null && entities.size() > 0) {
                            ReplmentDetailAdapter adapter = new ReplmentDetailAdapter(this, entities);
                            lv.setAdapter(adapter);
                        }
                    } else {
                        ToastUtil.shortShow(this, getString(R.string.toast_no_context));
                    }

                    break;
                }
        }
    }

    // 详情
    private void getReplmentDetail() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("replenish_id", id);
        requestHttpData(Constants.Urls.URL_POST_ORDER_GOODS_LIST, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

}
