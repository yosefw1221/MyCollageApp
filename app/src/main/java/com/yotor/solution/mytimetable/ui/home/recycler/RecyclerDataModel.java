package com.yotor.solution.mytimetable.ui.home.recycler;

import android.content.Context;
import android.graphics.drawable.Drawable;

//import com.google.android.gms.ads.NativeExpressAdView;

public class RecyclerDataModel {
    private int type;
    private String title;
    private String message;
    private String actionTxt;
    private Drawable actionIcon;
    private ActionModel actionModel;
//    private NativeExpressAdView expressAdView;
    private boolean pin;
    private Context context;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RecyclerDataModel lessonTimer() {
        this.type = RecyclerAdapter.VIEW_TYPE_TIMER_CARD;
        return this;
    }

    public RecyclerDataModel upcomingExamTask() {
        this.type = RecyclerAdapter.VIEW_TYPE_UPCOMING_CARD;
        return this;
    }

    public RecyclerDataModel basicCard() {
        this.type = RecyclerAdapter.VIEW_TYPE_BASIC_CARD;
        return this;
    }
//    public RecyclerDataModel adsCard(NativeExpressAdView expressAdView) {
//        this.expressAdView = expressAdView;
//        this.type = RecyclerAdapter.VIEW_TYPE_ADS_CARD;
//        return this;
//    }

    public RecyclerDataModel hintAction(String title, String message, String actionTxt,Drawable actionIcon, ActionModel actionModel) {
        this.actionTxt = actionTxt;
        this.type = RecyclerAdapter.VIEW_TYPE_HINT_CARD;
        this.title = title;
        this.message = message;
        this.actionIcon = actionIcon;
        this.actionModel = actionModel;
        return this;
    }
    public String getSubtitle() {
        return message;
    }

    public void setSubtitle(String subtitle) {
        this.message = subtitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ActionModel getActionModel() {
        return actionModel;
    }

    public void setActionModel(ActionModel actionModel) {
        this.actionModel = actionModel;
    }

    public String getActionTxt() {
        return actionTxt;
    }

    public void setActionTxt(String actionTxt) {
        this.actionTxt = actionTxt;
    }

    public Drawable getActionIcon() {
        return actionIcon;
    }

    public void setActionIcon(Drawable actionIcon) {
        this.actionIcon = actionIcon;
    }


//    public NativeExpressAdView getExpressAdView() {
//        return expressAdView;
//    }
}
