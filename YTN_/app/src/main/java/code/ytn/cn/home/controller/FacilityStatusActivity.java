package code.ytn.cn.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.FacilityStatusEntity;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.STATUS_ONE;

/**
 * 设备状态
 */
public class FacilityStatusActivity extends ToolBarActivity {

    @ViewInject(R.id.facility_status_name)
    private TextView statusName;
    @ViewInject(R.id.facility_status_status)
    private TextView statusSta;
    @ViewInject(R.id.facility_status_desc)
    private TextView statusDesc;
    private String id;

    public static void startFacilityStatusActivity(Context context,String cabinetId,String cabinetName){
        Intent intent = new Intent(context,FacilityStatusActivity.class);
        intent.putExtra(CABINET_ID,cabinetId);
        intent.putExtra(CABINET_NAME,cabinetName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_status);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_look_stauts));
        initData();
        queryStatus();
    }

    private void initData() {
        id = getIntent().getStringExtra(CABINET_ID);
        String name = getIntent().getStringExtra(CABINET_NAME);
        statusName.setText(name);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        FacilityStatusEntity entity = Parsers.getFacilityStatusEntity(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (entity != null) {
                    if (entity.getState().equals(STATUS_ONE)) {
                        statusSta.setText(R.string.text_normal);
                        if (!entity.getUserId().equals("")) {
                            statusDesc.setText(entity.getDesc());
                        }
                    } else {
                        statusSta.setText(R.string.text_abnormal);
                        statusDesc.setText(entity.getDesc());
                    }
                }
                break;
        }
    }

    // 查询货柜状态
    private void queryStatus() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("cabinet_id", id);
        requestHttpData(Constants.Urls.URL_POST_CHECK_CABINET_STATUS, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

}
