package com.shurpo.financialassistant.ui.currency;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.ImageView;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.utils.WebRequestUtil;
import com.shurpo.financialassistant.ui.BaseFragment;
import com.shurpo.financialassistant.ui.adapters.CurrencyRateAdapter;
import com.shurpo.financialassistant.utils.DateUtil;

public class CurrencyRatesFragment extends BaseFragment {

    //public static final int CURRENCY_LOADER = 10;

    public static CurrencyRatesFragment newInstance(){
        return new CurrencyRatesFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
     /*   adapter = new CurrencyRateAdapter(getActivity());
        listView.setAdapter(adapter);
        getActivity().getSupportLoaderManager().initLoader(CURRENCY_LOADER, getBundle(), callbacks);
        //update data when date is new day
        String date = preference.getDateCurrency();
        String currentDate = DateUtil.getCurrentDate();
        if (TextUtils.isEmpty(date) || !date.equals(currentDate)){
            refreshData(WebRequestUtil.CURRENCY_RATE_KEY);
        }*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refreshData(WebRequestUtil.CURRENCY_RATE_KEY);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void updateData() {
       /* getActivity().getSupportLoaderManager().restartLoader(CURRENCY_LOADER, getBundle(), callbacks);
        stopProgressActionBar();*/
    }

    private Bundle getBundle(){
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY, getPreference().getDateCurrency());
        return bundle;
    }
}
