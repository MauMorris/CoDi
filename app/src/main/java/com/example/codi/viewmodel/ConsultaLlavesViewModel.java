package com.example.codi.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.codi.servicios.codi.repositories.ConsultaLlavesDatos;

public class ConsultaLlavesViewModel extends ViewModel {
    private LiveData<String> consultaLlavesLiveData;

    public void init(){
        if (this.consultaLlavesLiveData != null) {
            return;
        }
        consultaLlavesLiveData = new MutableLiveData<>();
    }

    public LiveData<String> getDataConsultaVLlaves() {
        return this.consultaLlavesLiveData;
    }

    public void loadConsultaLlavesData(Context mContext, Activity mActivity) {
        // Do an asynchronous operation to fetch users.
        ConsultaLlavesDatos valCuentaData = new ConsultaLlavesDatos(mContext, mActivity);
        valCuentaData.setConsultaLlaves();

        this.consultaLlavesLiveData = valCuentaData.getServiceDataConsultaLlaves();
    }
}