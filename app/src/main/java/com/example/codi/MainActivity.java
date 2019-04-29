package com.example.codi;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.codi.crypto.GenerateKeys;
import com.example.codi.viewmodel.ConsultaEstadoMensajeViewModel;
import com.example.codi.viewmodel.ConsultaValidaCuentaViewModel;
import com.example.codi.viewmodel.RegIniViewModel;
import com.example.codi.viewmodel.RegSubViewModel;
import com.example.codi.viewmodel.ValidaCuentaViewModel;

public class MainActivity extends AppCompatActivity {

    Context mContext;
    Activity mActivity;
    GenerateKeys codiKeys;

    Animation show_button, show_card;
    Animation hide_button, hide_card;

    FloatingActionButton fabRegIni, fabRegSub, fabValCuenta;
    FloatingActionButton fabConsCuenta, fabGeneraQr, fabEscaneaQr, fabConsMensajes, fabCodr;
    CardView cvRegIni, cvRegSub, cvValCuenta;
    CardView cvConsCuenta, cvGeneraQr, cvEscaneaQr;
    EditText etCodr;

    RegIniViewModel regIniVModel;
    RegSubViewModel regSubVModel;
    ValidaCuentaViewModel valCuentaVModel;
    ConsultaValidaCuentaViewModel consultaVCuentaVModel;
    ConsultaEstadoMensajeViewModel consultaEdoMensajeVModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = MainActivity.this;
        mActivity = MainActivity.this;

        regIniVModel = ViewModelProviders.of(this).get(RegIniViewModel.class);
        regSubVModel = ViewModelProviders.of(this).get(RegSubViewModel.class);
        valCuentaVModel = ViewModelProviders.of(this).get(ValidaCuentaViewModel.class);
        consultaVCuentaVModel = ViewModelProviders.of(this).get(ConsultaValidaCuentaViewModel.class);
        consultaEdoMensajeVModel = ViewModelProviders.of(this).get(ConsultaEstadoMensajeViewModel.class);

        regIniVModel.init();
        regSubVModel.init();
        valCuentaVModel.init();
        consultaVCuentaVModel.init();
        consultaEdoMensajeVModel.init();

        regIniVModel.getDataRegIni().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String regIniData) {
                Toast.makeText(mContext, regIniData, Toast.LENGTH_LONG).show();
            }
        });

        regSubVModel.getDataRegSub().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String regSubData) {
                Toast.makeText(mContext, regSubData, Toast.LENGTH_LONG).show();
            }
        });

        valCuentaVModel.getDataValCuenta().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String valCuentaData) {
                Toast.makeText(mContext, valCuentaData, Toast.LENGTH_LONG).show();
            }
        });

        consultaVCuentaVModel.getDataConsultaVal().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String consultaValCuentaData) {
                Toast.makeText(mContext, consultaValCuentaData, Toast.LENGTH_LONG).show();
            }
        });

//        consultaEdoMensajeVModel.getDataConsultaVal().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String consultaValCuentaData) {
//                Toast.makeText(mContext, consultaValCuentaData, Toast.LENGTH_LONG).show();
//            }
//        });

        fabRegIni = findViewById(R.id.fabRegIni);
        fabCodr = findViewById(R.id.fabCodR);
        fabRegSub = findViewById(R.id.fabRegSub);
        fabValCuenta = findViewById(R.id.fabValidaCuenta);
        fabConsCuenta = findViewById(R.id.fabConsultaValidacion);
        fabGeneraQr = findViewById(R.id.fabGenerarQR);
        fabEscaneaQr = findViewById(R.id.fabEscanearQr);

        fabRegIni.setOnClickListener(fabListener);
        fabCodr.setOnClickListener(fabListener);
        fabRegSub.setOnClickListener(fabListener);
        fabValCuenta.setOnClickListener(fabListener);
        fabConsCuenta.setOnClickListener(fabListener);
        fabGeneraQr.setOnClickListener(fabListener);
        fabEscaneaQr.setOnClickListener(fabListener);

        etCodr = findViewById(R.id.etCodR);

        cvRegIni = findViewById(R.id.cv_reg_ini);
        cvRegSub = findViewById(R.id.cv_reg_sub);
        cvValCuenta = findViewById(R.id.cv_valida_cuenta);
        cvConsCuenta = findViewById(R.id.cv_consulta_validacion);
        cvGeneraQr = findViewById(R.id.cv_escanera_qr);
        cvEscaneaQr = findViewById(R.id.cv_generar_qr);

        show_button = AnimationUtils.loadAnimation(mContext, R.anim.show_button);
        hide_button = AnimationUtils.loadAnimation(mContext, R.anim.hide_button);

        show_card = AnimationUtils.loadAnimation(mContext, R.anim.show_card);
        hide_card = AnimationUtils.loadAnimation(mContext, R.anim.hide_card);

        showAnim();
    }

    private void showAnim() {
        fabRegIni.startAnimation(show_button);
        fabRegSub.startAnimation(show_button);
        fabValCuenta.startAnimation(show_button);
        fabConsCuenta.startAnimation(show_button);
        fabGeneraQr.startAnimation(show_button);
//        fabConsMensajes.startAnimation(show_button);
        fabCodr.startAnimation(show_button);

        cvRegIni.startAnimation(show_card);
        cvRegSub.startAnimation(show_card);
        cvValCuenta.startAnimation(show_card);
        cvConsCuenta.startAnimation(show_card);
        cvGeneraQr.startAnimation(show_card);
        cvEscaneaQr.startAnimation(show_card);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.fabRegIni:
                    regIniVModel.loadRegIniData(mContext, mActivity);

                    Log.v("a", "1");
                    break;
                case R.id.fabRegSub:
                    regSubVModel.loadRegSubData(mContext, mActivity);

                    Log.v("a", "2");
                    break;
                case R.id.fabValidaCuenta:
                    valCuentaVModel.loadValCuentaData(mContext, mActivity);

                    Log.v("a", "3");
                    break;
                case R.id.fabConsultaValidacion:
                    consultaVCuentaVModel.loadConsultaValData(mContext, mActivity);

                    Log.v("a", "4");
                    break;
                case R.id.fabGenerarQR:
                    newActivity(GenerarQrActivity.class);

                    Log.v("a", "5");
                    break;
                case R.id.fabEscanearQr:


                    Log.v("a", "6");
                    break;
                case R.id.fabCodR:
                    String sCodr = etCodr.getText().toString();
                    etCodr.setText("");
                    codiKeys = new GenerateKeys(sCodr, mContext);
                    codiKeys.cryptofunctions();
                    Log.v("a", sCodr);
                    break;
            }
        }
    };

    private void newActivity(Class nextActivity){
        Intent nextFunctionality = new Intent(mContext, nextActivity);
        startActivity(nextFunctionality);
    }
}