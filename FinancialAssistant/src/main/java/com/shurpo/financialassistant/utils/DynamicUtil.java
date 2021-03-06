package com.shurpo.financialassistant.utils;

import android.database.Cursor;
import android.util.Log;

import com.shurpo.financialassistant.model.provider.FinancialAssistantContract;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DynamicUtil {

    private List<CurrencyInfoUtil> currencyInfoUtils = new ArrayList<CurrencyInfoUtil>();

    public List<CurrencyInfoUtil> getCurrency(Cursor cursor) {
        while (cursor.moveToNext()) {
            int currencyId = cursor.getInt(cursor.getColumnIndex(FinancialAssistantContract.Currency.CURRENCY_ID));
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date date = null;
            try {
                date = format.parse(cursor.getString(cursor.getColumnIndex(FinancialAssistantContract.Currency.CURRENCY_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Double rate = cursor.getDouble(cursor.getColumnIndex(FinancialAssistantContract.Currency.RATE));
            CurrencyInfoUtil util = new CurrencyInfoUtil(currencyId, date, rate);
            currencyInfoUtils.add(util);
        }
        return currencyInfoUtils;
    }

    public List<CurrencyInfoUtil> getCurrencyInfoUtils() {
        for (CurrencyInfoUtil util : currencyInfoUtils) {
            Log.d("zxcv", util.toString());
        }
        return currencyInfoUtils;
    }
}
