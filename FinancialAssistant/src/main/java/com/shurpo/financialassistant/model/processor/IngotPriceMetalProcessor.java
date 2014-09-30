package com.shurpo.financialassistant.model.processor;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract.*;
import com.shurpo.financialassistant.utils.DateUtil;
import com.shurpo.financialassistant.utils.PreferenceUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;

public class IngotPriceMetalProcessor extends Processor {

    private String date;

    public IngotPriceMetalProcessor(PreferenceUtil preferenceUtil) {
        super(preferenceUtil);
    }

    @Override
    protected void execute(XmlPullParser parser, ContentResolver resolver) throws XmlPullParserException, IOException, RemoteException, OperationApplicationException {
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.INGOTS_ELEMENT);
        date =  getDate(parser.getAttributeValue(XMLConstants.DATE_ATTRIBUTE));
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if (name.equals(XMLConstants.INGOTS_PRICES_ELEMENT)){
                resolver.applyBatch(authority, readIngotPriceMetal(parser));
            }else {
                skip(parser);
            }
        }
    }

    private String getDate(String dateParser){
        String newDate = dateParser;
        String currentDate = DateUtil.getCurrentDate();
        if (!dateParser.equals(currentDate)){
            newDate = currentDate;
        }
        return newDate;
    }

    private ArrayList<ContentProviderOperation> readIngotPriceMetal(XmlPullParser parser)throws XmlPullParserException, IOException{
        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(IngotPriceMetal.CONTENT_URI);

        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.INGOTS_PRICES_ELEMENT);
        Integer id = Integer.valueOf(parser.getAttributeValue(XMLConstants.ID_ATTRIBUTE));
        Integer nominal = Integer.valueOf(parser.getAttributeValue(XMLConstants.NOMINAL_ATTRIBUTE));
        Integer noCertificateRubles = null;
        Integer certificateRubles = null;
        Integer banksDollars = null;
        Integer banksRubles = null;
        Integer entitiesDollars = null;
        Integer entitiesRubles = null;
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if (name.equals(XMLConstants.NO_CERTIFICATE_RUBLES_ELEMENT)){
                noCertificateRubles = readNoCertificateRubles(parser);
            }else if (name.equals(XMLConstants.CERTIFICATE_RUBLES_ELEMENT)){
                certificateRubles = readCertificateRubles(parser);
            }else if (name.equals(XMLConstants.BANKS_DOLLARS_ELEMENT)){
                banksDollars = readBanksDollars(parser);
            }else if (name.equals(XMLConstants.BANKS_RUBLES_ELEMENT)){
                banksRubles = readBanksRubles(parser);
            }else if (name.equals(XMLConstants.ENTITIES_DOLLARS_ELEMENT)){
                entitiesDollars = readEntitiesDollars(parser);
            }else if (name.equals(XMLConstants.ENTITIES_RUBLES_ELEMENT)){
                entitiesRubles = readEntitiesRubles(parser);
            }else {
                skip(parser);
            }
        }
        builder.withValue(IngotPriceMetal.INGOT_PRICE_DATE, date);
        builder.withValue(IngotPriceMetal.METAL_ID, id);
        builder.withValue(IngotPriceMetal.NOMINAL, nominal);
        builder.withValue(IngotPriceMetal.NO_CERTIFICATE_RUBLES, noCertificateRubles);
        builder.withValue(IngotPriceMetal.CERTIFICATE_RUBLES, certificateRubles);
        builder.withValue(IngotPriceMetal.BANKS_DOLLARS, banksDollars);
        builder.withValue(IngotPriceMetal.BANKS_RUBLES, banksRubles);
        builder.withValue(IngotPriceMetal.ENTITIES_DOLLARS, entitiesDollars);
        builder.withValue(IngotPriceMetal.ENTITIES_RUBLES, entitiesRubles);
        batch.add(builder.build());
        return batch;
    }

    private Integer readNoCertificateRubles(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.NO_CERTIFICATE_RUBLES_ELEMENT);
        Integer noCertificateRubles = Integer.valueOf(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.NO_CERTIFICATE_RUBLES_ELEMENT);
        return noCertificateRubles;
    }

    private Integer readCertificateRubles(XmlPullParser parser)throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.CERTIFICATE_RUBLES_ELEMENT);
        Integer certificateRubles = Integer.valueOf(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.CERTIFICATE_RUBLES_ELEMENT);
        return certificateRubles;
    }

    private Integer readBanksDollars(XmlPullParser parser)throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.BANKS_DOLLARS_ELEMENT);
        Integer banksDollars = Integer.valueOf(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.BANKS_DOLLARS_ELEMENT);
        return banksDollars;
    }

    private Integer readBanksRubles(XmlPullParser parser)throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.BANKS_RUBLES_ELEMENT);
        Integer banksRubles = Integer.valueOf(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.BANKS_RUBLES_ELEMENT);
        return banksRubles;
    }

    private Integer readEntitiesDollars(XmlPullParser parser)throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.ENTITIES_DOLLARS_ELEMENT);
        Integer entitiesDollars = Integer.valueOf(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.ENTITIES_DOLLARS_ELEMENT);
        return entitiesDollars;
    }

    private Integer readEntitiesRubles(XmlPullParser parser)throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.ENTITIES_RUBLES_ELEMENT);
        Integer entitiesRubles = Integer.valueOf(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.ENTITIES_RUBLES_ELEMENT);
        return entitiesRubles;
    }
}
