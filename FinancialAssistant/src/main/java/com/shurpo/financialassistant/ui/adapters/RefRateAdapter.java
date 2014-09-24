package com.shurpo.financialassistant.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract.*;
import com.shurpo.financialassistant.utils.CursorResultUtil;
import com.shurpo.financialassistant.utils.PreferenceUtil;

public class RefRateAdapter extends CursorAdapter {

    private static class ViewHolder {
        TextView dateRefRateView;
        TextView refRateView;
    }

    private LayoutInflater inflater;
    private PreferenceUtil preferenceUtil;

    public RefRateAdapter(Context context, PreferenceUtil preferenceUtil) {
        super(context, null, 0);
        this.preferenceUtil = preferenceUtil;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        View view = inflater.inflate(R.layout.item_ref_rate_view, null);
        viewHolder.dateRefRateView = (TextView) view.findViewById(R.id.date_ref_rate);
        viewHolder.refRateView = (TextView) view.findViewById(R.id.ref_rate);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.dateRefRateView.setText(cursor.getString(cursor.getColumnIndex(RefRate.REF_DATE)));
        Double result = cursor.getDouble(cursor.getColumnIndex(RefRate.REF_VALUE));
        viewHolder.refRateView.setText(CursorResultUtil.getDoubleResult(result));
        if (cursor.getPosition() == 0){
            preferenceUtil.setCurrentRefRate(CursorResultUtil.getDoubleResult(result));
        }
    }
}
