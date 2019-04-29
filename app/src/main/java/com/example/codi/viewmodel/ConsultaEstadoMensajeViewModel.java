package com.example.codi.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.example.codi.servicios.codi.response.objects.ResponseRegistroInicial;

public class ConsultaEstadoMensajeViewModel extends ViewModel {
    ResponseRegistroInicial regIniDataResponse;

    public void getUsers() {
        loadUsers();
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
    }

    public void init() {
    }
}