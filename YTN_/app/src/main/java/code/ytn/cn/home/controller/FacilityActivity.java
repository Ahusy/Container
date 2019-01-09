package code.ytn.cn.home.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.STATUS;

/**
 * 设备管理2-18
 */
public class FacilityActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.facility_polling)
    private ImageView facPolling;
    @ViewInject(R.id.facility_repairs)
    private ImageView facRepa;
    @ViewInject(R.id.facility_lock)
    private ImageView facLock;
    @ViewInject(R.id.facility_hard_manager)
    private ImageView facManager;
    @ViewInject(R.id.facility_phone)
    private TextView facPhone;
    private String cabinetId;
    private String cabinetName;
    private String status;
    private String phone;

    public static void startFacilityActivity(Context context,String id,String name,String stauts){
        Intent intent = new Intent(context,FacilityActivity.class);
        intent.putExtra(CABINET_ID,id);
        intent.putExtra(CABINET_NAME,name);
        intent.putExtra(STATUS,stauts);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_faclity));
        initClick();
    }

    private void initClick() {
        facPolling.setOnClickListener(this);
        facRepa.setOnClickListener(this);
        facLock.setOnClickListener(this);
        facPhone.setOnClickListener(this);
        facManager.setOnClickListener(this);
        cabinetId = getIntent().getStringExtra(CABINET_ID);
        cabinetName= getIntent().getStringExtra(CABINET_NAME);
        status = getIntent().getStringExtra(STATUS);
        phone = facPhone.getText().toString().trim().substring(5, 15);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.facility_polling: // 设备巡检
                ToastUtil.showToast(this, getString(R.string.coming_soon));
                break;
            case R.id.facility_repairs: // 设备报修
                RepairsActivity.startRepairsActivity(this,cabinetId,cabinetName,status);
                break;
            case R.id.facility_lock:    // 冰柜锁定
                ToastUtil.showToast(this, getString(R.string.coming_soon));
                break;
            case R.id.facility_hard_manager: // 硬件管理
                FacilityStatusActivity.startFacilityStatusActivity(this,cabinetId,cabinetName);
                break;
            case R.id.facility_phone: // 拨打电话
                DialogUtils.showTwoBtnDialog(this, "确定拨打此号码?", phone, v1 -> {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                    DialogUtils.closeDialog();
                }, v1 -> DialogUtils.closeDialog());

                break;
        }
    }

}
