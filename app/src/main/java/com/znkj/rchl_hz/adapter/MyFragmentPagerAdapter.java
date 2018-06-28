package com.znkj.rchl_hz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.znkj.rchl_hz.fragment.JqFragment;
import com.znkj.rchl_hz.fragment.MhFragment;

/**
 * Created by Carson_Ho on 16/7/22.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{ "精确核查", "模糊核查"};

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //Intent intent= new Intent();

    @Override
    public Fragment getItem(int position) {

        /*Bundle bundle = new Bundle();
        bundle.putString("msg", "ssdsds");
        bundle.putString("xm", "lll");
        bundle.putString("sjh", "222222");
        bundle.putString("sfzh", "123456");*/
        Fragment fragment;
        fragment = new JqFragment();
        //fragment.setArguments(bundle);
        if (position == 1) {
            return new MhFragment();
        }
        return fragment;
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
