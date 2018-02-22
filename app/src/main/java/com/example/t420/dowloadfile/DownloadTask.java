package com.example.t420.dowloadfile;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by t420 on 22-Feb-18.
 */

public class DownloadTask {
    private Context context;
    private Button buttonText;
    private String dowloadUrl="",dowloadFileName="";

    public DownloadTask(Context context, Button buttonText, String dowloadUrl) {
        this.context = context;
        this.buttonText = buttonText;
        this.dowloadUrl = dowloadUrl;
        dowloadFileName =dowloadUrl.replace(Utils.mainUrl,"") ;
        new DowloadingTask().execute();

    }

    private class DowloadingTask extends AsyncTask<Void,Void,Void>{
        File apkStorage = null;
        File outputFile = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonText.setEnabled(false);
            buttonText.setText(R.string.downloadStarted);
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    buttonText.setEnabled(true);
                    buttonText.setText(R.string.downloadCompleted);
                } else {
                    buttonText.setText(R.string.downloadFailed);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonText.setEnabled(true);
                            buttonText.setText(R.string.downloadAgain);
                        }
                    }, 3000);
                }
            }
            catch (Exception e){
                buttonText.setText(R.string.downloadFailed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonText.setEnabled(true);
                        buttonText.setText(R.string.downloadAgain);
                    }
                }, 3000);
            }
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url =new URL(dowloadUrl);
                HttpURLConnection c= (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.connect();

                if(new CheckForSDCard().isSDCardPresent()){
                    apkStorage=new File(Environment.getExternalStorageDirectory()+"/"+Utils.downloadDirectory);
                }else {
                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();
                }

                if(!apkStorage.exists()){
                    apkStorage.mkdir();
                }

                outputFile =new File(apkStorage,dowloadFileName);

                if(!outputFile.exists()){
                    outputFile.createNewFile();
                }

                FileOutputStream fos=new FileOutputStream(outputFile);
                InputStream is=c.getInputStream();

                byte[] buffer=new byte[1024];
                int len1=0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }
                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                outputFile = null;
            }
            return null;
        }

    }
}
