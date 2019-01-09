package code.ytn.cn.utils;

import android.hardware.Camera;

/**
 * Created by dell on 2018/6/15
 */

public class CameraCanUseUtils {

    /**
     * 测试当前摄像头能否被使用
     *
     * @return
     */
    public static boolean isCameraCanUse() {
//        boolean canUse = true;
//        Camera mCamera = null;
//        try {
//            mCamera = Camera.open(0);
//            mCamera.setDisplayOrientation(90);
//        } catch (Exception e) {
//            canUse = false;
//        }
//        if (canUse) {
//            mCamera.release();
//            mCamera = null;
//        }
//        //Timber.v("isCameraCanuse="+canUse);
//        return canUse;
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }
}
