package com.shurpo.financialassistant.ui.dynamics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Maksim_Shurpo on 9/29/2014.
 */
public class TestDynamicCurrencyFragment extends Fragment{

    public TestDynamicCurrencyFragment() {
    }

    public TestDynamicCurrencyFragment newInstance(){
        return new TestDynamicCurrencyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
