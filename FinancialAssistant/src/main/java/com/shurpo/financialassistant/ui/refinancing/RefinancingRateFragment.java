package com.shurpo.financialassistant.ui.refinancing;

import android.os.Bundle;
import android.view.*;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.utils.WebRequestUtil;
import com.shurpo.financialassistant.ui.BaseFragment;
import com.shurpo.financialassistant.ui.adapters.RefRateAdapter;

public class RefinancingRateFragment extends BaseFragment {

    public static final int REF_RATE_LOADER = 50;

    public static RefinancingRateFragment newInstance(){
        return new RefinancingRateFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new RefRateAdapter(getActivity(), preference);
        listView.setAdapter(adapter);
        getActivity().getSupportLoaderManager().initLoader(REF_RATE_LOADER, null, callbacks);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);
        if(!preference.isDownloadRefRate()){
            refreshData(WebRequestUtil.REF_RATE_KEY);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refreshData(WebRequestUtil.REF_RATE_KEY);
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
