package com.shurpo.financialassistant.model.processor;

import com.shurpo.financialassistant.utils.PreferenceUtil;

public class ProcessorFactory {

    public static final int CURRENCY_RATE_PROCESSOR = 200;
    public static final int DYNAMIC_PROCESSOR = 300;
    public static final int METAL_PROCESSOR = 400;
    public static final int INGOTS_PRICE_METAL_PROGRESS = 401;
    public static final int REF_RATE_PROCESSOR = 500;

    public static Processor createProcessor(PreferenceUtil preferenceUtil, int action) throws UnsupportedOperationException {
        switch (action) {
            case CURRENCY_RATE_PROCESSOR:
                return new CurrencyProcessor(preferenceUtil);
            case DYNAMIC_PROCESSOR:
                return new DynamicCurrencyProcessor(preferenceUtil);
            case METAL_PROCESSOR:
                return new MetalsProcessor(preferenceUtil);
            case INGOTS_PRICE_METAL_PROGRESS:
                return new IngotPriceMetalProcessor(preferenceUtil);
            case REF_RATE_PROCESSOR:
                return new RefRateProcessor(preferenceUtil);
            default:
                throw new UnsupportedOperationException("No processor was found to match the requested URI. " + action);
        }
    }
}
