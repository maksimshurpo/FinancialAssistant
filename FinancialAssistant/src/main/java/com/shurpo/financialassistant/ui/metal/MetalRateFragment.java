package com.shurpo.financialassistant.ui.metal;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.utils.WebRequestUtil;
import com.shurpo.financialassistant.ui.BaseFragment;
import com.shurpo.financialassistant.ui.adapters.MetalRateAdapter;
import com.shurpo.financialassistant.utils.DateUtil;

public class MetalRateFragment extends BaseFragment {

    private String LOG_TAG = getClass().getName();

    public static final int METAL_LOADER = 40;
    private OnLoaderCallback onLoadFinishedCallback = new OnLoaderCallback() {
        @Override
        public void onLoadFinished(Loader loader, Object o) {
            Cursor cursor = (Cursor) o;
            cursor.moveToFirst();

            /**If the table has not got information about date*/
            if (cursor.getCount() == 0) {
                Log.d(LOG_TAG, "Cursor has got " + cursor.getCount() + " count of rows.");
                refreshData(WebRequestUtil.RequestUri.metal);
            }else {
                getAdapter().swapCursor(cursor);
            }
        }
    };

    public static MetalRateFragment newInstance(){
        return new MetalRateFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnLoaderCallback(onLoadFinishedCallback);
        setAdapter(new MetalRateAdapter(getActivity()));
        getActivity().getSupportLoaderManager().initLoader(METAL_LOADER, null, callbacks);
        getListView().setAdapter(getAdapter());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);
        String date = getPreference().getDateMetal();
        String currentDate = DateUtil.getCurrentDate();
        //update data when date is new day
        if (TextUtils.isEmpty(date) || !date.equals(currentDate)){
            refreshData(WebRequestUtil.RequestUri.metal);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refreshData(WebRequestUtil.RequestUri.metal);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateData() {
        getActivity().getSupportLoaderManager().restartLoader(METAL_LOADER, null, callbacks);
        stopProgressActionBar();
    }

}
