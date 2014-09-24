package com.shurpo.financialassistant.model.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract.*;

public class FinancialAssistantDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "assistant.db";
    private static final int DATABASE_VERSION = 1;


    public interface Tables {
        String CURRENCY = "currency";
        String CURRENCY_INFO = "currency_info";
        String METAL = "metal";
        String INGOT_PRICE_METAL = "ingot_price_metal";
        String REF_RATE = "ref_rate";

        String CURRENCY_JOIN_CURRENCY_INFO = "currency INNER JOIN currency_info ON "
                + "currency.currency_id = currency_info.currency_id";

        String META_JOIN_INGOT_PRICE_METAL = "metal INNER JOIN ingot_price_metal ON "
                + "metal.metal_id = ingot_price_metal.metal_id";
    }

    public FinancialAssistantDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.CURRENCY + " ( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Currency.CURRENCY_ID + " INTEGER NOT NULL, "
                + Currency.NUM_CODE + " INTEGER NOT NULL, "
                + Currency.CHAR_CODE + " TEXT NOT NULL, "
                + Currency.SCALE + " INTEGER NOT NULL, "
                + Currency.NAME + " TEXT NOT NULL, "
                + Currency.FAVOURITE + " INTEGER NOT NULL, "
                + "UNIQUE (" + Currency.CURRENCY_ID + ") ON CONFLICT IGNORE );");
        db.execSQL("CREATE TABLE " + Tables.CURRENCY_INFO + " ( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CurrencyInfo.CURRENCY_ID + " INTEGER NOT NULL , "
                + CurrencyInfo.CURRENCY_DATE + " TEXT NOT NULL , "
                + CurrencyInfo.RATE + " REAL NOT NULL, "
                + "UNIQUE (" + CurrencyInfo.CURRENCY_ID + " , " + CurrencyInfo.CURRENCY_DATE + ") ON CONFLICT IGNORE );");
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
            db.execSQL("DROP TABLE IF EXISTS " + Tables.CURRENCY_INFO);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.METAL);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.INGOT_PRICE_METAL);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.REF_RATE);
            onCreate(db);
        }
    }
}
