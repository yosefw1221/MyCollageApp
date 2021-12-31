package com.yotor.solution.mytimetable.portal.model;


import java.io.Serializable;

public class Lesson implements Serializable {
    private int START_TIME;
    private int END_TIME;
    private int LESSON;
    private int DAY;
    private int ID;
    private int COURSE_ID;
    private Course COURSE;

    public Lesson(){

    }
    public Lesson(Course course,int id,int start,int end,int lesson,int day){
        COURSE = course;
        START_TIME = start;
        END_TIME = end;
        LESSON = lesson;
        DAY = day;
        ID = id;
    }
    public int getSTART_TIME() {
        return START_TIME;
    }

    public void setSTART_TIME(int START_TIME) {
        this.START_TIME = START_TIME;
    }

    public int getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(int END_TIME) {
        this.END_TIME = END_TIME;
    }

    public int getLESSON() {
        return LESSON;
    }

    public void setLESSON(int LESSON) {
        this.LESSON = LESSON;
    }

    public int getDAY() {
        return DAY;
    }

    public void setDAY(int DAY) {
        this.DAY = DAY;
    }

    public Course getCOURSE() {
        return COURSE;
    }

    public void setCOURSE(Course COURSE) {
        this.COURSE = COURSE;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCOURSE_ID(int COURSE_ID) {
        this.COURSE_ID = COURSE_ID;
    }

    public int getCOURSE_ID() {
        return COURSE_ID;
    }
}