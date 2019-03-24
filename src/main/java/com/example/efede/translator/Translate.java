package com.example.efede.translator;

import android.content.Context;
import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

public class Translate {
    //Function for calling executing the Translator Background Task
    public static String Translate(String source,String language,Context context){
        TranslateApi translateApi = new TranslateApi(context);
        AsyncTask<String, Void, String> target = translateApi.execute(source,language);


        try {
            return String.valueOf(target.get());
        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return null;

    }

}
