package com.yotor.solution.mytimetable.ui.notifications;

import android.os.Bundle;

import java.io.Serializable;

public class Alarm implements Serializable {
    public static final int TYPE_LESSON=0;
    public static final int TYPE_EXAM=1;
    public static final int TYPE_TASK=2;
    public static final int TYPE_MUTE=3;
    public static final int TYPE_UNMUTE=4;
    private Bundle bundle;
    private int id;
    private long timeMillis;
    private int type;
    public Alarm() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public long getTimemilisecond() {
        return timeMillis;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }


    public Bundle getBundle() {
        return bundle;
    }
    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
