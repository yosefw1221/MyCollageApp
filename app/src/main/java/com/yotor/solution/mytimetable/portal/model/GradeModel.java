package com.yotor.solution.mytimetable.portal.model;

import java.io.Serializable;

public class GradeModel implements Serializable {
    private int COLOR;
    private int MODEL_ID;
    private int COURSE_ID;
    private String COURSENAME;
    private int ECTS;
    private int SelectedGRADE;




    public String getCOURSENAME() {
        return COURSENAME;
    }

    public void setCOURSENAME(String COURSENAME) {
        this.COURSENAME = COURSENAME;
    }

    public int getECTS() {
        return ECTS;
    }

    public void setECTS(int ECTS) {
        this.ECTS = ECTS;
    }

    public int getCOURSE_ID() {
        return COURSE_ID;
    }

    public void setCOURSE_ID(int COURSE_ID) {
        this.COURSE_ID = COURSE_ID;
    }

    public int getSelectedGRADE() {
        return SelectedGRADE;
    }

    public void setSelectedGRADE(int selectedGRADE) {
        SelectedGRADE = selectedGRADE;
    }

    public int getMODEL_ID() {
        return MODEL_ID;
    }

    public void setMODEL_ID(int MODEL_ID) {
        this.MODEL_ID = MODEL_ID;
    }

    public int getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(int COLOR) {
        this.COLOR = COLOR;
    }
}
