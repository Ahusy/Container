package code.ytn.cn.login.utils;

import android.content.Context;

import com.common.utils.PreferencesUtils;

import code.ytn.cn.network.entity.ChooseEntity;

/**
 * Created by dell on 2018/3/27
 */

public class ChooseRadio {

    private static final String TYPE = "type";


    public static void savaType(Context context, ChooseEntity chooseEntity) {
        PreferencesUtils.putString(context, TYPE, chooseEntity.getType());

    }

    public static void cleanType(Context context) {
        PreferencesUtils.removeSharedPreferenceByKey(context, TYPE);

    }

    public static String getType(Context context) {
        return PreferencesUtils.getString(context, TYPE);
    }

    public static void setType(Context context, String type) {
        PreferencesUtils.putString(context, TYPE, type);
    }
}
