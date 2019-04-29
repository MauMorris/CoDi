package com.example.codi.servicios.codi.repositories;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.codi.constants.Constants;
import com.example.codi.crypto.CipherData;
import com.example.codi.crypto.Utils;
import com.example.codi.servicios.codi.callback.MyRequestCallback;
import com.example.codi.servicios.codi.request.GetServiceData;
import com.example.codi.servicios.codi.response.objects.ResponseRegistroSubsecuente;
import com.google.gson.Gson;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class RegistroSubsecuenteDatos implements MyRequestCallback {
    private static final String LOG_TAG = RegistroSubsecuenteDatos.class.getSimpleName();

    private Context regIniContext;
    private Activity regIniActivity;
    private String hmac;


    public RegistroSubsecuenteDatos(Context regIniContext, Activity regIniActivity) {
        this.regIniContext = regIniContext;
        this.regIniActivity = regIniActivity;
    }

    public void setRegSub() {
        this.hmac = generateHmac();
    }

    private String generateHmac() {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);

        String keysource = prefs.getString(Constants.KEYSOURCE, Constants.KEYSOURCE_DEF);
        String nc = prefs.getString(Constants.CELULAR, Constants.CELULAR_DEF);
        String dv = prefs.getString(Constants.DV, Constants.DV_DEF);
        String idN = prefs.getString(Constants.ID_NOTIFICATIONS, Constants.ID_NOTIFICATIONS_DEF);

        StringBuilder newDv;

        if (dv != null && dv.length() < 2){
            newDv = new StringBuilder("00");
            newDv.append(dv);
        } else if(dv != null && dv.length() <3){
            newDv = new StringBuilder("0");
            newDv.append(dv);
        } else{
            newDv = new StringBuilder();
            newDv.append(dv);
        }

        String contentHmac = nc + newDv + idN;
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

    public String getJsonRequestRegSub() {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);

        String nc = prefs.getString(Constants.CELULAR, Constants.CELULAR_DEF);
        String dv = prefs.getString(Constants.DV, Constants.DV_DEF);
        String idH = prefs.getString(Constants.ID_HARDWARE, Constants.ID_HARDWARE_DEF);
        String os = prefs.getString(Constants.SISTEMA_OPERATIVO, Constants.SISTEMA_OPERATIVO_DEF);
        String vOs = prefs.getString(Constants.OS_VERSION, Constants.OS_VERSION_DEF);
        String fab = prefs.getString(Constants.FABRICANTE, Constants.FABRICANTE_DEF);
        String mod = prefs.getString(Constants.MODELO, Constants.MODELO_DEF);
        String idN = prefs.getString(Constants.ID_NOTIFICATIONS, Constants.ID_NOTIFICATIONS_DEF);
        String hmac = this.hmac;

        String dataToReturn = "d={\"nc\" : \"" + nc + "\"," +
                "\"dv\" : " + dv + "," +
                "\"idH\" : \"" + idH + "\"," +
                "\"ia\" : {" +
                "\"so\" : \"" + os + "\"," +
                "\"vSO\" : \"" + vOs + "\"," +
                "\"fab\" : \"" + fab + "\"," +
                "\"mod\" : \"" + mod + "\"" +
                "}," +
                "\"idN\" : \"" + idN + "\"," +
                "\"hmac\" : \"" + hmac + "\"" +
                "}";

        Log.v(LOG_TAG, dataToReturn);
        return dataToReturn;
    }

    public LiveData<String> getServiceDataRegSub() {
        String url = Constants.urlRegSub;
        final String data = getJsonRequestRegSub();

        MutableLiveData<String> dataString;

        HashMap<String, String> params = new HashMap<>();
        params.put("content-Type", "text/plain");

        GetServiceData validaCuentaRequest = new GetServiceData(data, url, "POST",params , regIniActivity, this);
        dataString = validaCuentaRequest.doRequest();

        return dataString;
    }

    @Override
    public void finishedCallback(String dataString) {
        Gson gson = new Gson();
        ResponseRegistroSubsecuente dataResponse = gson.fromJson(dataString, ResponseRegistroSubsecuente.class);

        saveResponseRegSub(dataResponse);
        Toast.makeText(regIniContext, dataString, Toast.LENGTH_LONG).show();
    }

    private void saveResponseRegSub(ResponseRegistroSubsecuente dataResponse) {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorCodi = prefs.edit();

        editorCodi.putString(Constants.DV_OMISION, dataResponse.getDvOmision());
        editorCodi.apply();

        logResponseRegSub(dataResponse);
    }

    private void logResponseRegSub(ResponseRegistroSubsecuente dataResponse) {
            Log.v(LOG_TAG, dataResponse.getDvOmision());
            Log.v(LOG_TAG, dataResponse.getDv());
            Log.v(LOG_TAG, dataResponse.getEdoPet());
    }
}