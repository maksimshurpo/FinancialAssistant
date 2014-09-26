package com.shurpo.financialassistant.utils;

import android.content.Context;

public class WebRequestUtil {

    public static final int CURRENCY_RATE_KEY = 1;
    public static final int REF_RATE_KEY = 2;
    public static final int METALL_KEY = 3;

    private static final String URL_CURRENCIES = "http://www.nbrb.by/Services/XmlExRates.aspx";
    private static final String URL_ON_DATE = "ondate=";
    private static final String URL_REF_RATE = "http://www.nbrb.by/Services/XmlRefRate.aspx";
    private static final String URL_DYNAMIC = "http://www.nbrb.by/Services/XmlExRatesDyn.aspx";
    private static final String URL_CUR_ID = "curId=";
    private static final String URL_FROM_DATE = "fromDate=";
    private static final String URL_TO_DATE = "toDate=";

    private static final String URL_INGOTS_PRICE_METAL = "http://www.nbrb.by/Services/XmlIngots.aspx";
    private static final String URL_METAL = "http://www.nbrb.by/Services/XmlMetalsRef.aspx";

    public static final int CURRENCY_RATE_PROCESSOR = 200;
    public static final int DYNAMIC_PROCESSOR = 300;
    public static final int METAL_PROCESSOR = 400;
    public static final int INGOTS_PRICE_METAL_PROGRESS = 401;
    public static final int REF_RATE_PROCESSOR = 500;

    private String[] urls;
    private int[] progresses;
    private PreferenceUtil preferenceUtil;

    public WebRequestUtil(Context context) {
        preferenceUtil = new PreferenceUtil(context);
    }

    public WebRequestUtil url(int key){
        progresses = null;
        switch (key){
            case CURRENCY_RATE_KEY:
                urls = new String[]{getUrlOnDate(URL_CURRENCIES, preferenceUtil.getHistoryCurrencyDate())};
                progresses = new int[]{CURRENCY_RATE_PROCESSOR};
                break;
            case DYNAMIC_PROCESSOR:
                urls = new String[]{getUrlDynamic(URL_DYNAMIC, preferenceUtil.getCurrencyId(), DateUtil.getLastMonthDate(6),
                        DateUtil.getCurrentDate())};
                progresses = new int[]{DYNAMIC_PROCESSOR};
                break;
            case REF_RATE_KEY:
                urls = new String[]{URL_REF_RATE};
                progresses = new int[]{REF_RATE_PROCESSOR};
                break;
            case METALL_KEY:
                urls = new String[]{URL_METAL, getUrlOnDate(URL_INGOTS_PRICE_METAL)};
                progresses = new int[]{METAL_PROCESSOR, INGOTS_PRICE_METAL_PROGRESS};
                break;
            default:
                throw new UnsupportedOperationException("Unknown key for URL : " + key);
        }
        return this;
    }

    public String[] getUrls() {
        return urls;
    }

    private String getUrlOnDate(String url){
        return this.getUrlOnDate(url, DateUtil.getCurrentDate());
    }

    private String getUrlOnDate(String url, String date){
        return url + "?" + URL_ON_DATE + date;
    }

    private String getUrlDynamic(String url, String id, String fromDate, String toDate){
        return url + "?" + URL_CUR_ID + id + "&" + URL_FROM_DATE + fromDate + "&" + URL_TO_DATE + toDate;
    }

    public int[] getProgresses() {
        return progresses;
    }
}
