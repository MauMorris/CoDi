package com.example.codi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

public class GenerarQrActivity extends AppCompatActivity {
    public final static String[] incertidumbre = {"H (30%)", "Q (25%)", "M (15%)", "L (7%)"};

    public final static String[] clabeTdc = {"1500003862", "1205791510",
            "1500000077", "0102196018",
            "4152313304428563", "4555041870000184",
            "4555041000002290", "4555042602360516",
            "4555042700367868", "4152313304428837"};

    public final static String[] dataClabeQr = {"012180015000038629", "012180012057915109",
            "012180015000000770", "012180001021960188"};

    public final static String[] dataTdcQr = {"4152313304428563", "4555041870000184", "4555041000002290",
            "4555042602360516", "4555042700367868", "4152313304428837"};

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String charset = "UTF-8";

    public EditText et_concepto, et_cantidad, et_nombre;
    public Spinner sp_incertidumbre, sp_cuentaClabe;
    public ImageView iv_qr;
    public Switch sw_imagen;
    public Button btn_generarQR;

    public ArrayAdapter adapter, adapter1;

    public String strItemIncertidumbre, strCuentaClabe, strTdc, json;
    public Boolean isTdc = false, isLogo = false;
    public CreateQRFromJson qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_qr);

        et_concepto = findViewById(R.id.ed_concepto);
        et_cantidad = findViewById(R.id.ed_cantidad);
        et_nombre = findViewById(R.id.ed_nombre);

        iv_qr = findViewById(R.id.iv_qr);

        sp_incertidumbre = findViewById(R.id.sp_incertidumbre);
        sp_cuentaClabe = findViewById(R.id.sp_cuentaClable);

        btn_generarQR = findViewById(R.id.bt_generar);

        adapter = new ArrayAdapter<>(GenerarQrActivity.this, android.R.layout.simple_spinner_dropdown_item,
                incertidumbre);

        sp_incertidumbre.setAdapter(adapter);

        adapter1 = new ArrayAdapter<>(GenerarQrActivity.this, android.R.layout.simple_spinner_dropdown_item,
                clabeTdc);

        sp_cuentaClabe.setAdapter(adapter1);

        setListeners();
    }


    public void setListeners() {
        sp_incertidumbre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strItemIncertidumbre = adapterView.getItemAtPosition(i).toString();

                Log.d(LOG_TAG + ": ", "seleccion de % de incertidumbre:: " + strItemIncertidumbre);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        sp_cuentaClabe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String localData = adapterView.getItemAtPosition(i).toString();

                Log.d(LOG_TAG + ": ", "seleccion de cuenta o TDC:: " + localData);

                switch (localData) {
                    case "1500003862":
                        setValues("", false, dataClabeQr[0]);
                        break;
                    case "1205791510":
                        setValues("", false, dataClabeQr[1]);
                        break;
                    case "1500000077":
                        setValues("", false, dataClabeQr[2]);
                        break;
                    case "0102196018":
                        setValues("", false, dataClabeQr[3]);
                        break;
                    case "4152313304428563":
                        setValues(dataTdcQr[0], true, "");
                        break;
                    case "4555041870000184":
                        setValues(dataTdcQr[1], true, "");
                        break;
                    case "4555041000002290":
                        setValues(dataTdcQr[2], true, "");
                        break;
                    case "4555042602360516":
                        setValues(dataTdcQr[3], true, "");
                        break;
                    case "4555042700367868":
                        setValues(dataTdcQr[4], true, "");
                        break;
                    case "4152313304428837":
                        setValues(dataTdcQr[5], true, "");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btn_generarQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qr = new CreateQRFromJson();
                json = qr.crearJson(et_cantidad.getText().toString(), et_concepto.getText().toString(),
                        et_nombre.getText().toString(), isTdc, strTdc, strCuentaClabe);

                Log.d(LOG_TAG + ": ", "JSON Respuesta --> " + json);

                iv_qr.setImageBitmap(qr.createQR(json, charset, iv_qr.getLayoutParams().width,
                        strItemIncertidumbre, GenerarQrActivity.this, isLogo));

                Log.d(LOG_TAG + ": ", "se ha generado el QR correctamente");
            }
        });
    }

    public void setValues(String numTdc, Boolean isTDC, String numCuentaClabe) {
        strTdc = numTdc;
        isTdc = isTDC;
        strCuentaClabe = numCuentaClabe;

        Log.d(LOG_TAG + ": ", "valores guardados para formar el JSON:: " +
                " numero TDC: " + numTdc +
                " numero CLABE: " + numCuentaClabe +
                " bandera TDC: " + isTDC);
    }
}