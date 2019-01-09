package code.ytn.cn.shop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import code.ytn.cn.shop.fragment.MessageAllFragment;
import code.ytn.cn.shop.fragment.MessageReadFragment;
import code.ytn.cn.shop.fragment.MessageUnreadFragment;

/**
 * Created by dell on 2018/8/17
 */

public class MessagePageAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"全部", "已读", "未读"};

    public MessagePageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new MessageReadFragment();
        } else if (position == 2) {
            return new MessageUnreadFragment();
        }
        return new MessageAllFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
