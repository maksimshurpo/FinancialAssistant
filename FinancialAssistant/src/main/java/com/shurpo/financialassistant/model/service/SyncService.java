package com.shurpo.financialassistant.model.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.util.Log;
import com.shurpo.financialassistant.exceptions.ProcessorException;
import com.shurpo.financialassistant.model.processor.ProcessorFactory;
import com.shurpo.financialassistant.model.webservice.RESTExecutor;
import com.shurpo.financialassistant.ui.BaseFragment.*;

public class SyncService extends IntentService {

    private RESTExecutor restExecutor;

    public SyncService() {
        super("Download");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ContentResolver resolver = getContentResolver();
        restExecutor = new RESTExecutor(resolver);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String[] url = intent.getStringArrayExtra(ServiceHelper.API_URL_EXTRA);
        int[] processor = intent.getIntArrayExtra(ServiceHelper.PROCESSOR_EXTRA);
        Intent intentReceiver = new Intent(LoaderReceiver.CURRENCY_RECEIVER);
        intentReceiver.putExtra(LoaderReceiver.EXTRA_LOAD_DATA, loadData(url, processor));
        sendBroadcast(intentReceiver);
    }

    private boolean loadData(String[] url, int[] processor) {
        int countTrue = 0;
        boolean truth = false;
        try {
            if (url.length != processor.length){
                throw new ProcessorException("urls and processors don't equals");
            }
            for (int i = 0; i < url.length; i++){
                boolean isTrue = restExecutor.execute(url[i], ProcessorFactory.createProcessor(processor[i]));
                if (isTrue){
                    countTrue++;
                }
            }
            if (countTrue == url.length){
                truth = true;
            }
        } catch (ProcessorException e) {
            e.printStackTrace();
            truth = false;
        }
        return truth;
    }
}
