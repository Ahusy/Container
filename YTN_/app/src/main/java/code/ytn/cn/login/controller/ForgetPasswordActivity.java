package code.ytn.cn.login.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.common.CommonConstant;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.utils.DownTimeUtil;
import code.ytn.cn.utils.InputUtil;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;

/**
 * 找回密码
 */
public class ForgetPasswordActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.forget_chant_num)
    private EditText forgNum;
    @ViewInject(R.id.forget_chant_name)
    private EditText forgName;
    @ViewInject(R.id.forget_new_password)
    private EditText forgNewPassword;
    @ViewInject(R.id.forget_sure_password)
    private EditText forgSurePassword;
    @ViewInject(R.id.forget_verification_code)
    private EditText forgetCode;
    @ViewInject(R.id.forget_get_verification_code)
    private TextView forgGetVerifiCode;
    @ViewInject(R.id.forget_comit)
    private TextView forgComit;
    private DownTimeUtil downTimeUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_forgetpwd));
        initClick();
        InputUtil.editIsEmpty(forgComit,forgNum,forgName,forgNewPassword,forgSurePassword,forgetCode);
        downTimeUtil = new DownTimeUtil(this);
        downTimeUtil.initCountDownTime(forgGetVerifiCode);
    }

    private void initClick() {
        forgComit.setOnClickListener(this);
        forgGetVerifiCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forget_comit: // 确认
                if(forgNum.getText().toString().equals("")){
                    ToastUtil.shortShow(this, getString(R.string.register_input_chant_num));
                    break;
                }
                if(forgName.getText().toString().equals("")){
                    ToastUtil.shortShow(this, getString(R.string.register_input_chant_name));
                    break;
                }
                if(forgNewPassword.getText().toString().length() < 6){
                    ToastUtil.shortShow(this,getString(R.string.toast_newpwd));
                    break;
                }
                if(forgSurePassword.getText().toString().length() > 16){
                    ToastUtil.shortShow(this,getString(R.string.toast_surepwd));
                    break;
                }
                if (forgNewPassword.getText().toString().equals(forgSurePassword.getText().toString())) {
                    resetPassword();
                } else {
                    ToastUtil.shortShow(this, getString(R.string.login_change_pwd_different));
                }
                break;
            case R.id.forget_get_verification_code:
                if(forgNum.getText().toString().equals("")){
                    ToastUtil.shortShow(this, getString(R.string.register_input_chant_num));
                    break;
                }
                if(forgName.getText().toString().equals("")){
                    ToastUtil.shortShow(this, getString(R.string.register_input_chant_name));
                    break;
                }
                getVerifyCode();
                downTimeUtil.countDownTimer.start();
                break;
        }
    }


    @Override
    public void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        Entity entity = Parsers.getResult(data);
        switch (requestCode) {
            case REQUEST_NET_ONE://获取验证码
                if (CommonConstant.REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
                    ToastUtil.shortShow(this, entity.getResultMsg());
                    closeProgressDialog();
                } else {
                    closeProgressDialog();
                    ToastUtil.shortShow(this, entity.getResultMsg());
                    forgGetVerifiCode.setEnabled(true);
                    forgGetVerifiCode.setText(getString(R.string.register_get_verify_code));
                    downTimeUtil.countDownTimer.cancel();
                }
                break;

            case REQUEST_NET_TWO://重新设置新密码
                if (CommonConstant.REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
                startActivity(new Intent(this, LoginActivity.class));
                ToastUtil.shortShow(this, entity.getResultMsg());
                finish();
            }else{
                ToastUtil.shortShow(this, entity.getResultMsg());
            }
                break;

        }
    }

    // 获取短信验证码
    private void getVerifyCode() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("chant_code", forgNum.getText().toString());
        params.put("login_name", forgName.getText().toString());
        requestHttpData(Constants.Urls.URL_POST_GET_IDENTIFY_CODE, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    private void resetPassword() {
            showProgressDialog();
            IdentityHashMap<String, String> params = new IdentityHashMap<>();
            params.put("chant_code", forgNum.getText().toString());
            params.put("login_name", forgName.getText().toString());
            params.put("password", forgNewPassword.getText().toString());
            params.put("", forgSurePassword.getText().toString());
            params.put("identify_code", forgetCode.getText().toString());
            requestHttpData(Constants.Urls.URL_POST_RETRIEVE_PASSWORD, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
        }
    }
