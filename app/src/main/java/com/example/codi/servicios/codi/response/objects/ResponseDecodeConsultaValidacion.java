package com.example.codi.servicios.codi.response.objects;

public class ResponseDecodeConsultaValidacion {
    private String rv;
    private String cr;
    private String nombreCDA;
    private String hmac;
    private String ci;
    private String cb;
    private String tc;
    private InnerObjectConsultaVal ds;

    public String getRv() {
        return rv;
    }

    public String getCr() {
        return cr;
    }

    public String getNombreCDA() {
        return nombreCDA;
    }

    public String getHmac() {
        return hmac;
    }

    public String getCi() {
        return ci;
    }

    public String getCb() {
        return cb;
    }

    public String getTc() {
        return tc;
    }

    public InnerObjectConsultaVal getDs() {
        return ds;
    }
}