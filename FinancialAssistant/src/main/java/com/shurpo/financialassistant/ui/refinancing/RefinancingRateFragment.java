package com.shurpo.financialassistant.ui.refinancing;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.*;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.utils.WebRequestUtil;
import com.shurpo.financialassistant.ui.BaseFragment;
import com.shurpo.financialassistant.ui.adapters.RefRateAdapter;

public class RefinancingRateFragment extends BaseFragment {

    private String LOG_TAG = getClass().getName();

    public static final int REF_RATE_LOADER = 50;

    private OnLoaderCallback onLoadFinishedCallback = new OnLoaderCallback() {
        @Override
        public void onLoadFinished(Loader loader, Object o) {
            Cursor cursor = (Cursor) o;
            cursor.moveToFirst();

            /**If the table has not got information about date*/
            if (cursor.getCount() == 0) {
                Log.d(LOG_TAG, "Cursor has got " + cursor.getCount() + " count of rows.");
                refreshData(WebRequestUtil.RequestUri.refRate);
            }else {
                getAdapter().swapCursor(cursor);
            }
        }
    };

    public static RefinancingRateFragment newInstance(){
        return new RefinancingRateFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(new RefRateAdapter(getActivity(), getPreference()));
        setOnLoaderCallback(onLoadFinishedCallback);
        getActivity().getSupportLoaderManager().initLoader(REF_RATE_LOADER, null, callbacks);
        getListView().setAdapter(getAdapter());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);
        if(!getPreference().isDownloadRefRate()){
            refreshData(WebRequestUtil.RequestUri.refRate);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refreshData(WebRequestUtil.RequestUri.refRate);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateData() {
        getActivity().getSupportLoaderManager().restartLoader(REF_RATE_LOADER, null, callbacks);
        stopProgressActionBar();
    }
}
