package com.shurpo.financialassistant.ui.dynamics;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Maksim_Shurpo on 9/29/2014.
 */
public class ActionBarManager {

    private Context context;
    private ActionBar actionBar;
    private ActionBar.TabListener tabListener;

    public ActionBarManager(Context context) {
        this.context = context;
        actionBar = ((ActionBarActivity) context).getSupportActionBar();
    }

    public void CreateTagsActionBar(ActionBar.TabListener tabListener, String... nameTabs){
        this.tabListener = tabListener;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (String value : nameTabs){
            actionBar.addTab(actionBar.newTab().setText(value).setTabListener(tabListener));
        }
    }

    public ActionBar.TabListener getTabListener() {
        return tabListener;
    }

    public void removeAllActionBarTags(){
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    public void setDisplayHomeAsUpEnabled(boolean displayHomeAsUpEnabled){
        actionBar.setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled);
    }

    public void setHomeButtonEnabled(boolean homeButtonEnabled){
        actionBar.setHomeButtonEnabled(homeButtonEnabled);
    }

    public ActionBar getActionBar() {
        return actionBar;
    }
}
