package com.bytesnmaterials.zro.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bytesnmaterials.zro.fragments.FragmentChats;
import com.bytesnmaterials.zro.fragments.FragmentUsers;
import com.bytesnmaterials.zro.fragments.IUpdatableFragement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitesh on 9/8/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
