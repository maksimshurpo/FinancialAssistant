package com.shurpo.financialassistant.ui;

import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shurpo.financialassistant.R;
import com.shurpo.financialassistant.ui.calculate.CalculateFragment;
import com.shurpo.financialassistant.ui.currency.CurrencyFragment;
import com.shurpo.financialassistant.ui.adapters.DrawerListAdapter;
import com.shurpo.financialassistant.ui.dynamics.DynamicsCurrencyFragment;
import com.shurpo.financialassistant.ui.metal.MetalRateFragment;
import com.shurpo.financialassistant.ui.refinancing.RefinancingRateFragment;

public class FinancialAssistantActivity extends ActionBarActivity {

    private static final String ABOUT_APP_DIALOG_TAG = "ABOUT_APP_DIALOG_TAG";
    private static final int FIRST_ITEM_DRAWER = 0;

    private static final int HISTORY_CURRENCY_RATES = 0;
    private static final int RATE_METAL = 1;
    private static final int REFINANCING_RATE = 2;
    private static final int CALCULATE = 3;
    private static final int DYNAMIC = 4;

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerListAdapter adapter;
    private CharSequence title;
    private String[] formTitles;

    private ListView.OnItemClickListener drawerItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        title = getTitle();
        formTitles = getResources().getStringArray(R.array.drawer_texts);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.left_drawer);

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        adapter = new DrawerListAdapter(this, formTitles);
        drawerListView.setAdapter(adapter);
        drawerListView.setOnItemClickListener(drawerItemClickListener);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(title);
                supportInvalidateOptionsMenu();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(title);
                supportInvalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        if (savedInstanceState == null) {
            selectItem(FIRST_ITEM_DRAWER);
        }
    }

    private void selectItem(int position) {
        Fragment fragment;
        switch (position) {
            case HISTORY_CURRENCY_RATES:
                fragment = CurrencyFragment.newInstance();
                break;
            case RATE_METAL:
                fragment = MetalRateFragment.newInstance();
                break;
            case REFINANCING_RATE:
                fragment = RefinancingRateFragment.newInstance();
                break;
            case CALCULATE:
                fragment = CalculateFragment.newInstance();
                break;
            case DYNAMIC:
                fragment = DynamicsCurrencyFragment.newInstance();
                break;
            default:
                throw new IllegalArgumentException("Unknown item of drawer");
        }
        Bundle args = new Bundle();
        args.putInt(getString(R.string.arg_form_item_key), position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        drawerListView.setItemChecked(position, true);
        drawerListView.setSelection(position);
        setTitle(formTitles[position]);
        drawerLayout.closeDrawer(drawerListView);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerListView);
        menu.findItem(R.id.menu_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menu_settings:
                DialogFragment dialogFragment = new AboutAppDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), ABOUT_APP_DIALOG_TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
