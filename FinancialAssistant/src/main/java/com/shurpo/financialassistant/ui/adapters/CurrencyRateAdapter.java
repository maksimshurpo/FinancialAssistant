package com.shurpo.financialassistant.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract.*;
import com.shurpo.financialassistant.utils.CursorResultUtil;

public class CurrencyRateAdapter extends CursorAdapter{

    private static class ViewHolder{
        TextView scaleCharCodeView;
        TextView nameView;
        TextView rateView;
        TextView dateView;
    }

    private LayoutInflater inflater;

    public CurrencyRateAdapter(Context context) {
        super(context, null, 0);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        View view = inflater.inflate(R.layout.item_currency_view, null);
        viewHolder.scaleCharCodeView = (TextView)view.findViewById(R.id.scale_char_code);
        viewHolder.nameView = (TextView)view.findViewById(R.id.name_currency);
        viewHolder.rateView = (TextView)view.findViewById(R.id.rate_currency);
        viewHolder.dateView = (TextView)view.findViewById(R.id.date_currency);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        viewHolder.scaleCharCodeView.setText(CursorResultUtil.getScaleCharCode(cursor));
        viewHolder.nameView.setText(cursor.getString(cursor.getColumnIndex(Currency.NAME)));

        Double result = cursor.getDouble(cursor.getColumnIndex(FinancialAssistantContract.CurrencyInfo.RATE));
        viewHolder.rateView.setText(CursorResultUtil.getDoubleResult(result));
        viewHolder.dateView.setText(cursor.getString(cursor.getColumnIndex(CurrencyInfo.CURRENCY_DATE)));

    }

}
