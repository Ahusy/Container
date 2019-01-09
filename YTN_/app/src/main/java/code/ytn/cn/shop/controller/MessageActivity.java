package code.ytn.cn.shop.controller;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.common.viewinject.annotation.ViewInject;

import java.lang.reflect.Field;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.shop.adapter.MessagePageAdapter;
import code.ytn.cn.shop.view_.NoScrollViewPager;
import code.ytn.cn.utils.ViewInjectUtils;

/**
 * 消息通知
 */
public class MessageActivity extends ToolBarActivity {

    @ViewInject(R.id.message_tab)
    private TabLayout tab;
    @ViewInject(R.id.message_vp)
    private NoScrollViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ViewInjectUtils.inject(this);
        setCenterTitle("消息中心");
        initData();
    }

    // 初始化数据
    private void initData() {
        MessagePageAdapter adapter = new MessagePageAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        // 禁止滑动
        vp.setScanScroll(false);
        tab.setupWithViewPager(vp);
        vp.setOffscreenPageLimit(2);
        vp.setCurrentItem(0);

    }

    @Override
    protected void onStart() {
        super.onStart();
        tab.post(() -> setIndicator(tab, 35, 35));
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

}
