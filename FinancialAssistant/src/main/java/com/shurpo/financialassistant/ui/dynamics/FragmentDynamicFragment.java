package com.shurpo.financialassistant.ui.dynamics;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract;
import com.shurpo.financialassistant.ui.BaseFragment;
import com.shurpo.financialassistant.ui.adapters.DynamicPageAdapter;

/**
 * Created by Maksim_Shurpo on 9/29/2014.
 */
public class FragmentDynamicFragment extends BaseFragment {

    private ActionBarManager actionBarManager;
    private ViewPager dynamicPageView;
    private PagerAdapter dynamicPagerAdapter;
    private Spinner spinnerView;
    private LinearLayout graphLayout;

    private ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            //Toast.makeText(FinancialAssistantActivity.this, "tab position " + tab.getPosition(), Toast.LENGTH_LONG).show();
            //dynamicPageView.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }
    };

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = getAdapter().getCursor();
            cursor.moveToPosition(position);
            getPreference().saveCurrencyId(cursor.getString(cursor.getColumnIndex(FinancialAssistantContract.Currency.CURRENCY_ID)));

            //refreshData(WebRequestUtil.RequestUri.dynamic);
            // graphView.removeAllSeries();
            //isSendReceiver = false;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public FragmentDynamicFragment() {
    }

    public static FragmentDynamicFragment newInstance(){
        return new FragmentDynamicFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBarManager = new ActionBarManager(getActivity());
        actionBarManager.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBarManager.CreateTagsActionBar(tabListener, new String[]{
                getString(R.string.week_dynamic_tab),
                getString(R.string.month_dynamic_tab),
                getString(R.string.half_year_dynamic_tab)
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dynamic_layout, container, false);
        int positionDrawerItem = getArguments().getInt(getString(R.string.arg_form_item_key));
        String title = getResources().getStringArray(R.array.drawer_texts)[positionDrawerItem];
        getActivity().setTitle(title);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        graphLayout = (LinearLayout) view.findViewById(R.id.dynamic_graph_layout);
        String[] from = new String[]{FinancialAssistantContract.Currency.NAME};
        int[] to = new int[]{android.R.id.text1};
        setAdapter(new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, from, to, 0));
        spinnerView = (Spinner) view.findViewById(R.id.dynamic_spinner);
        spinnerView.setAdapter(getAdapter());
        spinnerView.setOnItemSelectedListener(onItemSelectedListener);

        dynamicPageView = (ViewPager) view.findViewById(R.id.dynamic_pager);

        dynamicPagerAdapter = new DynamicPageAdapter(getActivity().getSupportFragmentManager());
        dynamicPageView.setAdapter(dynamicPagerAdapter);
        actionBarManager.getActionBar().setSelectedNavigationItem(dynamicPageView.getCurrentItem());
        dynamicPageView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                actionBarManager.getActionBar().setSelectedNavigationItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void updateData() {

    }
}
