package com.example.codi.firebaseservices;

import android.os.AsyncTask;
import android.util.Log;

import com.example.codi.firebaseservices.callback.MyTokenResultCallback;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

public class Token2 extends AsyncTask<String, Void, Void> {
    private MyTokenResultCallback mTokenResult;
    private String token;

    public Token2(MyTokenResultCallback mTokenResult){
        this.mTokenResult = mTokenResult;
    }

    @Override
    protected Void doInBackground(String... params) {
        try{
            token = FirebaseInstanceId.getInstance().getToken(params[0], FirebaseMessaging.INSTANCE_ID_SCOPE);
        }catch(IOException e){
            e.printStackTrace();
        }
        Log.d("*Token2: ",token + " ");
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mTokenResult.getToken(token);
        Log.d("*Return Token2: ",token + " ");
    }
}