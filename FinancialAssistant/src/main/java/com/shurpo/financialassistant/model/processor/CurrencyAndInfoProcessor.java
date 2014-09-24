package com.shurpo.financialassistant.model.processor;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class CurrencyAndInfoProcessor extends Processor {

    private ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
    private String currencyDate;

    @Override
    protected void execute(XmlPullParser parser, ContentResolver resolver) throws XmlPullParserException, IOException, RemoteException, OperationApplicationException {
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.DAILY_EX_RATES_ELEMENT);
        currencyDate = parser.getAttributeValue(XMLConstants.DATE_ATTRIBUTE);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(XMLConstants.CURRENCY_ELEMENT)) {
                readParser(parser);
            } else {
                skip(parser);
            }
        }
        resolver.applyBatch(authority, batch);
    }

    private void readParser(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.CURRENCY_ELEMENT);
        String numCode = null;
        Integer scale = null;
        String charCode = null;
        String nameCurrency = null;
        Double rate = null;
        /*value of id*/
        int currencyId = Integer.valueOf(parser.getAttributeValue(XMLConstants.ID_ATTRIBUTE));
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(XMLConstants.NUM_CODE_ELEMENT)) {
                numCode = readNumCode(parser);
            } else if (name.equals(XMLConstants.SCALE_ELEMENT)) {
                scale = readScale(parser);
            } else if (name.equals(XMLConstants.CHAR_CODE_ELEMENT)) {
                charCode = readCharCode(parser);
            } else if (name.equals(XMLConstants.NAME_ELEMENT)) {
                nameCurrency = readName(parser);
            } else if (name.equals(XMLConstants.RATE_ELEMENT)) {
                rate = readRate(parser);
            } else {
                skip(parser);
            }
        }

        batch.add(ContentProviderOperation.newInsert(Currency.CONTENT_URI)
                .withValue(Currency.CURRENCY_ID, currencyId)
                .withValue(Currency.NUM_CODE, numCode)
                .withValue(Currency.SCALE, scale)
                .withValue(Currency.CHAR_CODE, charCode)
                .withValue(Currency.NAME, nameCurrency)
                .withValue(Currency.FAVOURITE, XMLConstants.FAVOURITE).build());

        batch.add(ContentProviderOperation.newInsert(CurrencyInfo.CONTENT_URI)
                .withValue(CurrencyInfo.CURRENCY_ID, currencyId)
                .withValue(CurrencyInfo.CURRENCY_DATE, currencyDate)
                .withValue(CurrencyInfo.RATE, rate).build());
    }

    private String readNumCode(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.NUM_CODE_ELEMENT);
        String numCode = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.NUM_CODE_ELEMENT);
        return numCode;
    }

    private int readScale(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.SCALE_ELEMENT);
        int scale = Integer.valueOf(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.SCALE_ELEMENT);
        return scale;
    }

    private String readCharCode(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.CHAR_CODE_ELEMENT);
        String charCode = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.CHAR_CODE_ELEMENT);
        return charCode;
    }

    private String readName(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.NAME_ELEMENT);
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.NAME_ELEMENT);
        return name;
    }

    private Double readRate(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.RATE_ELEMENT);
        Double rate = Double.valueOf(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.RATE_ELEMENT);
        return rate;
    }
}
