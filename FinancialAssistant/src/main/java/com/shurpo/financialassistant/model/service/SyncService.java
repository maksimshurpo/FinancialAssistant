package com.shurpo.financialassistant.model.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.util.Log;

import com.shurpo.financialassistant.exceptions.ProcessorException;
import com.shurpo.financialassistant.model.processor.ProcessorFactory;
import com.shurpo.financialassistant.ui.BaseFragment.*;
import com.shurpo.financialassistant.utils.PreferenceUtil;

public class SyncService extends IntentService {
    private String LOG_TAG = getClass().getName();

    private TransportHttp transportHttp;

    public SyncService() {
        super("Download");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ContentResolver resolver = getContentResolver();
        transportHttp = new TransportHttp(resolver);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String[] url = intent.getStringArrayExtra(ServiceHelper.API_URL_EXTRA);
        int[] processor = intent.getIntArrayExtra(ServiceHelper.PROCESSOR_EXTRA);

        Intent intentReceiver = new Intent(LoaderInformationReceiver.CURRENCY_RECEIVER);
        intentReceiver.putExtra(LoaderInformationReceiver.EXTRA_LOAD_DATA, loadData(url, processor));
        sendBroadcast(intentReceiver);
    }

    /**
     * if all request executed then the method will return true else false.
     * */
    private boolean loadData(String[] url, int[] processor) {
        int countTrue = 0;
        boolean truth = false;
        try {
            if (url.length != processor.length){
                throw new ProcessorException("urls and processors don't equals");
            }
            for (int i = 0; i < url.length; i++){
                boolean isTrue = transportHttp.execute(url[i], ProcessorFactory.createProcessor(new PreferenceUtil(getApplicationContext()), processor[i]));
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
        Log.d(LOG_TAG, "loading of date is " + truth);
        return truth;
    }
}
