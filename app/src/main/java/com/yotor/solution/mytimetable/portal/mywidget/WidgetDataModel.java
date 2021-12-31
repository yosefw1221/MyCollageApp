package com.yotor.solution.mytimetable.portal.mywidget;

import org.joda.time.DateTime;

public class WidgetDataModel {
    private DateTime startTime;
    private DateTime endTime;
    private String subTitle;
    private int TotalLesson;
    private String courseName;

    int getTotalLesson() {
        return TotalLesson;
    }

    void setTotalLesson(int totallesson) {
        TotalLesson = totallesson;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public String getCourseName() {
        return courseName;
    }

    void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    String getSubTitle() {
        return subTitle;
    }

    void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}

