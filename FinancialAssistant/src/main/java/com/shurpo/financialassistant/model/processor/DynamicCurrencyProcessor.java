package com.shurpo.financialassistant.model.processor;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;

public class DynamicCurrencyProcessor extends Processor {

    private ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
    private String currencyId;

    @Override
    protected void execute(XmlPullParser parser, ContentResolver resolver) throws XmlPullParserException, IOException, RemoteException, OperationApplicationException {
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.DYNAMIC_CURRENCY_ELEMENT);
      //  batch.add(ContentProviderOperation.newDelete(CurrencyInfo.CONTENT_URI).build());
        currencyId = parser.getAttributeValue(XMLConstants.ID_ATTRIBUTE);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(XMLConstants.DYNAMIC_RECORD_ELEMENT)) {
                readParser(parser);
            }else {
                skip(parser);
            }
        }
        resolver.applyBatch(authority, batch);
    }

    private void readParser(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.DYNAMIC_RECORD_ELEMENT);
        String date = parser.getAttributeValue(XMLConstants.DATE_ATTRIBUTE);
        String rate = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(XMLConstants.DYNAMIC_RATE_ELEMENT)) {
                rate = readRate(parser);
            } else {
                skip(parser);
            }
        }
        Log.d("qwert", "Currency_id = " + currencyId + ", currency_date = " + date + ", rate =  " + rate);
       /* batch.add(ContentProviderOperation.newInsert(CurrencyInfo.CONTENT_URI)
                .withValue(CurrencyInfo.CURRENCY_ID, currencyId)
                .withValue(CurrencyInfo.CURRENCY_DATE, date)
                .withValue(CurrencyInfo.RATE, rate).build());*/

    }

    private String readRate(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.DYNAMIC_RATE_ELEMENT);
        String rate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.DYNAMIC_RATE_ELEMENT);
        return rate;
    }
}
