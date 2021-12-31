package com.yotor.solution.mytimetable.portal.mywidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new LessonWidgetFactory(this.getApplicationContext());
    }
}
