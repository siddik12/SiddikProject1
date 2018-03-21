package com.rockscoder.easyphotofilters.utils;

import android.util.Log;

import com.rockscoder.easyphotofilters.model.Param;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Siddiqur on 3/21/2018.
 */

public class HttpClient {

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .build();

    public JSONObject makeHttpRequestPost(String url, ArrayList<Param> params) {
        String json = "";
        int i = 0;
        while (i < params.size()){
            try {
                params.get(i).setValue(params.get(i).getValue().replace("\"","\\\"").replace("'", "\\'"));
                i++;
            }catch (Exception e){
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
        }

        try {
            json = new HttpClient().run(url,params);
        } catch (IOException e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            return null;
        }

    }


    private String run(String url, ArrayList<Param> params) throws IOException {
        FormBody.Builder formBody = new FormBody.Builder();
        for (int i = 0; i < params.size(); i++) {
            formBody.add(params.get(i).getName(),params.get(i).getValue());
        }
        return this.client.newCall(new Request.Builder().url(url).post(formBody.build()).build()).execute().body().string();
    }

}
