package com.shurpo.financialassistant.utils;

import android.database.Cursor;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract;

public final class CursorResultUtil {

    private CursorResultUtil() {

    }

    public static String getScaleCharCode(Cursor cursor){
        return cursor.getInt(cursor.getColumnIndex(FinancialAssistantContract.Currency.SCALE)) + " "
                + cursor.getString(cursor.getColumnIndex(FinancialAssistantContract.Currency.CHAR_CODE));
    }

    public static String getIntResult(Integer result){
        return "" + result;
    }

    public static String getDoubleResult(Double result){
        return formValue(result);
    }

    /**
     * @return if value doesn't have residue that get int value, else double value */
    private static String formValue(Double result){
        long resultInt = (long)(result * 100);
        long residue = resultInt % 10;
        if(residue == 0){
            resultInt = resultInt / 10;
            residue = resultInt % 10;
            if(residue == 0){
                resultInt = resultInt / 10;
                return "" + resultInt;
            }else {
                return "" + result;
            }
        }else {
            return "" + result;
        }
    }
}
