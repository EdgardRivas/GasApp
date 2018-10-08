package com.capillasmemoriales.informatica.gasapp.models;

public class Detail {

    private String cf;
    private int ti;
    private double gl;
    private double value;
    private String date;

    public Detail() {
        this.cf = "";
        this.ti = 0;
        this.gl = 0;
        this.value = 0;
        this.date = null;
    }

    public Detail(String cf, int ti, double gl, double value, String date) {
        this.cf = cf;
        this.ti = ti;
        this.gl = gl;
        this.value = value;
        this.date = date;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public int getTi() {
        return ti;
    }

    public void setTi(int ti) {
        this.ti = ti;
    }

    public double getGl() {
        return gl;
    }

    public void setGl(double gl) {
        this.gl = gl;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
