package com.example.codi.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.codi.servicios.codi.repositories.RegistroInicialDatos;

public class RegIniViewModel extends ViewModel {
    private LiveData<String> regIniLiveData = new MutableLiveData<>();

    public void init(){
        if (this.regIniLiveData != null) {
            return;
        }
    }

    public LiveData<String> getDataRegIni() {
        return this.regIniLiveData;
    }

    public void loadRegIniData(Context mContext, Activity mActivity) {
        // Do an asynchronous operation to fetch users.
        RegistroInicialDatos regIniData = new RegistroInicialDatos(mContext, mActivity);
        regIniData.setRegIni();

        this.regIniLiveData = regIniData.getServiceDataRegIni();
    }
}