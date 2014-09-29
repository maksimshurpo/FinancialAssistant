package com.shurpo.financialassistant.ui.currency;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.*;
import android.widget.DatePicker;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.ui.adapters.CurrencyRateAdapter;
import com.shurpo.financialassistant.utils.DateUtil;
import com.shurpo.financialassistant.utils.WebRequestUtil;
import com.shurpo.financialassistant.ui.BaseFragment;
import java.util.Calendar;
import java.util.Date;

public class CurrencyFragment extends BaseFragment {

    private String LOG_TAG = getClass().getName();

    public static final int CURRENCY_LOADER = 20;

    private OnLoaderCallback onLoadFinishedCallback = new OnLoaderCallback() {
        @Override
        public void onLoadFinished(Loader loader, Object o) {
            Cursor cursor = (Cursor) o;
            cursor.moveToFirst();

            /**If the table has not got information about date*/
            if (cursor.getCount() == 0) {
                Log.d(LOG_TAG, "Cursor has got " + cursor.getCount() + " count of rows.");
                refreshData(WebRequestUtil.RequestUri.currencyRate);
            }else {
                getAdapter().swapCursor(cursor);
            }
        }
    };

    private DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            Date dateFormat = calendar.getTime();
            String dateString = DateUtil.getFormatDate(dateFormat);
            getPreference().saveLastDateCurrency(dateString);
            refreshData(WebRequestUtil.RequestUri.currencyRate);
        }
    };

    public static CurrencyFragment newInstance() {
        return new CurrencyFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(new CurrencyRateAdapter(getActivity()));
        setOnLoaderCallback(onLoadFinishedCallback);
        getActivity().getSupportLoaderManager().initLoader(CURRENCY_LOADER, null, callbacks);
        getListView().setAdapter(getAdapter());
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

    private void showDatePicker() {
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
        getActivity().getSupportLoaderManager().restartLoader(CURRENCY_LOADER, null, callbacks);
        stopProgressActionBar();
    }
}
