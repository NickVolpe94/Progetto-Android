package com.example.registrazioneapp;

public class Impostazioni {
    private boolean Escrementi;
    private boolean Randagio;
    private boolean guasto;
    private boolean buca;


    public Impostazioni(boolean escrementi, boolean randagio, boolean guasto, boolean buca) {
        Escrementi = escrementi;
        Randagio = randagio;
        this.guasto = guasto;
        this.buca = buca;
    }

    public boolean getEscrementi() {
        return Escrementi;
    }

    public void setEscrementi(boolean escrementi) {
        Escrementi = escrementi;
    }

    public boolean getRandagio() {
        return Randagio;
    }

    public void setRandagio(boolean randagio) {
        Randagio = randagio;
    }

    public boolean getGuasto() {
        return guasto;
    }

    public void setGuasto(boolean guasto) {
        this.guasto = guasto;
    }

    public boolean getBuca() {
        return buca;
    }

    public void setBuca(boolean buca) {
        this.buca = buca;
    }
}
