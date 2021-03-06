package code.ytn.cn.launcher.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.common.utils.LogUtil;
import com.common.utils.NetWorkUtil;
import com.common.utils.VersionInfoUtils;
import com.facebook.stetho.Stetho;

import cn.jpush.android.api.JPushInterface;
import code.ytn.cn.login.utils.UserCenter;

/**
 * 初始化操作
 */
public class InitializeService extends IntentService {

	/**
	 * Creates an IntentService.  Invoked by your subclass's constructor.
	 * name Used to name the worker thread, important only for debugging.
	 */
	public InitializeService() {
		super("InitializeService");
	}

	public static void start(Context context) {
		context.startService(new Intent(context, InitializeService.class));
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			performInit();
		}
	}

	private void performInit() {
		try {
//			SDKInitializer.initialize(getApplicationContext());
//			QbSdk.initX5Environment(this, null);
/*			ShareSDK.initSDK(this);
//			MobclickAgent.setDebugMode(true);
			AnalysisUtil.init(getApplicationContext());*/
			String userId = UserCenter.getUserId(getApplicationContext());
			String chantId = UserCenter.getChantId(getApplicationContext());
			JPushInterface.setDebugMode(true);
			JPushInterface.init(getApplicationContext());
//			JPushInterface.setAlias(getApplicationContext(), DeviceUtil.getDeviceId(getApplicationContext()), null);
			JPushInterface.setAlias(getApplicationContext(),userId,null);
      		SDKInitializer.initialize(getApplicationContext());

			NetWorkUtil.syncConnectState(getApplicationContext());

			LogUtil.isDebug = !VersionInfoUtils.isReleaseVersion(getApplicationContext());
			if (LogUtil.isDebug) {
				Stetho.initialize(Stetho.newInitializerBuilder(getApplicationContext())
						.enableDumpapp(Stetho.defaultDumperPluginsProvider(getApplicationContext()))
						.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(getApplicationContext())).build());
			}
		} catch (Exception e) {
			//避免第三方应用引起应用崩溃
			e.printStackTrace();
		}
	}
}
