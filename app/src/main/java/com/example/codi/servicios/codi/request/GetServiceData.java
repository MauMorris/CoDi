package com.example.codi.servicios.codi.request;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.example.codi.servicios.codi.callback.MyRequestCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GetServiceData {
    public static final String LOG_TAG = GetServiceData.class.getSimpleName();

    private String payload;
    private String url;
    private String typeRequest;
    private HashMap headers;
    private Activity activity;
    private MyRequestCallback mRequest;

    public GetServiceData(String payload, String url, String typeRequest, HashMap headers, Activity activity, MyRequestCallback myRequestCallback) {
        this.payload = payload;
        this.url = url;
        this.typeRequest = typeRequest;
        this.headers = headers;
        this.activity = activity;
        this.mRequest = myRequestCallback;
    }

    public MutableLiveData<String> doRequest(){
        String url = this.url;
        final String data = this.payload;
        int method;
        final MutableLiveData<String> dataString = new MutableLiveData<>();

        if(typeRequest.equals("POST"))
            method = Request.Method.POST;
        else
            method = Request.Method.GET;

        final StringRequest jsonobj = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v(LOG_TAG, response);
                        dataString.setValue(response);
                        mRequest.finishedCallback(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v(LOG_TAG, error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
            @Override
            public byte[] getBody() {
                return data == null ? null : data.getBytes(StandardCharsets.UTF_8);
            }
        };
        jsonobj.setRetryPolicy(new DefaultRetryPolicy( 15000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Cache cache = new DiskBasedCache(activity.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        final RequestQueue requestQueue = new RequestQueue(cache, network);
        // Start the queue
        requestQueue.start();
        requestQueue.add(jsonobj);

        return dataString;
    }
}
