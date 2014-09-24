package com.shurpo.financialassistant.ui.currency;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.DatePicker;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.ui.adapters.CurrencyRateAdapter;
import com.shurpo.financialassistant.utils.DateUtil;
import com.shurpo.financialassistant.utils.WebRequestUtil;
import com.shurpo.financialassistant.ui.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HistoryCurrencyRatesFragment extends BaseFragment {

    public static final int HISTORY_CURRENCY_LOADER = 20;

    private DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            Date dateFormat = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            preference.setHistoryCurrencyDate(format.format(dateFormat));
            refreshData(WebRequestUtil.HISTORY_CURRENCY_RATE_PROCESSOR);
        }
    };

    public static HistoryCurrencyRatesFragment newInstance() {
        return new HistoryCurrencyRatesFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new CurrencyRateAdapter(getActivity());
        listView.setAdapter(adapter);
        getActivity().getSupportLoaderManager().initLoader(HISTORY_CURRENCY_LOADER, null, callbacks);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_date, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_date:
                showDatePicker();
                return true;
        }
        return true;
    }

    private void showDatePicker(){
        DatePickerFragment date = new DatePickerFragment();
        Calendar calendar = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt(DatePickerFragment.ARG_YEAR, calendar.get(Calendar.YEAR));
        args.putInt(DatePickerFragment.ARG_MONTH, calendar.get(Calendar.MONTH));
        args.putInt(DatePickerFragment.ARG_DAY, calendar.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);

        date.setCallBack(onDate);
        date.show(getActivity().getSupportFragmentManager(), "Date Picker");
    }

    @Override
    protected void updateData() {
        getActivity().getSupportLoaderManager().restartLoader(HISTORY_CURRENCY_LOADER, null, callbacks);
        stopProgressActionBar();
    }
}
