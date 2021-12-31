package com.yotor.solution.mytimetable.portal.model;


import java.io.Serializable;

public class ExamTaskModel implements Serializable {
    private int ID;
    private int TYPE;
    private String TOPIC;
    private int COURSE_ID;
    private String COURSENAME;
    private String NOTES;
    private boolean isDONE;
    private long DATE;
    private int Color;



    public String getCOURSENAME() {
        return COURSENAME;
    }

    public void setCOURSENAME(String COURSENAME) {
        this.COURSENAME = COURSENAME;
    }

    public int getCOURSE_ID() {
        return COURSE_ID;
    }

    public void setCOURSE_ID(int COURSE_ID) {
        this.COURSE_ID = COURSE_ID;
    }

    public boolean isDONE() {
        return isDONE;
    }

    public void setIsDONE(boolean isDONE) {
        this.isDONE = isDONE;
    }

    public long getDATE() {
        return (DATE);
    }

    public void setDATE(long timeinmilisec) {
        this.DATE = timeinmilisec;
    }

    public String getNOTES() {
        return NOTES;
    }

    public void setNOTES(String NOTES) {
        this.NOTES = NOTES;
    }

    public String getTOPIC() {
        return TOPIC;
    }

    public void setTOPIC(String TOPIC) {
        this.TOPIC = TOPIC;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public int getColor() {
        return Color;
    }

    public void setColor(int color) {
        Color = color;
    }
}
