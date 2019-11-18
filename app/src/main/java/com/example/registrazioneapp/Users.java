package com.example.registrazioneapp;

import java.util.Date;

public class Users {
    Date data2;
    private String id;
    private String motivo;
    private String data;
    double lat;
    double lon;
    public Users(){

    }

    public Users(String id, String motivo, String data,double lat, double lon,Date data2) {
        this.id = id;
        this.motivo = motivo;
        this.lat = lat;
        this.lon = lon;
        this.data = data;
        this.data2 = data2;
    }

    public String getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public String getMotivo() {
        return motivo;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}



