package com.shurpo.financialassistant.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.ui.dynamics.DynamicFragment;

/**
 * Created by Maksim_Shurpo on 9/29/2014.
 */
public class DynamicPageAdapter extends FragmentStatePagerAdapter{

    public static final String POSITION_PAGE_KEY = "POSITION_PAGE_KEY";

    private static final int COUNT_PAGE = 3;

    public DynamicPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = DynamicFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_PAGE_KEY, i);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return COUNT_PAGE;
    }
}
