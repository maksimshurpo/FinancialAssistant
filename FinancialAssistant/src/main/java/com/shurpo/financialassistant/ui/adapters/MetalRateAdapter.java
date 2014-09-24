package com.shurpo.financialassistant.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract.*;
import com.shurpo.financialassistant.utils.CursorResultUtil;

public class MetalRateAdapter extends CursorAdapter {

    private static class ViewHolder {
        TextView nominalView;
        TextView nameView;
        TextView entitiesRublesView;
        TextView entitiesDollarsView;
        TextView dateView;
    }

    private LayoutInflater inflater;

    public MetalRateAdapter(Context context) {
        super(context, null, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        View view = inflater.inflate(R.layout.item_metal_view, null);
        viewHolder.nominalView = (TextView) view.findViewById(R.id.nominal_metal);
        viewHolder.nameView = (TextView) view.findViewById(R.id.name_metal);
        viewHolder.entitiesRublesView = (TextView) view.findViewById(R.id.entities_rubles);
        viewHolder.entitiesDollarsView = (TextView) view.findViewById(R.id.entities_dollars);
        viewHolder.dateView = (TextView) view.findViewById(R.id.date_metal);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.nameView.setText(cursor.getString(cursor.getColumnIndex(Metal.NAME)));
        viewHolder.dateView.setText(cursor.getString(cursor.getColumnIndex(IngotPriceMetal.INGOT_PRICE_DATE)));

        Integer resultInt = cursor.getInt(cursor.getColumnIndex(IngotPriceMetal.NOMINAL));
        viewHolder.nominalView.setText(CursorResultUtil.getIntResult(resultInt));

        Double resultDouble = cursor.getDouble(cursor.getColumnIndex(IngotPriceMetal.ENTITIES_RUBLES));
        viewHolder.entitiesRublesView.setText(CursorResultUtil.getDoubleResult(resultDouble));

        resultDouble = cursor.getDouble(cursor.getColumnIndex(IngotPriceMetal.ENTITIES_DOLLARS));
        viewHolder.entitiesDollarsView.setText(CursorResultUtil.getDoubleResult(resultDouble));
    }
}
