package com.example.codi.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.codi.servicios.codi.repositories.ConsultaValidacionDatos;

public class ConsultaValidaCuentaViewModel extends ViewModel {
    private LiveData<String> consultaValLiveData;

    public void init(){
        if (this.consultaValLiveData != null) {
            return;
        }
        consultaValLiveData = new MutableLiveData<>();
    }

    public LiveData<String> getDataConsultaVal() {
        return this.consultaValLiveData;
    }

    public void loadConsultaValData(Context mContext, Activity mActivity) {
        // Do an asynchronous operation to fetch users.
        ConsultaValidacionDatos valCuentaData = new ConsultaValidacionDatos(mContext, mActivity);
        valCuentaData.setConsultaValidacion();

        this.consultaValLiveData = valCuentaData.getServiceDataConsultaVal();
    }
}