package code.ytn.cn.shop.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.network.entity.DataEntity;
import code.ytn.cn.shop.adapter.MyPagerAdapter;
import code.ytn.cn.shop.fragment.TodayRankFragment;
import code.ytn.cn.shop.fragment.WeekRankFragment;
import code.ytn.cn.shop.view.PickTime.DatePickDialog;
import code.ytn.cn.shop.view.PickTime.bean.DateType;
import code.ytn.cn.utils.DataUtils;
import code.ytn.cn.utils.DialogUtils;
import code.ytn.cn.utils.ViewInjectUtils;

/**
 * 商品销售排行
 */
public class SalesRankActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.sales_rank_tab)
    private TabLayout tableLayout;
    @ViewInject(R.id.sales_rank_vp)
    private ViewPager vp;
    private int current;
    private List<DataEntity> list;
    private int weeks;
    private int year;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_rank);
        ViewInjectUtils.inject(this);
        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
        cal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
        cal.setTime(new Date());
        weeks = cal.get(Calendar.WEEK_OF_YEAR); // 获取当前周
        year = cal.get(Calendar.YEAR); // 获取年
        DataEntity dataWeek = new DataEntity();
        dataWeek.setWeek("请选择周");
        list.add(dataWeek);
        for (int i = 0; i < 16; i++) {
            int data = weeks - i;
            String start = DataUtils.getStartDayOfWeekNo(year, data);
            String end = DataUtils.getEndDayOfWeekNo(year, data);
            DataEntity dataEntity = new DataEntity();
            dataEntity.setWeek(String.valueOf(data));
            dataEntity.setStart(start);
            dataEntity.setEnd(end);
            list.add(dataEntity);
        }
        mBtnTitleRightSerach.setVisibility(View.VISIBLE);
        mBtnTitleRightSerach.setOnClickListener(this);
        setCenterTitle("商品售卖排行");
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new TodayRankFragment());
        fragments.add(new WeekRankFragment());
        listTitle.add("日排行");
        listTitle.add("周排行");
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pagerAdapter.setFragments(fragments, listTitle);
        vp.setAdapter(pagerAdapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tableLayout.setupWithViewPager(vp);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tableLayout.post(() -> setIndicator(tableLayout, 50, 50));
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_button_serach:
                if (current == 0) {
                    // 日排行
                    DialogUtils.closeDialog();
                    DialogUtils.showOrderMissDialog(this, "商品售卖查询", (v1, begin) -> {
                        DatePickDialog dialog = new DatePickDialog(this);
                        //设置上下年分限制
                        dialog.setYearLimt(0);
                        //设置标题
                        dialog.setTitle("选择时间");
                        //设置类型
                        dialog.setType(DateType.TYPE_YMD);
                        //设置消息体的显示格式，日期格式
                        dialog.setMessageFormat("yyyy-MM-dd");
                        //设置选择回调
                        dialog.setOnChangeLisener(null);
                        //设置点击确定按钮回调
                        dialog.setOnSureLisener(date -> {
                            startTime = date.getTime();
                            long millis = System.currentTimeMillis();
                            if (millis < startTime) {
                                ToastUtil.shortShow(SalesRankActivity.this, "日期不能大于今天");
                                return;
                            }
                            begin.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                        });
                        dialog.show();
                    }, (v1, over) -> {
                        DatePickDialog dialog = new DatePickDialog(this);
                        //设置上下年分限制
                        dialog.setYearLimt(0);
                        //设置标题
                        dialog.setTitle("选择时间");
                        //设置类型
                        dialog.setType(DateType.TYPE_YMD);
                        //设置消息体的显示格式，日期格式
                        dialog.setMessageFormat("yyyy-MM-dd");
                        //设置选择回调
                        dialog.setOnChangeLisener(null);
                        //设置点击确定按钮回调
                        dialog.setOnSureLisener(date -> {
                            long millis = System.currentTimeMillis();
                            if (millis < date.getTime()) {
                                ToastUtil.shortShow(SalesRankActivity.this, "日期不能大于今天");
                                return;
                            }
                            if (startTime > date.getTime()) {
                                ToastUtil.shortShow(SalesRankActivity.this, "开始日期不能大于结束日期");
                                return;
                            }
                            over.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                        });
                        dialog.show();
                    }, (v1, begin, over) -> {
                        String timeBegin = begin.getText().toString().trim();
                        String timeOver = over.getText().toString().trim();
                        if (timeBegin.equals("")) {
                            ToastUtil.shortShow(this, "请选择开始日期");
                            return;
                        }
                        Intent intent = new Intent("days");
                        intent.putExtra("change", "day");
                        intent.putExtra("begin", timeBegin);
                        intent.putExtra("over", timeOver);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                        DialogUtils.closeDialog();
                    }, v1 -> DialogUtils.closeDialog());
                } else {
                    // 周排行
                    DialogUtils.showWeekDialog(this, String.valueOf(weeks), list, (v1, week, start, end) -> {
                        if (week.equals("请选择周")) {
                            ToastUtil.shortShow(this, "请选择要查询的周期");
                            return;
                        }
                        String startReplace = start.replace(".", "-");
                        String startTime = year + "-" + startReplace;
                        String endReplace = end.replace(".", "-");
                        String endTime = year + "-" + endReplace;
                        Intent intent = new Intent("weeks");
                        intent.putExtra("change", "week");
                        intent.putExtra("start", startTime);
                        intent.putExtra("end", endTime);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                        DialogUtils.closeDialog();
                    }, v1 -> DialogUtils.closeDialog());
                }
                break;
        }
    }
}
