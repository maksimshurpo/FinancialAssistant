package com.shurpo.financialassistant.ui.currency;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment{

    public static final String ARG_YEAR = "ARG_YEAR";
    public static final String ARG_MONTH = "ARG_MONTH";
    public static final String ARG_DAY = "ARG_DAY";

    private DatePickerDialog.OnDateSetListener onDateSetListener;

    public void setCallBack(DatePickerDialog.OnDateSetListener onDateSetListener){
        this.onDateSetListener = onDateSetListener;
    }

    private int year;
    private int month;
    private int day;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt(ARG_YEAR);
        month = args.getInt(ARG_MONTH);
        day = args.getInt(ARG_DAY);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), onDateSetListener, year, month, day);
    }
}

