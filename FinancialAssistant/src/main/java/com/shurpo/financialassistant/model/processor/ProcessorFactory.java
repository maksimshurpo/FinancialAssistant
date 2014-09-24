package com.shurpo.financialassistant.model.processor;

import com.shurpo.financialassistant.utils.WebRequestUtil;

public class ProcessorFactory {
    public static Processor createProcessor(int action) throws UnsupportedOperationException {
        switch (action) {
            case WebRequestUtil.CURRENCY_RATE_PROCESSOR:
                return new CurrencyAndInfoProcessor();
            case WebRequestUtil.HISTORY_CURRENCY_RATE_PROCESSOR:
                return new CurrencyAndInfoProcessor();
            case WebRequestUtil.DYNAMIC_PROCESSOR:
                return new DynamicCurrencyProcessor();
            case WebRequestUtil.METAL_PROCESSOR:
                return new MetalsProcessor();
            case WebRequestUtil.INGOTS_PRICE_METAL_PROGRESS:
                return new IngotPriceMetalProcessor();
            case WebRequestUtil.REF_RATE_PROCESSOR:
                return new RefRateProcessor();
            default:
                throw new UnsupportedOperationException("No processor was found to match the requested URI. " + action);
        }
    }
}
