package code.ytn.cn.login.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.common.CommonConstant;
import code.ytn.cn.home.controller.CounterListActivity;
import code.ytn.cn.home.controller.RepairsActivity;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.UserEntity;
import code.ytn.cn.shop.controller.ShopManagerHomeListActivity;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.ExitManager;
import code.ytn.cn.utils.InputUtil;
import code.ytn.cn.utils.SafeSharePreferenceUtil;
import code.ytn.cn.utils.VerifyUtils;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CLOCKIN;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;

/**
 * 登录页面
 */
public class LoginActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.login_comit)
    private TextView loginComit;
    @ViewInject(R.id.login_sh_num)
    private EditText shNum;
    @ViewInject(R.id.login_account_num)
    private EditText accountNum;
    @ViewInject(R.id.login_password)
    private EditText passwrod;
    @ViewInject(R.id.login_forget_password)
    private TextView fogetPassword;
    @ViewInject(R.id.left_button)
    private ImageView left;

    public static final long DIFF_DEFAULT_BACK_TIME = 2000;

    private long mBackTime = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.login));
        left.setVisibility(View.GONE);
        InputUtil.editIsEmpty(loginComit, shNum, accountNum, passwrod);
        initClick();
        findViewById(R.id.img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RepairsActivity.class));
            }
        });
    }

    private void initClick() {
        loginComit.setOnClickListener(this);
        fogetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_forget_password:  // 忘记密码
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.login_comit:  // 登录
                String strName = VerifyUtils.stringFilter(accountNum.getText().toString());
                if (!strName.equals(accountNum.getText().toString())) {
                    ToastUtil.shortShow(this, getString(R.string.login_toa_text));
                    if (passwrod.getText().toString().length() < 6) {
                        ToastUtil.shortShow(this, getString(R.string.login_toa_pwd_text));
                        break;
                    }
                    if (passwrod.getText().toString().length() > 16) {
                        break;
                    }
                    break;
                }
                login();
                break;
        }
    }

    private void login() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("chant_code", shNum.getText().toString());
        params.put("login_name", accountNum.getText().toString());
        params.put("password", passwrod.getText().toString());
        params.put("token", "1");
        requestHttpData(Constants.Urls.URL_POST_LOGIN, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                UserEntity userEntity = Parsers.getUserInfo(data);
                UserCenter.saveUserInfo(this, userEntity);
                if (userEntity.getResultCode().equals(CommonConstant.REQUEST_NET_SUCCESS)) {
                    if (userEntity.getRank().equals("4") || userEntity.getRank().equals("1")) {
                        DialogUtils.showOneBtnDialog(this, userEntity.getResultMsg(), v -> {
                            SafeSharePreferenceUtil.saveString(CLOCKIN, "1");
                            startActivity(new Intent(this, ShopManagerHomeListActivity.class));
                            finish();
                            DialogUtils.closeDialog();
                        });
                    } else {
                        DialogUtils.showOneBtnDialog(this, userEntity.getResultMsg(), v -> {
                            startActivity(new Intent(this, CounterListActivity.class));
                            finish();
                            DialogUtils.closeDialog();
                        });
                    }
                }

                break;
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        super.mistake(requestCode, status, errorMessage);
        closeProgressDialog();
        ToastUtil.showToast(this, errorMessage);
    }

    @Override
    public void onBackPressed() {
        long nowTime = System.currentTimeMillis();
        long diff = nowTime - mBackTime;
        if (diff >= DIFF_DEFAULT_BACK_TIME) {
            mBackTime = nowTime;
            Toast.makeText(getApplicationContext(), R.string.toast_back_again_exit, Toast.LENGTH_SHORT).show();
        } else {
            ExitManager.instance.exit();
        }
    }
}
