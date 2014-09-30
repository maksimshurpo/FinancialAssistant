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

public class MetalsProcessor extends Processor {

    public MetalsProcessor(PreferenceUtil preferenceUtil) {
        super(preferenceUtil);
    }

    @Override
    protected void execute(XmlPullParser parser, ContentResolver resolver) throws XmlPullParserException, IOException, RemoteException, OperationApplicationException {
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.METALS_ELEMENT);
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if (name.equals(XMLConstants.METAL_ELEMENT)){
                resolver.applyBatch(authority, readMetal(parser));
            }else {
                skip(parser);
            }
        }
    }

    private ArrayList<ContentProviderOperation> readMetal(XmlPullParser parser)throws XmlPullParserException, IOException{
        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(Metal.CONTENT_URI);
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.METAL_ELEMENT);
        Integer id = Integer.valueOf(parser.getAttributeValue(XMLConstants.ID_ATTRIBUTE));
        String nameMetal = null;
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if (name.equals(XMLConstants.NAME_ELEMENT)){
                nameMetal = readName(parser);
            }else {
                skip(parser);
            }
        }
        builder.withValue(Metal.METAL_ID, id);
        builder.withValue(Metal.NAME, nameMetal);
        builder.withValue(Metal.FAVOURITE, XMLConstants.FAVOURITE);
        batch.add(builder.build());
        return batch;
    }

    private String readName(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, XMLConstants.NAME_ELEMENT);
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, XMLConstants.NAME_ELEMENT);
        return name;
    }
}
