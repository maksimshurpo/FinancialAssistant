package com.shurpo.financialassistant.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

    public static final String PREFERENCE_KEY = "PREFERENCE_KEY";
    public static final String SAVE_CURRENCY_ID_KEY = "SAVE_CURRENCY_ID_KEY";
    public static final String SAVE_LAST_DATE_CURRENCY_KEY = "SAVE_LAST_DATE_CURRENCY_KEY";
    public static final String SAVE_DATE_METAL_KEY = "SAVE_DATE_METAL_KEY";
    public static final String DOWNLOAD_REF_RATE_KEY = "DOWNLOAD_REF_RATE_KEY";
    public static final String SAVE_CURRENT_REF_RATE_KEY = "SAVE_CURRENT_REF_RATE_KEY";
   // public static final String SAVE_HISTORY_CURRENCY_DATE_KEY = "SAVE_HISTORY_CURRENCY_DATE_KEY";
    public static final String SAVE_RESULT_CALCULATE_KEY = "SAVE_RESULT_CALCULATE_KEY";
    public static final String SAVE_NOMINAL_CALCULATE_KEY = "SAVE_NOMINAL_CALCULATE_KEY";
    public static final String SAVE_IS_SEND_RECEIVER_KEY  = "SAVE_IS_SEND_RECEIVER_KEY";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public PreferenceUtil(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void saveCurrencyId(String id){
        editor.putString(SAVE_CURRENCY_ID_KEY, id);
        editor.commit();
    }

    public void saveLastDateCurrency(String date){
        editor.putString(SAVE_LAST_DATE_CURRENCY_KEY, date);
        editor.commit();
    }

    public void saveLastDateMetal(String date) {
        editor.putString(SAVE_DATE_METAL_KEY, date);
        editor.commit();
    }

    public void setDownloadRefRate(boolean value){
        editor.putBoolean(DOWNLOAD_REF_RATE_KEY, value);
        editor.commit();
    }

    public void setCurrentRefRate(String refRate){
        editor.putString(SAVE_CURRENT_REF_RATE_KEY, refRate);
        editor.commit();
    }/*

    public void setHistoryCurrencyDate(String date){
        editor.putString(SAVE_HISTORY_CURRENCY_DATE_KEY, date);
        editor.commit();
    }*/

    public void saveResultCalculate(String result){
        editor.putString(SAVE_RESULT_CALCULATE_KEY, result);
        editor.commit();
    }

    public void saveNominalCalculate(String nominal){
        editor.putString(SAVE_NOMINAL_CALCULATE_KEY, nominal);
        editor.commit();
    }

    public void saveIsSendReceiver(boolean isSendReceiver){
        editor.putBoolean(SAVE_IS_SEND_RECEIVER_KEY, isSendReceiver);
        editor.commit();
    }

    public String getCurrencyId(){
        return preferences.getString(SAVE_CURRENCY_ID_KEY, null);
    }

    public String getLastDateCurrency(){
        return preferences.getString(SAVE_LAST_DATE_CURRENCY_KEY, null);
    }

    public String getDateMetal(){
        return preferences.getString(SAVE_DATE_METAL_KEY, null);
    }

    public boolean isDownloadRefRate(){
        return preferences.getBoolean(DOWNLOAD_REF_RATE_KEY, false);
    }

    public String getCurrentRefRate(){
        return preferences.getString(SAVE_CURRENT_REF_RATE_KEY, "0");
    }
/*
    public String getHistoryCurrencyDate(){
        return preferences.getString(SAVE_HISTORY_CURRENCY_DATE_KEY, DateUtil.getCurrentDate());
    }*/

    public String getResultCalculate(){
        return preferences.getString(SAVE_RESULT_CALCULATE_KEY, "0");
    }

    public String getNominalCalculate(){
        return preferences.getString(SAVE_NOMINAL_CALCULATE_KEY, "");
    }

    public Boolean getSenderReceiver(){
        return preferences.getBoolean(SAVE_IS_SEND_RECEIVER_KEY, false);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }
}
