package com.shurpo.financialassistant.ui.dynamics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.graphview.CustomLabelFormatter;
import com.shurpo.financialassistant.graphview.GraphView;
import com.shurpo.financialassistant.graphview.GraphViewSeries;
import com.shurpo.financialassistant.graphview.LineGraphView;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract.*;
import com.shurpo.financialassistant.ui.BaseFragment;
import com.shurpo.financialassistant.utils.CurrencyInfoUtil;
import com.shurpo.financialassistant.utils.DynamicUtil;
import com.shurpo.financialassistant.utils.GraphUtil;
import com.shurpo.financialassistant.utils.WebRequestUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DynamicsCurrencyFragment extends BaseFragment {

    private class NotifyGraphReceiver extends BroadcastReceiver {

        public static final String CURRENCY_RECEIVER = "com.shurpo.financialassistant.ui.dynamics";
        public static final String EXTRA_NOTIFY_GRAPH = "EXTRA_NOTIFY_GRAPH";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(EXTRA_NOTIFY_GRAPH, false)) {
                //положить данные в графику.


            }
        }
    }

    private static final String CURRENCY_ID_KEY = "CURRENCY_ID_KEY";

    private DynamicUtil dynamicUtil;
    private NotifyGraphReceiver receiver;

    private Spinner spinnerView;
    private ActionBar actionBar;
    private LinearLayout layout;
    private GraphViewSeries series;
    private GraphView graphView;
    private boolean isSendReceiver = false;


    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = adapter.getCursor();
            cursor.moveToPosition(position);
            preference.saveCurrencyId(cursor.getString(cursor.getColumnIndex(Currency.CURRENCY_ID)));

            refreshData(WebRequestUtil.DYNAMIC_PROCESSOR);
            graphView.removeAllSeries();
            isSendReceiver = false;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            Toast.makeText(getActivity(), "tab position " + tab.getPosition(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> dynamicCallBack = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader onCreateLoader(int id, Bundle bundle) {
            return new CursorLoader(getActivity(), CurrencyInfo.CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader loader, Cursor cursor) {
            dynamicUtil.getCurrencyInfo(cursor);
            drawGraph();
        }

        @Override
        public void onLoaderReset(Loader loader) {
        }
    };

    private void drawGraph(){
        layout.removeAllViews();
        GraphUtil graphUtil = new GraphUtil();
        graphUtil.initGraphViewData(GraphUtil.PERIOD_MONTH, dynamicUtil);
        series = new GraphViewSeries(graphUtil.getData(GraphUtil.PERIOD_MONTH));
        graphView.addSeries(series);
        graphView.setHorizontalLabels(graphUtil.getLabels(GraphUtil.PERIOD_MONTH));
        graphView.getGraphViewStyle().setTextSize(10);
        layout.addView(graphView);
    }

    public static DynamicsCurrencyFragment newInstance() {
        return new DynamicsCurrencyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dynamic_layout, container, false);
        int positionDrawerItem = getArguments().getInt(getString(R.string.arg_form_item_key));
        String title = getResources().getStringArray(R.array.drawer_texts)[positionDrawerItem];
        getActivity().setTitle(title);
        dynamicUtil = new DynamicUtil();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = (LinearLayout) view.findViewById(R.id.dynamic_graph_layout);
        graphView = new LineGraphView(getActivity(), "Валюта");

        String[] from = new String[]{Currency.NAME};
        int[] to = new int[]{android.R.id.text1};
        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, from, to, 0);
        spinnerView = (Spinner) view.findViewById(R.id.dynamic_spinner);
        spinnerView.setAdapter(adapter);
        spinnerView.setOnItemSelectedListener(onItemSelectedListener);
        getActivity().getSupportLoaderManager().initLoader(10, getBundle(), callbacks);

        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText(getActivity().getString(R.string.week_dynamic_tab)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(getActivity().getString(R.string.month_dynamic_tab)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(getActivity().getString(R.string.half_year_dynamic_tab)).setTabListener(tabListener));
    }

    @Override
    public void onStart() {
        super.onStart();
        receiver = new NotifyGraphReceiver();
        IntentFilter filter = new IntentFilter(NotifyGraphReceiver.CURRENCY_RECEIVER);
        getActivity().registerReceiver(receiver, filter);

    }

    @Override
    public void onPause() {
        super.onPause();
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refreshData(WebRequestUtil.DYNAMIC_PROCESSOR);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void updateData() {
        stopProgressActionBar();
        getActivity().getSupportLoaderManager().initLoader(0, null, dynamicCallBack);
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY, preference.getDateCurrency());
        return bundle;
    }
}
