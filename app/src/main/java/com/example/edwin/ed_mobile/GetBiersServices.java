package com.example.edwin.ed_mobile;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GetBiersServices extends IntentService {

    public static final String ACTION_DOWNLOAD = "com.example.edwin.ed_mobile.action.download";
    public static final String EXTRA_MESSAGE = "com.example.edwin.ed_mobile.action.extra.message";
    //public static String urlString = "http://binouze.fabrigli.fr/bieres.json";
    public static String urlString = "https://topfilm.herokuapp.com/cinemas.json";

    public GetBiersServices() {
        super("GetBiersServices");
        //String urlString = "http://binouze.fabrigli.fr/bieres.json";
        String urlString = "https://topfilm.herokuapp.com/cinemas.json";
    }


    /*public static void startActionBiers(Context context) {
        Intent intent = new Intent(context, GetBiersServices.class);
        intent.setAction(ACTION_DOWNLOAD);
        context.startService(intent);
    }*/


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD.equals(action)) {
                handleActionBiers();
            }
        }
    }


    private void handleActionBiers() {
        Log.i("GetBiersServices",  "Thread Service name:"+ Thread.currentThread().getName());
        URL url = null;
        String message="Download failed.";
        //String urlString = "http://binouze.fabrigli.fr/bieres.json";
        try {
            url = new URL(urlString);
            FileOutputStream fos = null;
            InputStream is = null;
            //url = new URL("http://www.cleverfiles.com/howto/wp-content/uploads/2016/08/mini.jpg");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setRequestMethod("GET");
            //conn.connect();
            is=conn.getInputStream();
            String fileName = urlString.substring(urlString.lastIndexOf('/') + 1);
            fos=new FileOutputStream(Environment.getExternalStorageDirectory()+"/"+fileName);

            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                copyInputStreamToFile(is, fos);
                Log.i("GetBiersServices",  "Downloaded !");
                message="Download completed";
            }
            // LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SecondeActivity.BIERS_UPDATE));
            Intent backIntent = new Intent(GetBiersServices.ACTION_DOWNLOAD);
            backIntent.putExtra(GetBiersServices.EXTRA_MESSAGE, message);
            sendBroadcast(backIntent);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    private void copyInputStreamToFile(InputStream in, FileOutputStream out) {
        try{
            //OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
