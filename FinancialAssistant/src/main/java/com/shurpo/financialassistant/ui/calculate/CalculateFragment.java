package com.shurpo.financialassistant.ui.calculate;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract;
import com.shurpo.financialassistant.ui.BaseFragment;
import com.shurpo.financialassistant.utils.CursorResultUtil;
import com.shurpo.financialassistant.utils.DateUtil;

public class CalculateFragment extends BaseFragment {

    private Spinner spinnerView;
    private EditText nominalView;
    private TextView resultView;
    private Button deleteNominalView;
    private Button applyCalculateView;
    private Double costCurrency;
    private int positionSpinner;

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
        bundle.putString(BUNDLE_KEY, DateUtil.getCurrentDate());
        getActivity().getSupportLoaderManager().initLoader(10, bundle, callbacks);

        spinnerView.setAdapter(getAdapter());
        spinnerView.setOnItemSelectedListener(onItemSelectedListener);

        applyCalculateView.setOnClickListener(onClickListener);
        deleteNominalView.setOnClickListener(onClickListener);
    }

    @Override
    protected void updateData() {
    }
}
