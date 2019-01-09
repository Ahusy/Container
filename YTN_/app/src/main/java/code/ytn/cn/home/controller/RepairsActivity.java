package code.ytn.cn.home.controller;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.common.network.FProtocol;
import com.common.utils.BitmapUtil;
import com.common.utils.DeviceUtil;
import com.common.utils.FileUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.MySpinner;
import code.ytn.cn.utils.PermissionUtils;
import code.ytn.cn.utils.ToggleButton;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.CABINET_ID;
import static code.ytn.cn.common.CommonConstant.CABINET_NAME;
import static code.ytn.cn.common.CommonConstant.REQUEST_ACT_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_CAMERA_PERMISSION_CODE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_SUCCESS;
import static code.ytn.cn.common.CommonConstant.STATUS;
import static code.ytn.cn.common.CommonConstant.STATUS_ONE;
import static code.ytn.cn.common.CommonConstant.STATUS_THREE;
import static com.common.utils.BitmapUtil.IMAGE_CACHE_DIR;

/**
 * 设备巡检
 */
public class RepairsActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.polling_name)
    private TextView pollName;
    @ViewInject(R.id.polling_et)
    private EditText pollEt;
    @ViewInject(R.id.polling_photo)
    private SimpleDraweeView pollPhoto;
    @ViewInject(R.id.polling_comit)
    private TextView pollComit;
    @ViewInject(R.id.toggbtn)
    private ToggleButton togg;
    @ViewInject(R.id.spinner)
    private MySpinner mSpinner;
    @ViewInject(R.id.polling_cause_ll)
    private LinearLayout ll;
    private Uri cameraUri;
    private String path;
    private String avatarPic;

    private List<String> dataList;
    private String status;
    private String id;
    private String name;

    private LocationClient mLocationClient = null;
    private BDLocationListener mBDLocationListener;
    private double latitude;
    private double longitude;
    private String address;
    private MyAdapter<String> adapter;

    public static void startRepairsActivity(Context context,String cabinetId,String cabinetName,String status){
        Intent intent = new Intent(context,RepairsActivity.class);
        intent.putExtra(CABINET_ID,cabinetId);
        intent.putExtra(CABINET_NAME,cabinetName);
        intent.putExtra(STATUS,status);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairs);
        ViewInjectUtils.inject(this);
        setCenterTitle(getString(R.string.title_repairs));
        initClick();
