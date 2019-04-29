package com.example.codi.servicios.codi.repositories;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.codi.constants.Constants;
import com.example.codi.servicios.codi.callback.MyRequestCallback;
import com.example.codi.servicios.codi.request.GetServiceData;

import java.util.HashMap;

public class ConsultaLlavesDatos implements MyRequestCallback {
    private static final String LOG_TAG = ConsultaLlavesDatos.class.getSimpleName();

    private Context regIniContext;
    private Activity regIniActivity;
    private String hmac;

    public ConsultaLlavesDatos(Context mContext, Activity mActivity) {
        this.regIniContext = mContext;
        this.regIniActivity = mActivity;
    }

    public void setConsultaLlaves() {

        String cr = Constants.CLAVE_RASTREO;

        saveDataConsultaLlaves(cr);

        this.hmac = generateHmac();
    }

    private void saveDataConsultaLlaves(String cr) {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorCodi = prefs.edit();

        editorCodi.putString(Constants.CR, cr);

        editorCodi.apply();

        logDataConsultaLlaves(cr);
    }

    private void logDataConsultaLlaves(String cr) {
        Log.v(LOG_TAG, cr);
    }

    private String generateHmac() {
        return "";
    }


    public String getJsonRequestConsultaLlaves() {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);

        String nc = prefs.getString(Constants.CELULAR, Constants.CELULAR_DEF);
        String dv = prefs.getString(Constants.DV, Constants.DV_DEF);
        String cr = prefs.getString(Constants.CR, Constants.CR_DEF);

        String hmac = this.hmac;

        String dataToReturn = "d={\"tipo\" : \"" + "1" + "\"," +
                "\"v\" : {" +
                "\"nc\" : \"" + nc + "\"," +
                "\"dv\" : \"" + dv + "\"" +
                "}," +
                "\"c\" : {" +
                "\"nc\" : \"" + nc + "\"," +
                "\"dv\" : \"" + dv + "\"" +
                "}," +
                "\"ic\" : {" +
                "\"id\" : \"" + nc + "\"," +
                "\"s\" : \"" + nc + "\"," +
                "\"mc\" : \"" + dv + "\"" +
                "}," +
                "\"hmac\" : \"" + hmac + "\"" +
                "}";

        Log.v(LOG_TAG, dataToReturn);
        return dataToReturn;
    }

    public MutableLiveData<String> getServiceDataConsultaLlaves() {
        MutableLiveData<String> dataString;

        String url = Constants.urlRegIni;
        String data = getJsonRequestConsultaLlaves();

        HashMap<String, String> params = new HashMap<>();
        params.put("content-Type", "text/plain");

        GetServiceData validaCuentaRequest = new GetServiceData(data, url, "POST",params , regIniActivity, this);
        dataString = validaCuentaRequest.doRequest();

        return dataString;
    }

    @Override
    public void finishedCallback(String dataString) {

    }
}