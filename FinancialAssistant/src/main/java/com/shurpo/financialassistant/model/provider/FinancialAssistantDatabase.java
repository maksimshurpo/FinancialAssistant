package com.shurpo.financialassistant.model.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract.*;

public class FinancialAssistantDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "assistant.db";
    private static final int DATABASE_VERSION = 7;

    public FinancialAssistantDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.CURRENCY + " ( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Currency.CURRENCY_ID + " INTEGER NOT NULL, "
                + Currency.CURRENCY_DATE + " TEXT NOT NULL, "
                + Currency.RATE + " REAL NOT NULL, "
                + Currency.NUM_CODE + " INTEGER NOT NULL, "
                + Currency.CHAR_CODE + " TEXT NOT NULL, "
                + Currency.SCALE + " INTEGER NOT NULL, "
                + Currency.NAME + " TEXT NOT NULL, "
                + Currency.FAVOURITE + " INTEGER NOT NULL, "
                + "UNIQUE (" + Currency.CURRENCY_ID + ") ON CONFLICT IGNORE );");
        db.execSQL("CREATE TABLE " + Tables.METAL + " ( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Metal.METAL_ID + " INTEGER NOT NULL, "
                + Metal.NAME + " TEXT NOT NULL, "
                + Metal.FAVOURITE + " INTEGER NOT NULL, "
                + "UNIQUE (" + Metal.METAL_ID + " , " + Metal.NAME + ") ON CONFLICT IGNORE );");
        db.execSQL("CREATE TABLE " + Tables.INGOT_PRICE_METAL + " ( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IngotPriceMetal.METAL_ID + " INTEGER NOT NULL , "
                + IngotPriceMetal.NOMINAL + " INTEGER NOT NULL, "
                + IngotPriceMetal.NO_CERTIFICATE_RUBLES + " INTEGER NOT NULL, "
                + IngotPriceMetal.CERTIFICATE_RUBLES + " INTEGER NOT NULL, "
                + IngotPriceMetal.BANKS_DOLLARS + " INTEGER NOT NULL, "
                + IngotPriceMetal.BANKS_RUBLES + " INTEGER NOT NULL, "
                + IngotPriceMetal.ENTITIES_DOLLARS + " INTEGER NOT NULL, "
                + IngotPriceMetal.ENTITIES_RUBLES + " INTEGER NOT NULL, "
                + IngotPriceMetal.INGOT_PRICE_DATE + " TEXT NOT NULL,"
                + "UNIQUE (" + IngotPriceMetal.METAL_ID + " , " + IngotPriceMetal.INGOT_PRICE_DATE + " , " + IngotPriceMetal.NOMINAL
                + ") ON CONFLICT IGNORE );");
        db.execSQL("CREATE TABLE " + Tables.REF_RATE + " ( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RefRate.REF_DATE + " TEXT NOT NULL, "
                + RefRate.REF_VALUE + " TEXT NOT NULL,"
                + "UNIQUE (" + RefRate.REF_DATE + " , " + RefRate.REF_VALUE + ") ON CONFLICT IGNORE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            Log.d(FinancialAssistantDatabase.class.getName(), "new version of DB " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.CURRENCY);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.METAL);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.INGOT_PRICE_METAL);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.REF_RATE);
            onCreate(db);
        }
    }
}
