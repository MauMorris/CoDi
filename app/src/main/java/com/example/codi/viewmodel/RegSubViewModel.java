package com.example.codi.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.codi.servicios.codi.repositories.RegistroSubsecuenteDatos;

public class RegSubViewModel extends ViewModel {
    private LiveData<String> regSubLiveData;

    public void init(){
        if (this.regSubLiveData != null) {
            return;
        }
        regSubLiveData = new MutableLiveData<>();
    }

    public LiveData<String> getDataRegSub() {
        return this.regSubLiveData;
    }

    public void loadRegSubData(Context mContext, Activity mActivity) {
        // Do an asynchronous operation to fetch users.
        RegistroSubsecuenteDatos regSubData = new RegistroSubsecuenteDatos(mContext, mActivity);
        regSubData.setRegSub();

        this.regSubLiveData = regSubData.getServiceDataRegSub();
    }
}