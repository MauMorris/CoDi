package com.example.codi.servicios.codi.repositories;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.codi.constants.Constants;
import com.example.codi.crypto.CipherData;
import com.example.codi.crypto.Utils;
import com.example.codi.servicios.codi.callback.MyRequestCallback;
import com.example.codi.servicios.codi.request.GetServiceData;
import com.example.codi.servicios.codi.response.objects.ResponseRegistroSubsecuente;
import com.example.codi.servicios.codi.response.objects.ResponseValidaCuenta;
import com.google.gson.Gson;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class ValidaCuentaDatos implements MyRequestCallback {
    private static final String LOG_TAG = ValidaCuentaDatos.class.getSimpleName();

    private Context regIniContext;
    private Activity regIniActivity;
    private String hmac;

    public ValidaCuentaDatos(Context mContext, Activity mActivity) {
        this.regIniContext = mContext;
        this.regIniActivity = mActivity;
    }

    public void setValidaCuenta() {

        String cb = Constants.clabe;
        String tc = Constants.TIPO_CLABE;
        String ci = Constants.CODIGO_BANCO;

        saveDataRegIni(cb, tc, ci);

        this.hmac = generateHmac();
    }

    private void saveDataRegIni(String cb, String tc, String ci) {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorCodi = prefs.edit();

        editorCodi.putString(Constants.CB, cb);
        editorCodi.putString(Constants.TC, tc);
        editorCodi.putString(Constants.CI, ci);

        editorCodi.apply();

        logDataRegIni(cb, tc, ci);
    }

    private void logDataRegIni(String cb, String tc, String ci) {
        Log.v(LOG_TAG, cb);
        Log.v(LOG_TAG, tc);
        Log.v(LOG_TAG, ci);
    }

    private String generateHmac() {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);

        String keysource = prefs.getString(Constants.KEYSOURCE, Constants.KEYSOURCE_DEF);
        String nc = prefs.getString(Constants.CELULAR, Constants.CELULAR_DEF);
        String dv = prefs.getString(Constants.DV, Constants.DV_DEF);
        String cb = prefs.getString(Constants.CB, Constants.CB_DEF);
        String tc = prefs.getString(Constants.TC, Constants.TC_DEF);
        String ci = prefs.getString(Constants.CI, Constants.CI_DEF);

        StringBuilder newDv;

        if (dv != null && dv.length() < 2) {
            newDv = new StringBuilder("00");
            newDv.append(dv);
        } else if (dv != null && dv.length() < 3) {
            newDv = new StringBuilder("0");
            newDv.append(dv);
        } else {
            newDv = new StringBuilder();
            newDv.append(dv);
        }

        String contentHmac = nc + newDv + cb + tc + ci;
        Log.v(LOG_TAG, contentHmac);
        String hmacValidaCuenta = null;
        try {
            if (keysource != null) {
                hmacValidaCuenta = CipherData.HMACEncode(Utils.getKeyHmac(keysource), contentHmac.getBytes());
                hmacValidaCuenta = hmacValidaCuenta.replace("\n", "");
                Log.v(LOG_TAG, hmacValidaCuenta);
                Log.v(LOG_TAG, Integer.toString(hmacValidaCuenta.length()));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return hmacValidaCuenta;
    }

    public String getJsonRequestValidaCuenta() {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);

        String nc = prefs.getString(Constants.CELULAR, Constants.CELULAR_DEF);
        String dv = prefs.getString(Constants.DV, Constants.DV_DEF);
        String cb = prefs.getString(Constants.CB, Constants.CB_DEF);
        String tc = prefs.getString(Constants.TC, Constants.TC_DEF);
        String ci = prefs.getString(Constants.CI, Constants.CI_DEF);

        String dataToReturn = "d={\"cb\" : \"" + cb + "\"," +
                "\"tc\" : \"" + tc + "\"," +
                "\"ci\" : \"" + ci + "\"," +
                "\"ds\" : {" +
                "\"nc\" : \"" + nc + "\"," +
                "\"dv\" : " + dv +
                "}," +
                "\"hmac\" : \"" + hmac + "\"" + "}";

        Log.v(LOG_TAG, dataToReturn);
        return dataToReturn;
    }

    public MutableLiveData<String> getServiceDataValidaCuenta() {
        MutableLiveData<String> dataString;

        String url = Constants.urlValidaCuenta;
        String data = getJsonRequestValidaCuenta();

        HashMap<String, String> params = new HashMap<>();
        params.put("content-Type", "text/plain");

        GetServiceData validaCuentaRequest = new GetServiceData(data, url, "POST", params, regIniActivity, this);
        dataString = validaCuentaRequest.doRequest();

        return dataString;
    }

    @Override
    public void finishedCallback(String dataString) {
        Gson gson = new Gson();
        ResponseValidaCuenta dataResponse = gson.fromJson(dataString, ResponseValidaCuenta.class);

        saveResponseRegSub(dataResponse);
        Toast.makeText(regIniContext, dataString, Toast.LENGTH_LONG).show();
    }

    private void saveResponseRegSub(ResponseValidaCuenta dataResponse) {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorCodi = prefs.edit();

        editorCodi.putString(Constants.CR, dataResponse.getCr());
        editorCodi.apply();
    }
}