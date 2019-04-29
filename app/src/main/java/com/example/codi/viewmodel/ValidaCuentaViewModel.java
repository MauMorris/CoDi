package com.example.codi.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.codi.servicios.codi.repositories.ValidaCuentaDatos;

public class ValidaCuentaViewModel extends ViewModel {
    private LiveData<String> valCuentaLiveData;

    public void init(){
        if (this.valCuentaLiveData != null) {
            return;
        }
        valCuentaLiveData = new MutableLiveData<>();
    }

    public LiveData<String> getDataValCuenta() {
        return this.valCuentaLiveData;
    }

    public void loadValCuentaData(Context mContext, Activity mActivity) {
        // Do an asynchronous operation to fetch users.
        ValidaCuentaDatos valCuentaData = new ValidaCuentaDatos(mContext, mActivity);
        valCuentaData.setValidaCuenta();

        this.valCuentaLiveData = valCuentaData.getServiceDataValidaCuenta();
    }
}