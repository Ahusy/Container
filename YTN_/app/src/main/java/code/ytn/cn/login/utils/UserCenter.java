package code.ytn.cn.login.utils;

import android.content.Context;

import com.common.utils.PreferencesUtils;
import com.common.utils.StringUtil;

import code.ytn.cn.network.entity.UserEntity;


/**
 * 用户信息
 */
public class UserCenter {

    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String CHANT_ID = "chant_id";
    private static final String RANK = "rank";

    public static void saveUserInfo(Context context, UserEntity userEntity) {
        PreferencesUtils.putString(context, USER_ID, userEntity.getUserId());
        PreferencesUtils.putString(context, USER_NAME, userEntity.getUserName());
        PreferencesUtils.putString(context, CHANT_ID, userEntity.getChantId());
        PreferencesUtils.putString(context, RANK, userEntity.getRank());
    }

    public static void cleanLoginInfo(Context context) {
        PreferencesUtils.removeSharedPreferenceByKey(context, USER_ID);
        PreferencesUtils.removeSharedPreferenceByKey(context, USER_NAME);
        PreferencesUtils.removeSharedPreferenceByKey(context, CHANT_ID);
        PreferencesUtils.removeSharedPreferenceByKey(context, RANK);
    }

    public static String getUserId(Context context) {
        return PreferencesUtils.getString(context, USER_ID);
    }

    public static String getName(Context context) {
        return PreferencesUtils.getString(context, USER_NAME);
    }

    public static String getChantId(Context context) {
        return PreferencesUtils.getString(context, CHANT_ID);
    }

    public static String getRank(Context context) {
        return PreferencesUtils.getString(context, RANK);
    }


    public static boolean isLogin(Context context) {
        String token = PreferencesUtils.getString(context, USER_ID);
        return !StringUtil.isEmpty(token);
    }

}
