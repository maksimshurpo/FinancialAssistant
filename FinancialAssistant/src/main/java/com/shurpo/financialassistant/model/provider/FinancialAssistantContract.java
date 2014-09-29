package com.shurpo.financialassistant.model.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class FinancialAssistantContract {
    private FinancialAssistantContract() {
    }

    public interface Tables {
        String CURRENCY = "currency";
        String METAL = "metal";
        String INGOT_PRICE_METAL = "ingot_price_metal";
        String REF_RATE = "ref_rate";

        String META_JOIN_INGOT_PRICE_METAL = METAL + " inner join " + INGOT_PRICE_METAL + " on "
                + METAL + "." + MetalColumns.METAL_ID + " = " + INGOT_PRICE_METAL + "." + IngotPriceMetalColumns.METAL_ID;
    }

    public interface CurrencyColumns {
        String CURRENCY_ID = "currency_id";
        String CURRENCY_DATE = "currency_date";
        String NUM_CODE = "num_code";
        String CHAR_CODE = "char_code";
        String SCALE = "scale";
        String NAME = "name";
        String RATE = "rate";
        String FAVOURITE = "favourite";
    }

    public interface MetalColumns {
        String METAL_ID = "metal_id";
        String NAME = "name";
        String FAVOURITE = "favourite";
    }

    public interface IngotPriceMetalColumns {
        String METAL_ID = "metal_id";
        String NOMINAL = "nominal";
        String NO_CERTIFICATE_RUBLES = "no_certificate_rubles";
        String CERTIFICATE_RUBLES = "certificate_rubles";
        String BANKS_DOLLARS = "banks_dollars";
        String BANKS_RUBLES = "banks_rubles";
        String ENTITIES_DOLLARS = "entities_dollars";
        String ENTITIES_RUBLES = "entities_rubles";
        String INGOT_PRICE_DATE = "ingot_price_date";
    }

    public interface RefRateColumns{
        String REF_DATE = "ref_date";
        String REF_VALUE = "ref_value";
    }

    public static final String CONTENT_AUTHORITY = "com.shurpo.financialassistant.model.provider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CURRENCY = "currency";
    public static final String PATH_METAL = "metal";
    public static final String PATH_METAL_AND_INGOT_PRICE_METAL = "metal_and_ingot_price_metal";
    public static final String PATH_INGOT_PRICE_METAL = "ingot_price_metal";
    public static final String PATH_REF_RATE = "ref_rate";

    public static class Currency implements CurrencyColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CURRENCY).build();
    }

    public static class Metal implements MetalColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_METAL).build();
    }
    public static class MetalAndIngotPriceMetal implements MetalColumns, IngotPriceMetalColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_METAL_AND_INGOT_PRICE_METAL).build();
    }

    public static class IngotPriceMetal implements IngotPriceMetalColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGOT_PRICE_METAL).build();
    }

    public static class RefRate implements RefRateColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REF_RATE).build();
    }
}
