package code.ytn.cn.shop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2018/7/5
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private List<String> list_Title;

    public void setFragments(ArrayList<Fragment> fragments, List<String> list_Title) {
        mFragmentList = fragments;
        this.list_Title = list_Title;
    }

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = mFragmentList.get(position);

        return fragment;
    }

    @Override
    public int getCount() {

        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position);
    }
}
