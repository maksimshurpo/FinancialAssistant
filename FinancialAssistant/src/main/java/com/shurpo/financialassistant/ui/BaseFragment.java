package com.shurpo.financialassistant.ui;

import android.content.*;
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
import com.shurpo.financialassistant.ui.currency.CurrencyFragment;
import com.shurpo.financialassistant.utils.DateUtil;
import com.shurpo.financialassistant.utils.WebRequestUtil;
import com.shurpo.financialassistant.ui.metal.MetalRateFragment;
import com.shurpo.financialassistant.ui.refinancing.RefinancingRateFragment;
import com.shurpo.financialassistant.utils.PreferenceUtil;

public abstract class BaseFragment extends Fragment {

    public class LoaderInformationReceiver extends BroadcastReceiver {

        public static final String CURRENCY_RECEIVER = "com.shurpo.financialassistant";
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

    public interface OnLoaderCallback {
        public void onLoadFinished(Loader loader, Object o);
    }

    protected static final String BUNDLE_KEY = "BUNDLE_KEY";

    private OnLoaderCallback onLoaderCallback;

    protected LoaderManager.LoaderCallbacks callbacks = new LoaderManager.LoaderCallbacks() {
        @Override
        public Loader onCreateLoader(int id, Bundle bundle) {
            String select = null;
            String[] selectArgs = null;
            String args;
            switch (id) {
                case CurrencyFragment.CURRENCY_LOADER:
                    String date = preference.getLastDateCurrency();
                    select = Currency.CURRENCY_DATE + "=?";
                    selectArgs = new String[]{date};
                    return new CursorLoader(getActivity(), Currency.CONTENT_URI, null, select, selectArgs, null);
                case MetalRateFragment.METAL_LOADER:
                    args = (String) bundle.get(BUNDLE_KEY);
                    if (!TextUtils.isEmpty(args)) {
                        select = IngotPriceMetal.INGOT_PRICE_DATE + "=?";
                        selectArgs = new String[]{args};
                    }
                    return new CursorLoader(getActivity(), MetalAndIngotPriceMetal.CONTENT_URI, null, select, selectArgs, null);
                case RefinancingRateFragment.REF_RATE_LOADER:
                    String orderBy = BaseColumns._ID + " DESC";
                    return new CursorLoader(getActivity(), RefRate.CONTENT_URI, null, null, null, orderBy);
                default:
                    throw new IllegalArgumentException("Unknown bundle id : " + id);
            }
        }

        @Override
        public void onLoadFinished(Loader loader, Object o) {
            onLoaderCallback.onLoadFinished(loader, o);
        }

        @Override
        public void onLoaderReset(Loader loader) {
            adapter.swapCursor(null);
        }
    };

    private NetworkReceiver networkReceiver;
    private LoaderInformationReceiver loaderInformationReceiver;
    private CursorAdapter adapter;
    private PreferenceUtil preference;
    private ProgressBar progressBar;
    private ListView listView;

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
        loaderInformationReceiver = new LoaderInformationReceiver();
        IntentFilter filterLoader = new IntentFilter(LoaderInformationReceiver.CURRENCY_RECEIVER);
        getActivity().registerReceiver(loaderInformationReceiver, filterLoader);

        //init some views.
        listView = (ListView) view.findViewById(R.id.list_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.update_rate);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (networkReceiver != null) {
            getActivity().unregisterReceiver(networkReceiver);
            networkReceiver = null;
        }
        if (loaderInformationReceiver != null) {
            getActivity().unregisterReceiver(loaderInformationReceiver);
            loaderInformationReceiver = null;
        }
    }

    protected abstract void updateData();

    protected void refreshData(WebRequestUtil.RequestUri requestUri) {
        if (NetworkReceiver.refreshDisplay) {
            ServiceHelper serviceHelper = ServiceHelper.getInstance(getActivity());
            WebRequestUtil webRequest = new WebRequestUtil(getActivity());
            serviceHelper.execute(webRequest.url(requestUri));
            progressBar.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getActivity(), "Связь потеряна", Toast.LENGTH_SHORT).show();
            stopProgressActionBar();
        }
    }

    protected void stopProgressActionBar() {
        progressBar.setVisibility(View.GONE);
    }

    public OnLoaderCallback getOnLoaderCallback() {
        return onLoaderCallback;
    }

    public void setOnLoaderCallback(OnLoaderCallback onLoaderCallback) {
        this.onLoaderCallback = onLoaderCallback;
    }

    public CursorAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(CursorAdapter adapter) {
        this.adapter = adapter;
    }

    public ListView getListView() {
        return listView;
    }

    public PreferenceUtil getPreference() {
        return preference;
    }
}
