package com.example.codi.servicios.codi.repositories;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.codi.constants.Constants;
import com.example.codi.servicios.codi.callback.MyRequestCallback;
import com.example.codi.servicios.codi.request.GetServiceData;
import com.example.codi.servicios.codi.response.objects.ResponseRegistroInicial;
import com.google.gson.Gson;

import java.util.HashMap;

public class RegistroInicialDatos implements MyRequestCallback {
    private static final String LOG_TAG = RegistroInicialDatos.class.getSimpleName();

    private Context regIniContext;
    private Activity regIniActivity;

    public RegistroInicialDatos(Context regIniContext, Activity regIniActivity) {
        this.regIniContext = regIniContext;
        this.regIniActivity = regIniActivity;
    }

    @SuppressLint("HardwareIds")
    public void setRegIni() {
        String androidId = Settings.Secure.getString(regIniContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        String appId = regIniActivity.getApplication().getPackageName();
        String idH = androidId + "-" + appId;

        String nc = Constants.numCelular;
        String os = Constants.os;
        String vOs = Build.VERSION.RELEASE;
        String fab = Build.MANUFACTURER;
        String mod = Build.MODEL;

        saveDataRegIni(nc, idH, os, vOs, fab, mod);
    }

    private void saveDataRegIni(String nc, String idH, String os, String vOs, String fab, String mod) {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorCodi = prefs.edit();

        editorCodi.putString(Constants.CELULAR, nc);
        editorCodi.putString(Constants.ID_HARDWARE, idH);
        editorCodi.putString(Constants.SISTEMA_OPERATIVO, os);
        editorCodi.putString(Constants.OS_VERSION, vOs);
        editorCodi.putString(Constants.FABRICANTE, fab);
        editorCodi.putString(Constants.MODELO, mod);

        editorCodi.apply();

        logDataRegIni(nc, idH, os, vOs, fab, mod);
    }

    private void logDataRegIni(String nc, String idH, String os, String vOs, String fab, String mod) {
        Log.v(LOG_TAG, nc);
        Log.v(LOG_TAG, idH);
        Log.v(LOG_TAG, os);
        Log.v(LOG_TAG, vOs);
        Log.v(LOG_TAG, fab);
        Log.v(LOG_TAG, mod);

        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);

        Log.v(LOG_TAG, "shared log: " + prefs.getString(Constants.CELULAR, Constants.CELULAR_DEF));
        Log.v(LOG_TAG, "shared log: " + prefs.getString(Constants.ID_HARDWARE, Constants.ID_HARDWARE_DEF));
        Log.v(LOG_TAG, "shared log: " + prefs.getString(Constants.os, Constants.OS_VERSION_DEF));
        Log.v(LOG_TAG, "shared log: " + prefs.getString(Constants.OS_VERSION, Constants.OS_VERSION_DEF));
        Log.v(LOG_TAG, "shared log: " + prefs.getString(Constants.FABRICANTE, Constants.FABRICANTE_DEF));
        Log.v(LOG_TAG, "shared log: " + prefs.getString(Constants.MODELO, Constants.MODELO_DEF));
    }

    private String getJsonRequestRegIni() {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);

        String nc = prefs.getString(Constants.CELULAR, Constants.CELULAR_DEF);
        String idH = prefs.getString(Constants.ID_HARDWARE, Constants.ID_HARDWARE_DEF);
        String os = prefs.getString(Constants.SISTEMA_OPERATIVO, Constants.SISTEMA_OPERATIVO_DEF);
        String vOs = prefs.getString(Constants.OS_VERSION, Constants.OS_VERSION_DEF);
        String fab = prefs.getString(Constants.FABRICANTE, Constants.FABRICANTE_DEF);
        String mod = prefs.getString(Constants.MODELO, Constants.MODELO_DEF);

        String dataToReturn = "d={\"nc\" : \"" + nc + "\"," +
                "\"idH\" : \"" + idH + "\"," +
                "\"ia\" : {" +
                "\"so\" : \"" + os + "\"," +
                "\"vSO\" : \"" + vOs + "\"," +
                "\"fab\" : \"" + fab + "\"," +
                "\"mod\" : \"" + mod + "\"" +
                "}" +
                "}";

        Log.v(LOG_TAG, dataToReturn);

        return dataToReturn;
    }

    public MutableLiveData<String> getServiceDataRegIni() {
        String url = Constants.urlRegIni;
        final String data = getJsonRequestRegIni();
        HashMap<String, String> params = new HashMap<>();
        params.put("content-Type", "text/plain");

        MutableLiveData<String> dataString;
        GetServiceData validaCuentaRequest = new GetServiceData(data, url, "POST",params , regIniActivity, this);
        dataString = validaCuentaRequest.doRequest();

        return dataString;
    }

    @Override
    public void finishedCallback(String dataString) {
        Gson gson = new Gson();
        ResponseRegistroInicial dataResponse = gson.fromJson(dataString, ResponseRegistroInicial.class);

        saveResponseRegIni(dataResponse);
        Toast.makeText(regIniContext, dataString, Toast.LENGTH_LONG).show();
    }

    private void saveResponseRegIni(ResponseRegistroInicial dataResponse) {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorCodi = prefs.edit();

        editorCodi.putString(Constants.GOOGLE_ID, dataResponse.getgId());
        editorCodi.putString(Constants.DV, dataResponse.getDv());

        editorCodi.apply();

        logResponseRegIni(dataResponse);
    }

    private void logResponseRegIni(ResponseRegistroInicial dataResponse) {
        Log.v(LOG_TAG, dataResponse.getgId());
        Log.v(LOG_TAG, dataResponse.getDv());
        Log.v(LOG_TAG, dataResponse.getEdoPet());
    }
}