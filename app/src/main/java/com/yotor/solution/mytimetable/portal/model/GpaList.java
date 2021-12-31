package com.yotor.solution.mytimetable.portal.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class GpaList implements Serializable {
    private String Name;
    private float Gpa;
    public GpaList(){

    }
    public GpaList(String name,float grade){
        Name = name;
        Gpa = grade;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public float getGpa() {
        return Gpa;
    }

    public void setGpa(float gpa) {
        Gpa = gpa;
    }

    @NonNull
    @Override
    public String toString() {
        return getName()+","+getGpa();
    }
}
