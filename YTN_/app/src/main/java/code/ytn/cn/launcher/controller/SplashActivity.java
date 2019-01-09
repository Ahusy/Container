package code.ytn.cn.launcher.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.Timer;
import java.util.TimerTask;

import code.ytn.cn.R;
import code.ytn.cn.base.BaseActivity;
import code.ytn.cn.home.controller.CounterListActivity;
import code.ytn.cn.login.controller.LoginActivity;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.shop.controller.ShopManagerHomeListActivity;
import code.ytn.cn.utils.SafeSharePreferenceUtil;

import static code.ytn.cn.common.CommonConstant.LATI_TUDE;
import static code.ytn.cn.common.CommonConstant.LONG_ITUDE;


/**
 * 空白启动页，判断进入登录页还是主页
 */
public class SplashActivity extends BaseActivity{
    private static final long LAUNCH_MIN_TIME = 2000L;
    private static final int MSG_CITY_INIT_FINISH = 1;
    private long mLaunchTime;
    // 定位相关
    private LocationClient locationClient = null;

    private static int LOCATION_COUTNS = 0;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CITY_INIT_FINISH:
                    gotoActivity();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        SafeSharePreferenceUtil.saveInt("statusBarHeight", getStatusHeight(this));
        getLocation();
        mLaunchTime = SystemClock.elapsedRealtime();
        initCityDB();
    }

    private void getLocation() {
        locationClient = new LocationClient(this);
        //设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);								//是否打开GPS
        option.setCoorType("bd09ll");							//设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst);	//设置定位优先级
//        option.setProdName("LocationDemo");						//设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
//        option.setScanSpan(UPDATE_TIME);						//设置定时定位的时间间隔。单位毫秒
        locationClient.setLocOption(option);

        //注册位置监听器
        locationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                // TODO Auto-generated method stub
                if (location == null) {
                    return;
                }
                StringBuffer sb = new StringBuffer(256);
                sb.append("Time : ");
                sb.append(location.getTime());
                sb.append("\nError code : ");
                sb.append(location.getLocType());
                sb.append("\nLatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nLontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nRadius : ");
                sb.append(location.getRadius());
                if (location.getLocType() == BDLocation.TypeGpsLocation){
                    sb.append("\nSpeed : ");
                    sb.append(location.getSpeed());
                    sb.append("\nSatellite : ");
                    sb.append(location.getSatelliteNumber());
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                    sb.append("\nAddress : ");
                    sb.append(location.getAddrStr());
                }
                LOCATION_COUTNS ++;
                sb.append("\n检查位置更新次数：");
                sb.append(String.valueOf(LOCATION_COUTNS));
                Log.i("============", "onReceiveLocation: "+sb.toString());

                SafeSharePreferenceUtil.saveString(LONG_ITUDE, String.valueOf(location.getLongitude()));
                SafeSharePreferenceUtil.saveString(LATI_TUDE, String.valueOf(location.getLatitude()));

            }

        });

        locationClient.start();
        locationClient.requestLocation();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
            locationClient = null;
        }
        }


    @SuppressLint("PrivateApi")
    public static int getStatusHeight(Activity activity){
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    private void initCityDB() {
        new Thread(() -> {
//            CityDBManager.introducedCityDB(this);
            handler.sendEmptyMessageDelayed(MSG_CITY_INIT_FINISH, 100);
        }).start();
    }

    private void gotoActivity() {
        long elapsed = SystemClock.elapsedRealtime() - mLaunchTime;
        if (elapsed >= LAUNCH_MIN_TIME) {
            performGotoActivity();
            finish();
        } else {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (SplashActivity.this.isFinishing()) {
                        return;
                    }
                    cancel();
                    performGotoActivity();
                    finish();
                }
            }, LAUNCH_MIN_TIME - elapsed);
        }

    }

    private void performGotoActivity() {
        if (UserCenter.isLogin(this)) {
            if(UserCenter.getRank(this).equals("4") || UserCenter.getRank(this).equals("1")){
                startActivity(new Intent(this, ShopManagerHomeListActivity.class));
            }else{
                startActivity(new Intent(this, CounterListActivity.class));
            }
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


}
