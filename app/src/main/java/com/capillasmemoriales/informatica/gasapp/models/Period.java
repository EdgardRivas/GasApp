package com.capillasmemoriales.informatica.gasapp.models;

public class Period {

    private int codPer;
    private int codEmp;
    private String desc;
    private String to;
    private String from;

    public Period() {

    }

    public Period(int codPer, int codEmp, String desc, String to, String from) {
        this.codPer = codPer;
        this.codEmp = codEmp;
        this.desc = desc;
        this.to = to;
        this.from = from;
    }

    public int getCodPer() {
        return codPer;
    }

    public void setCodPer(int codPer) {
        this.codPer = codPer;
    }

    public int getCodEmp() {
        return codEmp;
    }

    public void setCodEmp(int codEmp) {
        this.codEmp = codEmp;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
