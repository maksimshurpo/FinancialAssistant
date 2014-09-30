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
    public static final String LIST_DYNAMIC_RATES_BUNDLE_KEY = "LIST_DYNAMIC_RATES_BUNDLE_KEY";
    public static final String LIST_DYNAMIC_DATE_BUNDLE_KEY = "LIST_DYNAMIC_DATE_BUNDLE_KEY";

    private static final int COUNT_PAGE = 3;

    private Bundle bundle;

    public DynamicPageAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = DynamicFragment.newInstance();
        bundle.putInt(POSITION_PAGE_KEY, i);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return COUNT_PAGE;
    }
}
