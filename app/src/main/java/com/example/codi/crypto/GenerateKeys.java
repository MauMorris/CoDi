package com.example.codi.crypto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.example.codi.constants.Constants;
import com.example.codi.firebaseservices.Token2;
import com.example.codi.firebaseservices.callback.MyTokenResultCallback;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class GenerateKeys implements MyTokenResultCallback {
    public static final String LOG_TAG = GenerateKeys.class.getSimpleName();
    private String codR;
    private Context mContext;

    public GenerateKeys(String codR, Context mContext) {
        this.codR = codR;
        this.mContext = mContext;
    }

    public void cryptofunctions() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(this.codR.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte aDigest : digest) {
                sb.append(Integer.toString((aDigest & 0xff) + 0x100, 16).substring(1));
            }
            Log.v(LOG_TAG, sb.toString());

            SharedPreferences prefs = mContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);
            sb.append(prefs.getString(Constants.ID_HARDWARE, Constants.ID_HARDWARE_DEF));
            sb.append(prefs.getString(Constants.CELULAR, Constants.CELULAR_DEF));

            Log.v(LOG_TAG, sb.toString());

            digest = md.digest(sb.toString().getBytes());

            StringBuilder sbkSource = new StringBuilder();
            for (byte aDigest : digest) {
                sbkSource.append(Integer.toString((aDigest & 0xff) + 0x100, 16).substring(1));
            }

            String keysource = sbkSource.toString();
            Log.v(LOG_TAG, keysource);

            SharedPreferences.Editor editorCodi = prefs.edit();
            editorCodi.putString(Constants.KEYSOURCE, keysource);
            editorCodi.apply();

            byte[] llaveDes = Utils.getKeyAES(keysource);
            byte[] vectorDes = Utils.getIvAES(keysource);

            String googleId = prefs.getString(Constants.GOOGLE_ID, Constants.GOOGLE_ID_DEF);
            Log.v(LOG_TAG, googleId);

            byte[] googleIdBytes = new byte[0];

            if (googleId != null) {
                googleIdBytes = Utils.decodeBase64StringToByte(googleId);
            }
            Log.v(LOG_TAG, Arrays.toString(googleIdBytes));

            String message;
            try {
                message = CipherData.AESDecode(googleIdBytes, llaveDes, vectorDes);
                Log.v(LOG_TAG, message);

                editorCodi.putString(Constants.GOOGLE_ID_CLARO, message);
                editorCodi.apply();

                @SuppressLint("HardwareIds")
                String android_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                setInitializeAppSecondary(message, android_id);

                new Token2(this).execute(message);
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

    private void setInitializeAppSecondary(String cGoogleID, String androidID){
        FirebaseOptions.Builder builder = new FirebaseOptions.Builder()
                .setApplicationId("1:" + cGoogleID +":android:" + androidID);

        boolean hasBeenInitialized=false;
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps(mContext);

        for(FirebaseApp app:firebaseApps){
            if(app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)){
                hasBeenInitialized=true;
                FirebaseApp myApp = app;
                Log.d("Msj: ","project2 configurado:" +myApp.getName());
            }
        }

        if(!hasBeenInitialized){
            FirebaseApp myApp = FirebaseApp.initializeApp(mContext,builder.build());
            Log.d("Msj: ","project2 configurado:" +myApp.getName());
        }
    }

    @Override
    public void getToken(String sIdN) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.NAME_PREFERENCES_CODI, Context.MODE_PRIVATE);

        SharedPreferences.Editor editorCodi = prefs.edit();
        editorCodi.putString(Constants.ID_NOTIFICATIONS, sIdN);
        editorCodi.apply();

        Log.v(LOG_TAG, "getFromSaved " + prefs.getString(Constants.ID_NOTIFICATIONS, Constants.ID_NOTIFICATIONS_DEF));
    }
}