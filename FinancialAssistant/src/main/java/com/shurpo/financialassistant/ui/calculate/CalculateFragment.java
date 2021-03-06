package com.shurpo.financialassistant.ui.calculate;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract;
import com.shurpo.financialassistant.ui.BaseFragment;
import com.shurpo.financialassistant.ui.currency.CurrencyFragment;
import com.shurpo.financialassistant.utils.CursorResultUtil;
import com.shurpo.financialassistant.utils.DateUtil;
import com.shurpo.financialassistant.utils.WebRequestUtil;

public class CalculateFragment extends BaseFragment {

    private Spinner spinnerView;
    private EditText nominalView;
    private TextView resultView;
    private Button deleteNominalView;
    private Button applyCalculateView;
    private Double costCurrency;
    private int positionSpinner;

    private OnLoaderCallback onLoaderCallback = new OnLoaderCallback() {
        @Override
        public void onLoadFinished(Loader loader, Object o) {
            getAdapter().swapCursor((Cursor)o);
        }
    };

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            positionSpinner = position;
            Cursor cursor = getAdapter().getCursor();
            cursor.moveToPosition(position);
            costCurrency = cursor.getDouble(cursor.getColumnIndex(FinancialAssistantContract.Currency.RATE));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.apply_calculate:
                    if (TextUtils.isEmpty(nominalView.getText().toString()) || costCurrency == null){
                        return;
                    }
                    Double result = costCurrency * Double.valueOf(nominalView.getText().toString());
                    getPreference().saveResultCalculate("" + CursorResultUtil.getDoubleResult(result) + " руб");
                    getPreference().saveNominalCalculate(nominalView.getText().toString());
                    resultView.setText(getPreference().getResultCalculate());
                    break;
                case R.id.delete_nominal:
                    getPreference().saveNominalCalculate("");
                    getPreference().saveResultCalculate("0");
                    nominalView.setText(getPreference().getNominalCalculate());
                    resultView.setText(getPreference().getResultCalculate());
                    break;
            }
        }
    };

    public static CalculateFragment newInstance() {
        return new CalculateFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calculate_layout, container, false);
        int positionDrawerItem = getArguments().getInt(getString(R.string.arg_form_item_key));
        String title = getResources().getStringArray(R.array.drawer_texts)[positionDrawerItem];
        getActivity().setTitle(title);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnLoaderCallback(onLoaderCallback);
        nominalView = (EditText)view.findViewById(R.id.nominal_currency_calculate);
        resultView = (TextView)view.findViewById(R.id.result_calculate);
        applyCalculateView = (Button)view.findViewById(R.id.apply_calculate);
        deleteNominalView = (Button)view.findViewById(R.id.delete_nominal);
        spinnerView = (Spinner) view.findViewById(R.id.calculate_char_code_one);

        nominalView.setText(getPreference().getNominalCalculate());
        resultView.setText(getPreference().getResultCalculate());

        String[] from = new String[]{FinancialAssistantContract.Currency.NAME};
        int[] to = new int[]{android.R.id.text1};
        setAdapter(new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, from, to, 0));

        Bundle bundle = new Bundle();
        bundle.putString(LOADER_BUNDLE_KEY, DateUtil.getCurrentDate());
        getActivity().getSupportLoaderManager().initLoader(CurrencyFragment.CURRENCY_LOADER, bundle, callbacks);

        spinnerView.setAdapter(getAdapter());
        spinnerView.setOnItemSelectedListener(onItemSelectedListener);

        applyCalculateView.setOnClickListener(onClickListener);
        deleteNominalView.setOnClickListener(onClickListener);
    }

    @Override
    protected void updateData() {
    }

    @Override
    protected SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        };
    }
}
