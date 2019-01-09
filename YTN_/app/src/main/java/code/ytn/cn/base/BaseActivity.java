package code.ytn.cn.base;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.common.interfaces.IActivityHelper;
import com.common.network.FProtocol;
import com.common.ui.FBaseActivity;
import com.common.utils.ToastUtil;
import com.common.utils.VersionInfoUtils;

import java.util.IdentityHashMap;

import cn.jpush.android.api.JPushInterface;
import code.ytn.cn.MainApplication;
import code.ytn.cn.login.controller.LoginActivity;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.utils.ConfigUtils;
import code.ytn.cn.utils.ExitManager;
import code.ytn.cn.utils.SafeSharePreferenceUtil;

import static code.ytn.cn.common.CommonConstant.LATI_TUDE;
import static code.ytn.cn.common.CommonConstant.LONG_ITUDE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_SUCCESS;

public class BaseActivity extends FBaseActivity implements IActivityHelper {

    public static final int REQUEST_UPDATE_VERSION_CODE = -3;
    public static final int REQUEST_PERMISSION_CODE = 0x1;

    protected Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //建议在Base中写，解决在系统设置中修改了语言之后，切回应用语言会变成刚修改的语言的问题
        //两种处理方法，一种是在BaseActivity的onCreate方法的super.onCreate()方法之前设置语言
        //第二种方法是重写BaseActivity的getResources()方法，返回新的Resource
        MainApplication.configLanguage(this);
        super.onCreate(savedInstanceState);
        ExitManager.instance.addActivity(this);
        res = getResources();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    public void requestHttpData(String path, int requestCode) {
        super.requestHttpData(path, requestCode);
    }

    @Override
    public void requestHttpData(String path, int requestCode, FProtocol.HttpMethod method, IdentityHashMap<String, String> postParameters) {
        if (postParameters == null) {
            postParameters = new IdentityHashMap<>();
        }
        String latitude = SafeSharePreferenceUtil.getString(LATI_TUDE, "");
        String longitude = SafeSharePreferenceUtil.getString(LONG_ITUDE, "");
        postParameters.put("latitude",latitude);
        postParameters.put("longitude",longitude);
        postParameters.put("user_id", UserCenter.getUserId(this));
        postParameters.put("language", ConfigUtils.getPreLanguage(this));
        super.requestHttpData(path, requestCode, method, postParameters);
    }

    /**
     * 检查更新
     */
    protected void checkUpdateVersion() {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("version_code", String.valueOf(VersionInfoUtils.getVersionCode(this)));
        params.put("terminal","10");
        requestHttpData(Constants.Urls.URL_POST_CHECK_UPDATE, REQUEST_UPDATE_VERSION_CODE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void success(int requestCode, String data) {
        super.success(requestCode, data);
        closeProgressDialog();
        Entity entity = Parsers.getResult(data);
        if (REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
            parseData(requestCode, data);
//            ToastUtil.shortShow(this, entity.getResultMsg());
        } else if ("999999".equals(entity.getResultCode())) {
            //强退
            UserCenter.cleanLoginInfo(this);
            startActivity(new Intent(this, LoginActivity.class));
        } else if ("10101015".equals(entity.getResultCode())) {
//            ToastUtil.shortShow(this, entity.getResultMsg());
        }else {
            ToastUtil.shortShow(this, entity.getResultMsg());
        }
    }

    /**
     * 请求成功后实际处理数据的方法
     */
    protected void parseData(int requestCode, String data) {
        closeProgressDialog();
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        if (REQUEST_UPDATE_VERSION_CODE == requestCode) {
            closeProgressDialog();
        }
        ToastUtil.shortShow(this, errorMessage);
    }

    @Override
    public void onDestroy() {
        ExitManager.instance.remove(this);
        super.onDestroy();
    }

}
