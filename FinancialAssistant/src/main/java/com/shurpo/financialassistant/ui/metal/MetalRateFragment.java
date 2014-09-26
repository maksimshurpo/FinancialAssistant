package com.shurpo.financialassistant.ui.metal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.utils.WebRequestUtil;
import com.shurpo.financialassistant.ui.BaseFragment;
import com.shurpo.financialassistant.ui.adapters.MetalRateAdapter;
import com.shurpo.financialassistant.utils.DateUtil;

public class MetalRateFragment extends BaseFragment {

    public static final int METAL_LOADER = 40;

    public static MetalRateFragment newInstance(){
        return new MetalRateFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(new MetalRateAdapter(getActivity()));
        getListView().setAdapter(getAdapter());
        getActivity().getSupportLoaderManager().initLoader(METAL_LOADER, getBundle(), callbacks);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);
        String date = getPreference().getDateMetal();
        String currentDate = DateUtil.getCurrentDate();
        //update data when date is new day
        if (TextUtils.isEmpty(date) || !date.equals(currentDate)){
            refreshData(WebRequestUtil.METALL_KEY);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refreshData(WebRequestUtil.METALL_KEY);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateData() {
        getActivity().getSupportLoaderManager().restartLoader(METAL_LOADER, getBundle(), callbacks);
        stopProgressActionBar();
    }

    private Bundle getBundle(){
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY, getPreference().getDateMetal());
        return bundle;
    }
}
