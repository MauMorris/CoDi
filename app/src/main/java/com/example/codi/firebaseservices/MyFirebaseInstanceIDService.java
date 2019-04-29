package com.example.codi.firebaseservices;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        try {
            String refresh= FirebaseInstanceId.getInstance().getToken();
            System.out.println("token actualizado"+refresh);
        }catch (Exception e){
            System.out.println("Error");
        }
    }
}