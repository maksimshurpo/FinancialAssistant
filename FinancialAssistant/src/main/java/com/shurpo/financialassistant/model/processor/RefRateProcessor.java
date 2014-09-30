package com.shurpo.financialassistant.model.processor;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract.*;
import com.shurpo.financialassistant.utils.PreferenceUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;

public class RefRateProcessor extends Processor {

    public RefRateProcessor(PreferenceUtil preferenceUtil) {
        super(preferenceUtil);
    }

    @Override
    protected void execute(XmlPullParser parser, ContentResolver resolver) throws XmlPullParserException, IOException, RemoteException, OperationApplicationException {
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.REF_RATE_ELEMENT);
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals(XMLConstants.ITEM_ELEMENT)){
                resolver.applyBatch(authority, readRefRate(parser));
            }else {
                skip(parser);
            }
        }
    }

    private ArrayList<ContentProviderOperation> readRefRate(XmlPullParser parser) throws XmlPullParserException, IOException{
        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.ITEM_ELEMENT);
        String date = null;
        Double value = null;
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if (name.equals(XMLConstants.DATE_ELEMENT)){
                date = readDate(parser);
            }else if (name.equals(XMLConstants.VALUE_ELEMENT)){
                value = readValue(parser);
            }else {
                skip(parser);
            }
        }
        batch.add(ContentProviderOperation
                .newInsert(RefRate.CONTENT_URI)
                .withValue(RefRate.REF_DATE, date)
                .withValue(RefRate.REF_VALUE, value).build());
        return batch;
    }

    private String readDate(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.DATE_ELEMENT);
        String date = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.DATE_ELEMENT);
        return date;
    }

    private Double readValue(XmlPullParser parser)throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.VALUE_ELEMENT);
        Double value = Double.valueOf(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.VALUE_ELEMENT);
        return value;
    }
}
