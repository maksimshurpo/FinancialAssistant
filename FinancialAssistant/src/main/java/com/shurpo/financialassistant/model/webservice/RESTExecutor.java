package com.shurpo.financialassistant.model.webservice;

import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import com.shurpo.financialassistant.exceptions.ProcessorException;
import com.shurpo.financialassistant.model.processor.Processor;
import com.shurpo.financialassistant.model.provider.FinancialAssistantContract;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RESTExecutor implements Executor{

    private ContentResolver resolver;

    public RESTExecutor(ContentResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public boolean execute(String urlString, Processor processor) throws ProcessorException {
        boolean isConnected = false;
        try {

            Request request = new Request.Builder().url(urlString).build();
            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            Response response = call.execute();/*
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();*/

            InputStream stream = response.body().byteStream();
            processor.parse(stream, resolver, FinancialAssistantContract.CONTENT_AUTHORITY);
            isConnected = true;
        } catch (ProtocolException e) {
            throw new ProcessorException(e.getMessage());
        } catch (MalformedURLException e) {
            throw new ProcessorException("incorrect url : " + e.getMessage());
        } catch (IOException e) {
            throw new ProcessorException("disconnect HttpUrlConnection : " + e.getMessage());
        /*} catch (XmlPullParserException e) {
            throw new ProcessorException("incorrect xml parser : " + e.getMessage());*/
        } catch (Exception e) {
            throw new ProcessorException("InputStream has error : " + e.getMessage());
        }
        return isConnected;
    }

}
