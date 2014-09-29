package com.shurpo.financialassistant.ui.dynamics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.graphview.GraphView;
import com.shurpo.financialassistant.graphview.GraphViewSeries;
import com.shurpo.financialassistant.graphview.LineGraphView;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract;
import com.shurpo.financialassistant.ui.BaseFragment;
import com.shurpo.financialassistant.ui.adapters.DynamicPageAdapter;
import com.shurpo.financialassistant.utils.DynamicUtil;
import com.shurpo.financialassistant.utils.GraphUtil;

/**
 * Created by Maksim_Shurpo on 9/29/2014.
 */
public class DynamicFragment extends BaseFragment{

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

    public static final int HALF_YEAR_POSITION = 0;
    public static final int MONTH_POSITION = 1;
    public static final int WEEK_POSITION = 2;

    private Spinner spinnerView;
    private LinearLayout graphLayout;
    private GraphView graphView;
    private GraphViewSeries series;
    private DynamicUtil dynamicUtil;
    private NotifyGraphReceiver receiver;
    private int positionPage;

    private LoaderManager.LoaderCallbacks<Cursor> dynamicCallBack = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader onCreateLoader(int id, Bundle bundle) {
            return new CursorLoader(getActivity(), FinancialAssistantContract.Currency.CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader loader, Cursor cursor) {
            getAdapter().swapCursor(cursor);
            getAdapter().notifyDataSetChanged();
            //dynamicUtil.getCurrency(cursor);
           // drawGraph();
        }

        @Override
        public void onLoaderReset(Loader loader) {
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

    public DynamicFragment() {
    }

    public static DynamicFragment newInstance(){
        return new DynamicFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dynamic_layout, container, false);
        positionPage = getArguments().getInt(DynamicPageAdapter.POSITION_PAGE_KEY);
        dynamicUtil = new DynamicUtil();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        graphLayout = (LinearLayout) view.findViewById(R.id.dynamic_graph_layout);
        graphView = new LineGraphView(getActivity(), "Валюта");
        String[] from = new String[]{FinancialAssistantContract.Currency.NAME};
        int[] to = new int[]{android.R.id.text1};
        setAdapter(new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, from, to, 0));
        spinnerView = (Spinner) view.findViewById(R.id.dynamic_spinner);
        spinnerView.setAdapter(getAdapter());
        spinnerView.setOnItemSelectedListener(onItemSelectedListener);

        getActivity().getSupportLoaderManager().initLoader(0, null, dynamicCallBack);
    }


    @Override
    public void onStart() {
        super.onStart();
        receiver = new NotifyGraphReceiver();
        IntentFilter filter = new IntentFilter(NotifyGraphReceiver.CURRENCY_RECEIVER);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }

    @Override
    protected void updateData() {
        stopProgressActionBar();
        getActivity().getSupportLoaderManager().restartLoader(0, null, dynamicCallBack);
    }

    private void drawGraph(){
        graphLayout.removeAllViews();
        GraphUtil graphUtil = new GraphUtil();
        graphUtil.initGraphViewData(GraphUtil.PERIOD_MONTH, dynamicUtil);
        series = new GraphViewSeries(graphUtil.getData(GraphUtil.PERIOD_MONTH));
        graphView.addSeries(series);
        graphView.setHorizontalLabels(graphUtil.getLabels(GraphUtil.PERIOD_MONTH));
        graphView.getGraphViewStyle().setTextSize(10);
        graphLayout.addView(graphView);
    }
}
