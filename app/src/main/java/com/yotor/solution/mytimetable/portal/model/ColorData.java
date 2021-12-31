package com.yotor.solution.mytimetable.portal.model;


import android.graphics.Color;

public class ColorData {
    private String NAME;
    private int COLOR;


    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
    public int getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(int COLOR) {
        this.COLOR = COLOR;
    }public void setCOLOR(String color){
        this.COLOR = Color.parseColor(color);
    }

}
