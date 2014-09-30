package com.shurpo.financialassistant.ui.dynamics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.graphview.GraphView;
import com.shurpo.financialassistant.graphview.GraphViewSeries;
import com.shurpo.financialassistant.graphview.LineGraphView;
import com.shurpo.financialassistant.ui.adapters.DynamicPageAdapter;
import com.shurpo.financialassistant.utils.GraphUtil;
import java.util.ArrayList;

/**
 * Created by Maksim_Shurpo on 9/29/2014.
 */
public class DynamicFragment extends Fragment{

    public static final int HALF_YEAR_POSITION = 0;
    public static final int MONTH_POSITION = 1;
    public static final int WEEK_POSITION = 2;

    private LinearLayout graphLayout;
    private int positionPage;
    private GraphViewSeries series;
    private GraphView graphView;
    private ArrayList<String> rates;
    private ArrayList<String> dates;
    private GraphUtil graphUtil;

    public DynamicFragment() {
    }

    public static DynamicFragment newInstance(){
        return new DynamicFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dynamic_graph_view, container, false);
        positionPage = getArguments().getInt(DynamicPageAdapter.POSITION_PAGE_KEY);
        rates = getArguments().getStringArrayList(DynamicPageAdapter.LIST_DYNAMIC_RATES_BUNDLE_KEY);
        dates = getArguments().getStringArrayList(DynamicPageAdapter.LIST_DYNAMIC_DATE_BUNDLE_KEY);
        graphUtil = new GraphUtil();
        graphUtil.setDynamicsDates(dates);
        graphUtil.setDynamicsRates(rates);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        graphLayout = (LinearLayout) view.findViewById(R.id.dynamic_graph_layout);
        graphView = new LineGraphView(getActivity(), "Валюта");
        int period = 0;
        switch (positionPage){
            case HALF_YEAR_POSITION:
                period = GraphUtil.PERIOD_HALF_YEAR;
                break;
            case MONTH_POSITION:
                period = GraphUtil.PERIOD_MONTH;
                break;
            case WEEK_POSITION:
                period = GraphUtil.PERIOD_WEEK;
                break;
        }
        drawGraph(period);
    }

    private void drawGraph(int period){
        graphLayout.removeAllViews();
        graphUtil.initGraphViewData();
        series = new GraphViewSeries(graphUtil.getData(period));
        graphView.addSeries(series);
        graphView.setHorizontalLabels(graphUtil.getLabels(period));
        graphView.getGraphViewStyle().setTextSize(10);
        graphLayout.addView(graphView);
    }
}
