package com.shurpo.financialassistant.ui.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.utils.PreferenceUtil;

public class DrawerListAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView itemView;
        ImageView iconView;
        TextView refRateView;
    }

    private static final int ELEMENTS_DOESNT_EXISTS = 0;
    private static final int POSITION_REF_RATE = 3;

    private LayoutInflater layoutInflater;
    private String[] drawerTextArray;
    private TypedArray drawerIcons;
    private PreferenceUtil preferenceUtil;

    public DrawerListAdapter(Context context, String[] drawerList) {
        this.drawerTextArray = drawerList;
        drawerIcons = context.getResources().obtainTypedArray(R.array.drawer_icons);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public int getCount() {
        return drawerTextArray.length;
    }

    @Override
    public Object getItem(int position) {
        return drawerTextArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        convertView = layoutInflater.inflate(R.layout.item_drawer_list_view, null);
        viewHolder.itemView = (TextView) convertView.findViewById(R.id.text_item_drawer);
        viewHolder.iconView = (ImageView) convertView.findViewById(R.id.img_item_drawer);
        viewHolder.refRateView = (TextView) convertView.findViewById(R.id.drawer_ref_rate);
        convertView.setTag(convertView);

        if (drawerTextArray.length != ELEMENTS_DOESNT_EXISTS) {
            viewHolder.itemView.setText(drawerTextArray[position]);
            viewHolder.iconView.setImageDrawable(drawerIcons.getDrawable(position));
            if (position == POSITION_REF_RATE){
                viewHolder.refRateView.setVisibility(View.VISIBLE);
                viewHolder.refRateView.setText(preferenceUtil.getCurrentRefRate());
            }else {
                viewHolder.refRateView.setVisibility(View.GONE);
            }
        }
        return convertView;
    }
}