//        getLocation();
        initData();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        imagePipeline.clearDiskCaches();

        // combines above two lines
        imagePipeline.clearCaches();
        togg.setOnToggleChanged(on -> {
            if (on) {
                status = STATUS_THREE;
            } else {
                status = STATUS_ONE;
            }
        });
    }


    private void initData() {
        dataList = new ArrayList<>();
        dataList.add(getString(R.string.list_text_plase_choose));
        dataList.add(getString(R.string.list_text_cabinet_damage));
        dataList.add(getString(R.string.list_text_counter_blo_damage));
        dataList.add(getString(R.string.list_text_counter_damage));
        dataList.add(getString(R.string.list_text_other));
        adapter = new MyAdapter<>(this, android.R.layout.simple_spinner_item,
                dataList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

    private void initClick() {
        pollPhoto.setOnClickListener(this);
        ll.setOnClickListener(this);
        pollComit.setOnClickListener(this);
        id = getIntent().getStringExtra(CABINET_ID);
        name = getIntent().getStringExtra(CABINET_NAME);
        status = getIntent().getStringExtra(STATUS);
        pollName.setText(name);
        if (status != null && status.equals(STATUS_ONE)) {
            togg.setToggleOff();
        } else if (status != null && status.equals(STATUS_THREE)) {
            togg.setToggleOn();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.polling_photo:
                if (PermissionUtils.isGetPermission(RepairsActivity.this, Manifest.permission.CAMERA)) {
                    openCamera();
                } else {
                    PermissionUtils.secondRequest(RepairsActivity.this, REQUEST_CAMERA_PERMISSION_CODE, Manifest.permission.CAMERA);
                }
                break;
            case R.id.polling_comit:
                // 获取CustomSpinner选中的哪一项，-1表示没有选中
                int nSelect = mSpinner.getSelectedItemPosition();
                if (nSelect == -1) {
                    ToastUtil.showToast(this, getString(R.string.toast_chose_repairs_cause));
                    return;
                }
                if (pollEt.getText().toString().trim().equals("")) {
                    ToastUtil.showToast(this, getString(R.string.text_counter_status));
                    return;
                }
                if (togg.isToggleOn()) {
                    DialogUtils.showTwoBtnDialog(this, getString(R.string.hint), getString(R.string.dialog_block_counter), v1 -> {
                        commitPoll();
                        DialogUtils.closeDialog();
                    }, v2 -> DialogUtils.closeDialog());
                } else {
                    DialogUtils.showTwoBtnDialog(this, getString(R.string.hint), getString(R.string.dialog_deblocking), v1 -> {
                        commitPoll();
                        DialogUtils.closeDialog();
                    }, v2 -> DialogUtils.closeDialog());
                }
                break;
        }
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        Entity entity = Parsers.getResult(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (entity.getResultCode().equals(REQUEST_NET_SUCCESS)) {
                    ToastUtil.showToast(this, entity.getResultMsg());
                    finish();
                } else {
                    ToastUtil.showToast(this, entity.getResultMsg());
                }
                break;
        }
    }

    private void commitPoll() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("cabinet_id", id);
        params.put("cabinet_name", name);
        params.put("status", status);
        params.put("img", avatarPic);
        params.put("info", pollEt.getText().toString());
        String cause = "";
        switch (mSpinner.getSelectedItemPosition()) {
            case 0:
                cause = getString(R.string.list_text_cabinet_damage);
                break;
            case 1:
                cause = getString(R.string.list_text_counter_blo_damage);
                break;
            case 2:
                cause = getString(R.string.list_text_counter_damage);
                break;
            case 3:
                cause = getString(R.string.list_text_other);
                break;
        }
        params.put("reason", cause);
        params.put("longitude", String.valueOf(longitude));
        params.put("latitude", String.valueOf(latitude));
        params.put("address", address);
        params.put("worker_name", UserCenter.getName(this));
        params.put("worker_id", UserCenter.getUserId(this));
        requestHttpData(Constants.Urls.URL_POST_REPAIR, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }


    private void getLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        mBDLocationListener = new MyBDLocationListener();
        // 注册监听
        mLocationClient.registerLocationListener(mBDLocationListener);
        // 声明定位参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式 高精度
        option.setCoorType("bd09ll");// 设置返回定位结果是百度经纬度 默认gcj02
//        option.setScanSpan(5000);// 设置发起定位请求的时间间隔 单位ms
        option.setIsNeedAddress(true);// 设置定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向
        // 设置定位参数
        mLocationClient.setLocOption(option);
        // 启动定位
        mLocationClient.start();
    }

    private class MyBDLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // 非空判断
            if (location != null) {
                // 根据BDLocation 对象获得经纬度以及详细地址信息
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                address = location.getAddrStr();
                if (mLocationClient.isStarted()) {
                    // 获得位置之后停止定位
                    mLocationClient.stop();
                }
            } else {
                ToastUtil.showToast(RepairsActivity.this, "请先获取地址信息");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        // 取消监听函数
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mBDLocationListener);
        }
    }

    private void openCamera() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = FileUtil.getDiskCacheFile(RepairsActivity.this, IMAGE_CACHE_DIR);
        file = new File(file.getPath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");
        path = file.getPath();
        if (Build.VERSION.SDK_INT < 24) {
            cameraUri = Uri.fromFile(file);
        } else {
            //android 7.0
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, path);
            cameraUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(openCameraIntent, REQUEST_ACT_ONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && Manifest.permission.CAMERA.equals(permissions[0]) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    ToastUtil.shortShow(this, getString(R.string.personinfo_permission_photo_failed));
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_ACT_ONE://相机
                    DraweeController controller1 = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(ImageRequestBuilder.newBuilderWithSource(cameraUri)
                            .setResizeOptions(new ResizeOptions(DeviceUtil.getWidth(this), DeviceUtil.getHeight(this)))
                            .build())
                            .build();
                    pollPhoto.setController(controller1);
                    avatarPic = BitmapUtil.bitmapToString(path, 60, 60);
                    break;
            }
        }

    }

    private class MyAdapter<T> extends ArrayAdapter<T> {
        private LayoutInflater mInflater;
        private ViewHolder holder;
        private View rootView = null;
        private SpinnerHandler handler;

        public MyAdapter(Context context, int textViewResourceId, List<T> objects) {
            super(context, textViewResourceId, objects);

            handler = new SpinnerHandler();
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // 在下拉列表中不要显示“请选择:”这一项，所以要将总数减1
            return dataList.size() - 1;
        }

        // 获取下拉列表的view
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

            if (null == convertView) {
                // 将下拉列表的每个item用spinner_item.xml布局
                convertView = mInflater.inflate(R.layout.textview, null);
                holder = new ViewHolder();
                holder.txt = (TextView) convertView.findViewById(R.id.spinner_txt);
//                holder.radio = (RadioButton)convertView.findViewById(R.id.spinner_radio);
                // 给item添加点击事件，，dismiss下拉列表
                convertView.setOnClickListener(v -> {

                    ViewHolder vh = (ViewHolder) v.getTag();
                    // 将RadioButton选中
//                    vh.radio.setChecked(true);
                    // 设置Spinner第几项被选中了
                    mSpinner.setSelection(vh.index);
                    adapter.notifyDataSetChanged();
                    // 获取下拉列表的主窗口句柄
                    rootView = v.getRootView();
                    // 延迟100毫秒dismiss下拉列表
                    handler.sendEmptyMessageDelayed(1, 100);
                });

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 下拉列表中要去掉“请选择：”项，所以要“position + 1”
            holder.txt.setText(dataList.get(position + 1));
            holder.index = position;

            return convertView;
        }

        // 获取控件view
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int nPos = 0;

            int nIndex = mSpinner.getSelectedItemPosition();
            // 如果没有选中，返回“请选择：”，否则返回选中项
            if (nIndex == -1) {
                nPos = 0;
            } else {
                nPos = position + 1;
            }

            return super.getView(nPos, convertView, parent);
        }

        private class ViewHolder {
            public int index;
            public TextView txt;
        }

        private class SpinnerHandler extends Handler {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case 1:
                        if (null != rootView) {
                            rootView.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
}