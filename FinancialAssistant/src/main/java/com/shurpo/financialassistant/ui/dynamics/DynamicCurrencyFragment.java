package com.shurpo.financialassistant.ui.dynamics;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract;
import com.shurpo.financialassistant.ui.BaseFragment;
import com.shurpo.financialassistant.ui.adapters.DynamicPageAdapter;
import com.shurpo.financialassistant.ui.currency.CurrencyFragment;
import com.shurpo.financialassistant.utils.CurrencyInfoUtil;
import com.shurpo.financialassistant.utils.DynamicUtil;
import com.shurpo.financialassistant.utils.GraphUtil;
import com.shurpo.financialassistant.utils.WebRequestUtil;

import java.util.List;

/**
 * Created by Maksim_Shurpo on 9/29/2014.
 */
public class DynamicCurrencyFragment extends BaseFragment {

    public static final int DYNAMIC_LOADER = 60;

    private ActionBarManager actionBarManager;
    private ViewPager dynamicPageView;
    private PagerAdapter dynamicPagerAdapter;
    private Spinner spinnerView;
    private DynamicUtil dynamicUtil;

    private OnLoaderCallback onLoaderCallback = new OnLoaderCallback() {
        @Override
        public void onLoadFinished(Loader loader, Object o) {
            switch (loader.getId()){
                case CurrencyFragment.CURRENCY_LOADER:
                    getAdapter().swapCursor((Cursor)o);
                    break;
                case DYNAMIC_LOADER:
                    List<CurrencyInfoUtil> list = dynamicUtil.getCurrency((Cursor) o);
                    GraphUtil graphUtil = new GraphUtil();
                    graphUtil.breakToArrayRatesAndDates(list);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(DynamicPageAdapter.LIST_DYNAMIC_RATES_BUNDLE_KEY, graphUtil.getDynamicsRates());
                    bundle.putStringArrayList(DynamicPageAdapter.LIST_DYNAMIC_DATE_BUNDLE_KEY, graphUtil.getDynamicsDates());
                    if (dynamicPagerAdapter != null) {
                        dynamicPageView.removeAllViews();
                    }
                    dynamicPagerAdapter = new DynamicPageAdapter(getActivity().getSupportFragmentManager(), bundle);
                    dynamicPageView.setAdapter(dynamicPagerAdapter);
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
                    break;
            }
        }
    };

    private ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            dynamicPageView.setCurrentItem(tab.getPosition());
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
            getPreference().saveNumCodeCurrency(cursor.getInt(cursor.getColumnIndex(FinancialAssistantContract.Currency.NUM_CODE)));
            getPreference().saveCharCodeCurrency(cursor.getString(cursor.getColumnIndex(FinancialAssistantContract.Currency.CHAR_CODE)));
            getPreference().saveScaleCurrency(cursor.getInt(cursor.getColumnIndex(FinancialAssistantContract.Currency.SCALE)));
            getPreference().saveNameCurrency(cursor.getString(cursor.getColumnIndex(FinancialAssistantContract.Currency.NAME)));

            refreshData(WebRequestUtil.RequestUri.dynamic);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public DynamicCurrencyFragment() {
    }

    public static DynamicCurrencyFragment newInstance(){
        return new DynamicCurrencyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dynamic_layout, container, false);
        int positionDrawerItem = getArguments().getInt(getString(R.string.arg_form_item_key));
        String title = getResources().getStringArray(R.array.drawer_texts)[positionDrawerItem];
        getActivity().setTitle(title);
        dynamicUtil = new DynamicUtil();
        actionBarManager = new ActionBarManager(getActivity());
        actionBarManager.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnLoaderCallback(onLoaderCallback);
        progressBar = (ProgressBar) view.findViewById(R.id.update_rate);
        progressBar.setVisibility(View.GONE);
        String[] from = new String[]{FinancialAssistantContract.Currency.NAME};
        int[] to = new int[]{android.R.id.text1};
        setAdapter(new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, from, to, 0));
        spinnerView = (Spinner) view.findViewById(R.id.dynamic_spinner);
        spinnerView.setAdapter(getAdapter());
        spinnerView.setOnItemSelectedListener(onItemSelectedListener);

        dynamicPageView = (ViewPager) view.findViewById(R.id.dynamic_pager);


        getActivity().getSupportLoaderManager().initLoader(CurrencyFragment.CURRENCY_LOADER, getBundle(), callbacks);

        actionBarManager.CreateTagsActionBar(tabListener, new String[]{
                getString(R.string.week_dynamic_tab),
                getString(R.string.month_dynamic_tab),
                getString(R.string.half_year_dynamic_tab)
        });
        actionBarManager.getActionBar().setSelectedNavigationItem(dynamicPageView.getCurrentItem());
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(LOADER_BUNDLE_KEY, getPreference().getLastDateCurrency());
        return bundle;
    }

    @Override
    protected void updateData() {
        progressBar.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putString(LOADER_BUNDLE_KEY, getPreference().getCurrencyId());
        getActivity().getSupportLoaderManager().restartLoader(DYNAMIC_LOADER, bundle, callbacks);
    }

    @Override
    protected SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        };
    }
}
