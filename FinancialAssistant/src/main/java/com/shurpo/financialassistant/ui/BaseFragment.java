package com.shurpo.financialassistant.ui;

import android.content.*;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.*;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract.*;
import com.shurpo.financialassistant.model.receivers.NetworkReceiver;
import com.shurpo.financialassistant.model.service.ServiceHelper;
import com.shurpo.financialassistant.ui.currency.HistoryCurrencyRatesFragment;
import com.shurpo.financialassistant.utils.WebRequestUtil;
import com.shurpo.financialassistant.ui.metal.MetalRateFragment;
import com.shurpo.financialassistant.ui.currency.CurrencyRatesFragment;
import com.shurpo.financialassistant.ui.refinancing.RefinancingRateFragment;
import com.shurpo.financialassistant.utils.PreferenceUtil;

public abstract class BaseFragment extends Fragment {

    public class LoaderReceiver extends BroadcastReceiver {

        public static final String CURRENCY_RECEIVER = "com.shurpo.financialassistant.ui.currency.CurrencyRatesFragment";
        public static final String EXTRA_LOAD_DATA = "EXTRA_LOAD_DATA";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(EXTRA_LOAD_DATA, false)) {
                updateData();
            } else {
                /*if disconnect voice that stop progress action bar*/
                Toast.makeText(getActivity(), "Нет данных", Toast.LENGTH_SHORT).show();
                stopProgressActionBar();
            }
        }
    }

    protected static final String BUNDLE_KEY = "BUNDLE_KEY";

    protected LoaderManager.LoaderCallbacks callbacks = new LoaderManager.LoaderCallbacks() {
        @Override
        public Loader onCreateLoader(int id, Bundle bundle) {
            String select = null;
            String[] selectArgs = null;
            String args;
            switch (id) {
                case CurrencyRatesFragment.CURRENCY_LOADER:
                    args = (String) bundle.get(BUNDLE_KEY);
                    if (!TextUtils.isEmpty(args)) {
                        select = CurrencyInfo.CURRENCY_DATE + "=?";
                        selectArgs = new String[]{args};
                    }
                    return new CursorLoader(getActivity(), Currency.CONTENT_URI, null, select, selectArgs, null);
                case HistoryCurrencyRatesFragment.HISTORY_CURRENCY_LOADER:
                    String date = preference.getHistoryCurrencyDate();
                    if (!TextUtils.isEmpty(date)) {
                        select = CurrencyInfo.CURRENCY_DATE + "=?";
                        selectArgs = new String[]{date};
                    }
                    return new CursorLoader(getActivity(), Currency.CONTENT_URI, null, select, selectArgs, null);
                case MetalRateFragment.METAL_LOADER:
                    args = (String) bundle.get(BUNDLE_KEY);
                    if (!TextUtils.isEmpty(args)) {
                        select = IngotPriceMetal.INGOT_PRICE_DATE + "=?";
                        selectArgs = new String[]{args};
                    }
                    CursorLoader cursorLoader = new CursorLoader(getActivity(), MetalAndIngotPriceMetal.CONTENT_URI, null, select, selectArgs, null);
                    return cursorLoader;
                case RefinancingRateFragment.REF_RATE_LOADER:
                    String orderBy = BaseColumns._ID + " DESC";
                    return new CursorLoader(getActivity(), RefRate.CONTENT_URI, null, null, null, orderBy);
                default:
                    throw new IllegalArgumentException("Unknown bundle id : " + id);
            }
        }

        @Override
        public void onLoadFinished(Loader loader, Object o) {
            adapter.swapCursor((Cursor) o);
        }

        @Override
        public void onLoaderReset(Loader loader) {
            adapter.swapCursor(null);
        }
    };

    private NetworkReceiver networkReceiver;
    private LoaderReceiver loaderReceiver;
    protected CursorAdapter adapter;
    protected PreferenceUtil preference;
    protected ProgressBar progressBar;
    protected ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        preference = new PreferenceUtil(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_layout, container, false);
        int positionDrawerItem = getArguments().getInt(getString(R.string.arg_form_item_key));
        String title = getResources().getStringArray(R.array.drawer_texts)[positionDrawerItem];
        getActivity().setTitle(title);
        //init some views.
        listView = (ListView) rootView.findViewById(R.id.list_layout);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*register network receiver*/
        networkReceiver = new NetworkReceiver();
        IntentFilter filterNetwork = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(networkReceiver, filterNetwork);
        /*register update cursor receiver*/
        loaderReceiver = new LoaderReceiver();
        IntentFilter filterLoader = new IntentFilter(LoaderReceiver.CURRENCY_RECEIVER);
        getActivity().registerReceiver(loaderReceiver, filterLoader);

        progressBar = (ProgressBar) view.findViewById(R.id.update_rate);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (networkReceiver != null) {
            getActivity().unregisterReceiver(networkReceiver);
            networkReceiver = null;
        }
        if (loaderReceiver != null) {
            getActivity().unregisterReceiver(loaderReceiver);
            loaderReceiver = null;
        }
    }

    protected abstract void updateData();

    protected void refreshData(int webRequestInt) {
        if (NetworkReceiver.refreshDisplay) {
            ServiceHelper serviceHelper = ServiceHelper.getInstance(getActivity());
            WebRequestUtil webRequest = new WebRequestUtil(getActivity());
            serviceHelper.execute(webRequest.url(webRequestInt));
            progressBar.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getActivity(), "Связь потеряна", Toast.LENGTH_SHORT).show();
            stopProgressActionBar();
        }
    }

    protected void stopProgressActionBar() {
        progressBar.setVisibility(View.GONE);
    }
}
