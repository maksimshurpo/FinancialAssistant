package com.shurpo.financialassistant.model.service;


import android.content.Context;
import android.content.Intent;
import com.shurpo.financialassistant.utils.WebRequestUtil;

public class ServiceHelper {

    public static final String API_URL_EXTRA = "API_URL_EXTRA";
    public static final String PROCESSOR_EXTRA = "PROCESSOR_EXTRA";

    private static Object lock = new Object();
    private static ServiceHelper instance;
    private Context context;

    private ServiceHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    public static ServiceHelper getInstance(Context context) {
        synchronized (lock) {
            if (instance == null) {
                instance = new ServiceHelper(context);
            }
        }
        return instance;
    }

    public void execute(WebRequestUtil webRequest){
        Intent syncIntent = new Intent(Intent.ACTION_SYNC, null, context, SyncService.class);
        syncIntent.putExtra(API_URL_EXTRA, webRequest.getUrls());
        syncIntent.putExtra(PROCESSOR_EXTRA, webRequest.getProgresses());
        context.startService(syncIntent);
    }
}
