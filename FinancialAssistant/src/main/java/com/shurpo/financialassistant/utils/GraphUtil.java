package com.shurpo.financialassistant.utils;

import com.shurpo.financialassistant.graphview.GraphView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GraphUtil {

    private GraphView.GraphViewData[] weekDynamics;
    private GraphView.GraphViewData[] monthDynamics;
    private GraphView.GraphViewData[] halfYearDynamics;
    private String[] weekLabels;
    private String[] monthLabels;
    private String[] halfYearLabels;

    private final int DAY = 1;
    private final int MONTH = 0;
    private final int YEAR = 2;

    public static final int PERIOD_WEEK = 7;
    public static final int PERIOD_MONTH = 30;
    public static final int PERIOD_HALF_YEAR = 180;

    private final String DATE_SEPARATOR = "/";

    public void initGraphViewData(int period, DynamicUtil dynamicUtil) {
        GraphView.GraphViewData[] graphViewData = new GraphView.GraphViewData[dynamicUtil.getCurrencyInfoUtils().size()];
        List<CurrencyInfoUtil> utils = dynamicUtil.getCurrencyInfoUtils();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        ArrayList<Double> dynamicsRates = new ArrayList<Double>();
        ArrayList<String> dynamicsDates = new ArrayList<String>();
        for(CurrencyInfoUtil util:utils){
            dynamicsRates.add(util.getRate());
            dynamicsDates.add(format.format(util.getDate()));
        }


        weekDynamics = getDynamics( dynamicsRates.subList(dynamicsRates.size() - 7, dynamicsRates.size()));
        monthDynamics = getDynamics(dynamicsRates.subList(dynamicsRates.size() - 30, dynamicsRates.size()));
        halfYearDynamics = getDynamics(dynamicsRates.subList(dynamicsRates.size() - 180, dynamicsRates.size()));

        weekLabels = getLabels(PERIOD_WEEK, dynamicsDates.subList(dynamicsDates.size() - 7, dynamicsDates.size()));
        monthLabels = getLabels(PERIOD_MONTH, dynamicsDates.subList(dynamicsDates.size() - 30, dynamicsDates.size()));
        halfYearLabels = getLabels(PERIOD_HALF_YEAR, dynamicsDates.subList(dynamicsDates.size() - 180, dynamicsDates.size()));
    }

    private GraphView.GraphViewData[] getDynamics(List<Double> weekList){
        GraphView.GraphViewData[] dynamics = new GraphView.GraphViewData[weekList.size()];
        for(int i=0; i < weekList.size(); i++){
            dynamics[i] = new GraphView.GraphViewData(i + 1, weekList.get(i));
        }
        return dynamics;
    }

    private String[] getLabels(int period, List<String> labelsList){
        String separator = ".";
        String[] labels = new String[]{};
        switch(period){
            case PERIOD_WEEK:
                String[] weekLabels = new String[labelsList.size()];
                String[] weekDateParts;
                for(int i=0; i < labelsList.size(); i++){
                    weekDateParts = labelsList.get(i).split(DATE_SEPARATOR);
                    weekLabels[i] = weekDateParts[0] + separator + weekDateParts[1];
                }
                labels = weekLabels;
                break;
            case PERIOD_MONTH:
                ArrayList<String> monthLabels = new ArrayList<String>();
                String[] monthDateParts;

                for(int i=0; i < labelsList.size(); i++){
                    if(i%5 == 0){
                        // labels for every 5 days
                        monthDateParts = labelsList.get(i).split(DATE_SEPARATOR);
                        monthLabels.add(monthDateParts[DAY] + separator + monthDateParts[MONTH]);
                    }
                }
                monthDateParts = labelsList.get(labelsList.size() - 1).split(DATE_SEPARATOR);
                monthLabels.add(monthDateParts[DAY] + separator + monthDateParts[MONTH]);
                labels = monthLabels.toArray(labels);
                break;
            case PERIOD_HALF_YEAR:
                ArrayList<String> halfYearLabels = new ArrayList<String>();
                String[] halfYearDateParts;
                for(int i=0; i < labelsList.size() - 1; i++){
                    if(i%30 == 0){
                        // labels for every 30 days
                        halfYearDateParts = labelsList.get(i).split(DATE_SEPARATOR);
                        if(halfYearLabels.size() != 0){
                            halfYearLabels.add(halfYearDateParts[MONTH] + separator + halfYearDateParts[YEAR]);
                        } else{
                            halfYearLabels.add(halfYearDateParts[MONTH]);
                        }
                    }
                }
                halfYearDateParts = labelsList.get(labelsList.size() - 1).split(DATE_SEPARATOR);
                halfYearLabels.add(halfYearDateParts[MONTH]);
                labels = halfYearLabels.toArray(labels);
                break;
        }
        return labels;
    }

    public GraphView.GraphViewData[] getData(int period){
        GraphView.GraphViewData[] data = null;
        switch(period){
            case PERIOD_WEEK:
                if(weekDynamics != null){
                    data =  weekDynamics;
                }
                break;
            case PERIOD_MONTH:
                if(monthDynamics != null){
                    data =  monthDynamics;
                }
                break;
            case PERIOD_HALF_YEAR:
                if(halfYearDynamics != null){
                    data =  halfYearDynamics;
                }
                break;
        }
        return data;
    }

    public String[] getLabels(int period){
        String[] labels = null;
        switch(period){
            case PERIOD_WEEK:
                if(weekLabels != null){
                    labels =  weekLabels;
                }
                break;
            case PERIOD_MONTH:
                if(monthLabels != null){
                    labels =  monthLabels;
                }
                break;
            case PERIOD_HALF_YEAR:
                if(halfYearLabels != null){
                    labels =  halfYearLabels;
                }
                break;
        }
        return labels;
    }
}
