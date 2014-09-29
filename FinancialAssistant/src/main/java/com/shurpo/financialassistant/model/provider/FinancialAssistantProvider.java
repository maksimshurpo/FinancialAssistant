package com.shurpo.financialassistant.model.provider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.shurpo.financialassistant.utils.PreferenceUtil;
import com.shurpo.financialassistant.utils.SelectionBuilder;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract.*;

public class FinancialAssistantProvider extends ContentProvider {

    private String TAG_LOG = getClass().getName();

    private static final int CURRENCY = 101;
    private static final int METAL = 400;
    private static final int INGOTS_PRICE_METAL = 401;
    private static final int METAL_AND_INGOT_PRICE_METAL = 402;
    private static final int REF_RATE = 500;

    private FinancialAssistantDatabase openHelper;
    private PreferenceUtil preference;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = FinancialAssistantContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FinancialAssistantContract.PATH_CURRENCY, CURRENCY);
        matcher.addURI(authority, FinancialAssistantContract.PATH_METAL, METAL);
        matcher.addURI(authority, FinancialAssistantContract.PATH_INGOT_PRICE_METAL, INGOTS_PRICE_METAL);
        matcher.addURI(authority, FinancialAssistantContract.PATH_METAL_AND_INGOT_PRICE_METAL, METAL_AND_INGOT_PRICE_METAL);
        matcher.addURI(authority, FinancialAssistantContract.PATH_REF_RATE, REF_RATE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        openHelper = new FinancialAssistantDatabase(getContext());
        preference = new PreferenceUtil(getContext().getApplicationContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        int urlType = uriMatcher.match(uri);
        SelectionBuilder builder = new SelectionBuilder();
        switch (urlType) {
            case CURRENCY:
                builder.table(Tables.CURRENCY);
                break;
            case METAL_AND_INGOT_PRICE_METAL:
                builder.table(Tables.META_JOIN_INGOT_PRICE_METAL);
                break;
            case REF_RATE:
                builder.table(Tables.REF_RATE);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        builder.where(selection, selectionArgs);
        Cursor cursor = builder.query(db, projection, sortOrder);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        assert db != null;
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case CURRENCY:
                db.insert(Tables.CURRENCY, null, values);
                String dateCurrency = values.getAsString(Currency.CURRENCY_DATE);
                Log.d(TAG_LOG, dateCurrency);
                if (!TextUtils.isEmpty(dateCurrency)) {
                    preference.saveLastDateCurrency(dateCurrency);
                }
                break;
            case METAL:
                db.insert(Tables.METAL, null, values);
                break;
            case INGOTS_PRICE_METAL:
                String dateMetal = values.getAsString(IngotPriceMetal.INGOT_PRICE_DATE);
                if (!TextUtils.isEmpty(dateMetal)) {
                    preference.saveDateMetal(dateMetal);
                }
                db.insert(Tables.INGOT_PRICE_METAL, null, values);
                break;
            case REF_RATE:
                preference.setDownloadRefRate(true);
                db.insert(Tables.REF_RATE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        int uriType = uriMatcher.match(uri);
        switch (uriType){
            case CURRENCY:
                int deleteNum = db.delete(Tables.CURRENCY, null, null);
                Log.d(TAG_LOG, "Has deleted information in the table of currency");
                return deleteNum;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
