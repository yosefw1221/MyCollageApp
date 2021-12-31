package com.yotor.solution.mytimetable.portal.model;


import android.graphics.Color;

import com.yotor.solution.mytimetable.portal.Util;

import java.io.Serializable;


public class Course implements Serializable {
    private String NAME;
    private String ABBREVIATION;
    private String CODE;
    private int ECTS;
    private String[] PRE_REQUEST;
    private String[] CO_REQUEST;
    private int SEMESTER;
    private int COLOR;
    private int ID;


    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public int getECTS() {
        return ECTS;
    }

    public void setECTS(String ECTS) {
        this.ECTS = Util.strToInt(ECTS);
    }public void setECTS(int ECTS) {
        this.ECTS = ECTS;
    }

    public String[] getPRE_REQUEST() {
        return PRE_REQUEST;
    }

    public void setPRE_REQUEST(String PRE_REQUEST) {
        this.PRE_REQUEST = PRE_REQUEST.split("@");
    }

    public String[] getCO_REQUEST() {
        return CO_REQUEST;
    }

    public void setCO_REQUEST(String CO_REQUEST) {
        this.CO_REQUEST = CO_REQUEST.split("@");
    }

    public int getSEMESTER() {
        return SEMESTER;
    }

    public void setSEMESTER(String SEMESTER) {
        this.SEMESTER = Integer.parseInt(SEMESTER);
    }

    public int getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(int COLOR) {
        this.COLOR = COLOR;
    }
    public void setCOLOR(String COLOUR) {
        this.COLOR = Color.parseColor(COLOUR);
    }

    public String getABBREVIATION() {
        return ABBREVIATION;
    }

    public void setABBREVIATION(String ABBREVIATION) {
        this.ABBREVIATION = ABBREVIATION;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
