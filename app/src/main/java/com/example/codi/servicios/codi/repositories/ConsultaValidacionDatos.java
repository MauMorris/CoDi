package com.example.codi.servicios.codi.repositories;

import android.app.Activity;
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
import com.example.codi.servicios.codi.response.objects.ResponseConsultaValidaCuenta;
import com.example.codi.servicios.codi.response.objects.ResponseDecodeConsultaValidacion;
import com.example.codi.servicios.codi.response.objects.ResponseValidaCuenta;
import com.google.gson.Gson;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ConsultaValidacionDatos implements MyRequestCallback {
    private static final String LOG_TAG = ConsultaValidacionDatos.class.getSimpleName();

    private Context regIniContext;
    private Activity regIniActivity;
    private String hmac;

    public ConsultaValidacionDatos(Context mContext, Activity mActivity) {
        this.regIniContext = mContext;
        this.regIniActivity = mActivity;
    }

    public void setConsultaValidacion() {

        String cr = Constants.CLAVE_RASTREO;

        this.hmac = generateHmac();
    }

    private String generateHmac() {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);

        String keysource = prefs.getString(Constants.KEYSOURCE, Constants.KEYSOURCE_DEF);
        String nc = prefs.getString(Constants.CELULAR, Constants.CELULAR_DEF);
        String dv = prefs.getString(Constants.DV, Constants.DV_DEF);
        String cr = prefs.getString(Constants.CR, Constants.CR_DEF);

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

        String contentHmac = nc + newDv + cr;
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

    private String getJsonRequestConsultaValidacion() {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);

        String nc = prefs.getString(Constants.CELULAR, Constants.CELULAR_DEF);
        String dv = prefs.getString(Constants.DV, Constants.DV_DEF);
        String cr = prefs.getString(Constants.CR, Constants.CR_DEF);

        String hmac = this.hmac;

        String dataToReturn = "d={\"cr\" : \"" + cr + "\"," +
                "\"ds\" : {" +
                "\"nc\" : \"" + nc + "\"," +
                "\"dv\" : " + dv +
                "}," +
                "\"hmac\" : \"" + hmac + "\"" +
                "}";

        Log.v(LOG_TAG, dataToReturn);
        return dataToReturn;
    }

    public MutableLiveData<String> getServiceDataConsultaVal() {
        MutableLiveData<String> dataString;

        String url = Constants.urlConsultaValidacion;
        String data = getJsonRequestConsultaValidacion();

        HashMap<String, String> params = new HashMap<>();
        params.put("content-Type", "text/plain");

        GetServiceData validaCuentaRequest = new GetServiceData(data, url, "POST", params, regIniActivity, this);
        dataString = validaCuentaRequest.doRequest();

        return dataString;
    }

    @Override
    public void finishedCallback(String dataString) {
        Gson gson = new Gson();
        ResponseConsultaValidaCuenta dataResponse = gson.fromJson(dataString, ResponseConsultaValidaCuenta.class);

        saveResponseConsultaValidaCuenta(dataResponse);
        Toast.makeText(regIniContext, dataString, Toast.LENGTH_LONG).show();
    }

    private void saveResponseConsultaValidaCuenta(ResponseConsultaValidaCuenta dataResponse) {
        SharedPreferences prefs = regIniContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);

        String mensajeDataResponse = dataResponse.getInfCif();

        String claveRastreo = prefs.getString(Constants.CR, Constants.CR_DEF);
        String keysource = prefs.getString(Constants.KEYSOURCE, Constants.KEYSOURCE_DEF);

        String infoToDescif = claveRastreo + keysource;

        Log.v(LOG_TAG, claveRastreo);
        Log.v(LOG_TAG, keysource);
        Log.v(LOG_TAG, infoToDescif);
        Log.v(LOG_TAG, mensajeDataResponse);

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(infoToDescif.getBytes());

            Log.v(LOG_TAG, Arrays.toString(digest));

            StringBuilder sbkSource = new StringBuilder();
            for (byte aDigest : digest) {
                sbkSource.append(Integer.toString((aDigest & 0xff) + 0x100, 16).substring(1));
            }

            String varToDescypher = sbkSource.toString();
            Log.v(LOG_TAG, varToDescypher);

            String message;

            byte[] llaveDes = Utils.getKeyAES(varToDescypher);
            byte[] vectorDes = Utils.getIvAES(varToDescypher);

            byte [] mensaje = Utils.decodeBase64StringToByte(mensajeDataResponse);

            try {
                message = CipherData.AESDecode(mensaje, llaveDes, vectorDes);
                Log.v(LOG_TAG, message);
                Gson gson = new Gson();
                ResponseDecodeConsultaValidacion rdcv = gson.fromJson(message, ResponseDecodeConsultaValidacion.class);
                Log.v(LOG_TAG, rdcv.getRv());
                Log.v(LOG_TAG, rdcv.getTc());
                Log.v(LOG_TAG, rdcv.getCb());
                Log.v(LOG_TAG, rdcv.getCi());
                Log.v(LOG_TAG, rdcv.getCr());
                Log.v(LOG_TAG, rdcv.getHmac());
                Log.v(LOG_TAG, "nombre " + rdcv.getNombreCDA());

            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}