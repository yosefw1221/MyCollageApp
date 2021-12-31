package com.yotor.solution.mytimetable.ui.notifications;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyNotificationService extends Service {
    public MyNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

    return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        startService(new Intent(getApplicationContext(),MyNotificationService.class));
        super.onDestroy();
    }
}
