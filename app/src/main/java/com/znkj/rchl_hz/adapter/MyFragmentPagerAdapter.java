package com.znkj.rchl_hz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.znkj.rchl_hz.R;
import com.znkj.rchl_hz.fragment.JqFragment;
import com.znkj.rchl_hz.fragment.MhFragment;
import com.znkj.rchl_hz.model.fragmentTag;

import java.util.ArrayList;
import java.util.List;


public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{ "精确核查", "模糊核查"};

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //Intent intent= new Intent();

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;
        fragment = new JqFragment();
        if (position == 1) {
            fragment = new MhFragment();
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

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        //得到缓存的fragment
//        Fragment fragment = (Fragment) super.instantiateItem(container,
//                position);
//        String fragmentTagName = fragment.getTag();
//        fragmentTag tagList =  fragmentTag.getFtTag();
//        Log.e("走了这里11","============================");
//        if(position==1){
//            Log.e("走了这里22","============================");
//            List lll = new ArrayList();
//            lll.add(fragmentTagName);
//            tagList.setFragmentTag(lll);
//        }
//
//        return fragment;
//    }
}
