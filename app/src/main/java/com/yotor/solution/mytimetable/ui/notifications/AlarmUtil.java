package com.yotor.solution.mytimetable.ui.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.yotor.solution.mytimetable.portal.ShardPref;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
import com.yotor.solution.mytimetable.portal.model.ExamTaskModel;
import com.yotor.solution.mytimetable.portal.model.Lesson;
import com.yotor.solution.mytimetable.ui.Setting.LessonTimeHelper;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class AlarmUtil {
    private Context ctx;
    static final String ALARM_ACTION = "com.yotor.solution.mytimetable.ACTION_ADD_ALARM";

    public AlarmUtil(Context ctx) {
        this.ctx = ctx;
    }

    private static void setAlarm(Context context, Alarm alarm) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
       // Intent intent = new Intent(context, MyAlarmReceiver.class);
        Intent intent = new Intent(ALARM_ACTION);
        intent.putExtra("data",alarm.getBundle());
        intent.putExtra("time",alarm.getTimemilisecond());
        intent.putExtra("type",alarm.getType());
        Log.e("NEW ALARM :", "ID : " + alarm.getId() + " TYPE : "+alarm.getType()+ " DATE : " + new DateTime().withMillis(alarm.getTimemilisecond()).toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        if (manager != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.getTimemilisecond(), pendingIntent);
            else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
                manager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimemilisecond(), pendingIntent);
            else
                manager.set(AlarmManager.RTC_WAKEUP, alarm.getTimemilisecond(), pendingIntent);
    }

    public static void updateAlarmList(final Context ctx) {
        final AlarmUtil util = new AlarmUtil(ctx);
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                util.ScheduleAllAlarm(ctx, true);
            }
        };
        handler.post(runnable);
    }

    private void ScheduleAllAlarm(Context ctx, boolean forceUpdate) {
        if (!isRunning() || forceUpdate) {
            Log.e("ScheduleAlarm force " + forceUpdate, "  // SEATED //");
            stopAllAlarmIntent(ctx, false);
            scheduleLessonAlarmList(true);
            scheduleExamTaskAlarmList(true);
            scheduleMuteAlarmList(true);
        } else
            Log.e("ScheduleAlarm force " + forceUpdate, "Already seated");
    }

    private boolean isRunning() {
        Intent intent = new Intent(ctx, MyAlarmReceiver.class);
        List<Alarm> lessonAlarms = scheduleLessonAlarmList(false);
        for (Alarm lessonAlarm : lessonAlarms) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, lessonAlarm.getId(), intent, PendingIntent.FLAG_NO_CREATE);
            if (pendingIntent != null)
                return true;
        }
        return false;
    }

    public void stopLessonAlarm() {
        Log.e("stopLessonAlarm ", " Method Call");
        AlarmManager manager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ctx, MyAlarmReceiver.class);
        List<Alarm> lessonAlarms = getLessonAlarmList();
        for (Alarm lessonAlarm : lessonAlarms) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, lessonAlarm.getId(), intent, PendingIntent.FLAG_NO_CREATE);
            Log.e("stopLessonAlarm Seated " + (pendingIntent != null), " list : " + "id " + lessonAlarm.getId() + "  //  " + new DateTime().withMillis(lessonAlarm.getTimemilisecond()));
            if (pendingIntent != null) {
                if (manager!=null)
                manager.cancel(pendingIntent);
                pendingIntent.cancel();

            }
        }
    }

    private void stopMuteAlarm() {
        Log.e("++++StopMuteAlarm", "Method Call ==");
        AlarmManager manager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ctx, MyAlarmReceiver.class);
        List<Alarm> muteAlarms = scheduleMuteAlarmList(false);
        for (Alarm alarm : muteAlarms) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, alarm.getId(), intent, PendingIntent.FLAG_NO_CREATE);
            Log.e("stopMuteAlarm Seated " + (pendingIntent != null), " list : " + "id " + alarm.getId() + "  //  " + new DateTime().withMillis(alarm.getTimemilisecond()));
            if (pendingIntent != null) {
                if (manager!=null)
                manager.cancel(pendingIntent);
                pendingIntent.cancel();

            }
        }
    }

    private void stopAllAlarmIntent(Context ctx, boolean restart) {
        Log.e("-----StopAllAlarmIntent", "restart " + restart + "  Method Call ===");
        stopLessonAlarm();
        stopMuteAlarm();
        stopExamTaskAlarm(-1);
        if (restart) {
            ScheduleAllAlarm(ctx, false);
        }
    }

    public void stopExamTaskAlarm(int type) {
        Log.e("___++_StopExamTaskAlarm", "Method Call ====  = = = = == = = ");
        AlarmManager manager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ctx, MyAlarmReceiver.class);
        List<Alarm> taskAlarms = scheduleExamTaskAlarmList(false);
        for (Alarm alarm : taskAlarms) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, alarm.getId(), intent, PendingIntent.FLAG_NO_CREATE);
            if (pendingIntent != null) {
                if (type != -1) {
                    if (type == alarm.getType()) {
                        Log.e("stopExamTaskAlarm", "Canceled" + " type ");
                        if (manager!=null)
                        manager.cancel(pendingIntent);
                        pendingIntent.cancel();
                    }
                } else {
                    Log.e("stopExamTaskAlarm", "Canceled" + " type ALL");
                    if (manager!=null)
                    manager.cancel(pendingIntent);
                    pendingIntent.cancel();
                }
            } else {
                Log.e("stopExamTaskAlarm", "Pending Intent null");
            }
        }
    }

    public List<Alarm> scheduleLessonAlarmList(boolean setAlarm) {
        if (!PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean("lesson_notification",true))
            return new LinkedList<>();
        List<Alarm> list = getLessonAlarmList();
        List<Alarm> scheduled = new LinkedList<>();
        Log.e("//=ScheduleLessonAlarm ", "set Alarm " + setAlarm + " METHOD CALL ==== = =");
        int before = ShardPref.getInt(ctx, "lesson_notification_before", 5);
        for (Alarm alarm : list) {
            alarm.setTimeMillis(new DateTime().withMillis(alarm.getTimemilisecond()).minusMinutes(before).toDateTime().getMillis());
            Log.e("Alarm " + alarm.getId(), "status pending " + "date " + new DateTime().withMillis(alarm.getTimemilisecond()).toString());
            if (alarm.getTimemilisecond() > System.currentTimeMillis()) {
                Log.e("Alarm " + alarm.getId(), "status Added " + "date " + new DateTime().withMillis(alarm.getTimemilisecond()).toString());
                scheduled.add(alarm);
                if (setAlarm)
                    setAlarm(ctx, alarm);
            }
        }
        return scheduled;
    }

    private List<Alarm> getLessonAlarmList() {
        Log.e("+_+_getLessonAlarmList", "  //  METHOD CALL ++ + + ++ + + ");
        LessonTimeHelper helper = new LessonTimeHelper(ctx);
        StudentDB db = new StudentDB(ctx);
        //DateTime d = new DateTime();
        List<Alarm> alarmList = new LinkedList<>();
        Alarm alarm;
        List<Integer> dayList = sortedEnabledDay(ctx);
        int totalLesson = ShardPref.getInt(ctx, "total_lesson", 15);
        for (Integer day : dayList) {
            Log.e("### getLessonAlarmList", " day = " + day);
            List<Lesson> list = db.getLessonOfDay(day);
            for (Lesson lesson : list) {
                Log.e("getLessonAlarm  course ", lesson.getCOURSE().getNAME() + " day " + lesson.getDAY() + " id" + lesson.getID());
                if (helper.getLessonTime(lesson.getLESSON()).getLessonStartTime(day).getMillis() > System.currentTimeMillis()) {
                    if (lesson.getLESSON() > totalLesson)
                        continue; // skip lesson above total lesson
                    Log.e("getLessonAlarm ADDED ", lesson.getCOURSE().getNAME() + " day " + lesson.getDAY() + " id" + lesson.getID());
                    alarm = new Alarm();
                    alarm.setId(lesson.getID());
                    alarm.setTimeMillis(helper.getLessonTime(lesson.getLESSON()).getLessonStartTime(day).getMillis());
                    Bundle bundle = new Bundle();
                    bundle.putString("course",lesson.getCOURSE().getNAME());
                    bundle.putInt("color",lesson.getCOURSE().getCOLOR());
                    bundle.putInt("lesson",lesson.getLESSON());
                    bundle.putLong("date",alarm.getTimemilisecond());
                    alarm.setBundle(bundle);
                    alarm.setType(Alarm.TYPE_LESSON);
                    alarmList.add(alarm);
                }
            }
            if (alarmList.size() > 0)
                return alarmList;
        }
        /*for (int day = d.getDayOfWeek(), i = 0; i < 7; i++, day = (day < 7) ? (++day) : 1) {
        }*/
        return alarmList;
    }

    public List<Alarm> scheduleExamTaskAlarmList(boolean setAlarm) {
        boolean task = PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean("task_notification",true);
        boolean exam = PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean("exam_notification",true);
        if (!(task||exam))
            return new LinkedList<>();
        StudentDB db = new StudentDB(ctx);
        List<ExamTaskModel> taskModels = db.getAllTaskExam(0);
        List<Alarm> alarmList = new LinkedList<>();
        Log.e("#-ScheduleExamTaskAlarm", "  //    METHOD CALL ++ + + ++ + + { Size } : " + alarmList.size());
        Alarm alarm;
        int taskBefore = ShardPref.getInt(ctx, "task_notification_before", 1);
        int examBefore = ShardPref.getInt(ctx, "exam_notification_before", 7);
        for (ExamTaskModel taskModel : taskModels) {
            if (taskModel.isDONE()) continue;
            if (taskModel.getTYPE()==1&&!exam)continue;
            if (taskModel.getTYPE()==2&&!task)continue;
            Log.e("#-ScheduleExamTaskAlarm", "  //  task " + taskModel.getCOURSENAME() + "  topic  " + taskModel.getTOPIC());
            alarm = new Alarm();
            alarm.setId(taskModel.getID() + 1000);
            alarm.setType(taskModel.getTYPE());
            if (alarm.getType() == Alarm.TYPE_EXAM)
                alarm.setTimeMillis(new DateTime().withMillis(taskModel.getDATE()).minusDays(examBefore).toDateTime().getMillis());
            else
                alarm.setTimeMillis(new DateTime().withMillis(taskModel.getDATE()).minusDays(taskBefore).toDateTime().getMillis());
            if (alarm.getTimemilisecond() > System.currentTimeMillis()) {
                Bundle bundle = new Bundle();
                bundle.putString("course",taskModel.getCOURSENAME());
                bundle.putInt("color",taskModel.getColor());
                bundle.putString("topic",taskModel.getTOPIC());
                bundle.putString("note",taskModel.getNOTES());
                bundle.putLong("date",taskModel.getDATE());
                alarm.setBundle(bundle);
                alarmList.add(alarm);
                if (setAlarm)
                    setAlarm(ctx, alarm);
            }
        }
        return alarmList;
    }

    private List<Alarm> scheduleMuteAlarmList(boolean setAlarm) {
        List<Alarm> alarmList = new LinkedList<>();
        List<Alarm> lessonList;
        SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean isEnabled = manager.getBoolean("automate", true);
        if (!isEnabled)return alarmList;
        int before = manager.getInt("automate_before", 5);
        int duration = LessonTimeHelper.getLESSON_DURATION(ctx);
            Alarm alarm;
            lessonList = getLessonAlarmList();
            Log.e("#-scheduleMuteAlarm" + setAlarm, "  //    METHOD CALL ++ + + ++ + + alarm lesson Size : " + lessonList.size());
            for (int i = 0; i < lessonList.size(); i++) {
                Log.e("#-scheduleMuteAlarmList", "  //  lesson list : " + new DateTime().withMillis(lessonList.get(i).getTimemilisecond()).toString());
                DateTime time = new DateTime().withMillis(lessonList.get(i).getTimemilisecond()).minusMinutes(before);
                DateTime timeEnd = new DateTime().withMillis(lessonList.get(i).getTimemilisecond()).plusMinutes(duration);
                DateTime time2 = i < lessonList.size() - 1 ? new DateTime().withMillis(lessonList.get(i + 1).getTimemilisecond()).minusMinutes(before) : null;
                if (time.getMillis() > System.currentTimeMillis()) {
                    Log.e("#-scheduleMuteAlarmList", "  //  ())((()ADDED()()(() : " + new DateTime().withMillis(lessonList.get(i).getTimemilisecond()).toString());
                    alarm = new Alarm();
                    alarm.setId(2000 + lessonList.get(i).getId());
                    alarm.setTimeMillis(time.getMillis());
                    alarmList.add(alarm);
                    alarm.setType(Alarm.TYPE_MUTE);
                    if (setAlarm) setAlarm(ctx, alarm);
                    if (time2 != null) {
                        if (time2.getMillis() > timeEnd.getMillis()) {
                            alarm.setId(3000 + lessonList.get(i).getId());
                            alarm.setType(Alarm.TYPE_UNMUTE);
                            alarm.setTimeMillis(timeEnd.getMillis());
                            alarmList.add(alarm);
                            Log.e("#-ScheduleExamTaskAlarm", "  //  ())((()START TIME()()(() : " + new DateTime().withMillis(alarm.getTimemilisecond()).toString());
                            if (setAlarm) setAlarm(ctx, alarm);
                        }
                    } else {
                        alarm.setId(3000 + lessonList.get(i).getId());
                        alarm.setTimeMillis(timeEnd.getMillis());
                        alarm.setType(Alarm.TYPE_UNMUTE);
                        Log.e("#-ScheduleExamTaskAlarm", "  //  ())((()END TIME()()(() : " + new DateTime().withMillis(alarm.getTimemilisecond()).toString());
                        alarmList.add(alarm);
                        if (setAlarm) setAlarm(ctx, alarm);
                    }

                }
            }
        return alarmList;
    }

    public static List<Integer> sortedEnabledDay(Context ctx) {
        int D = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        List<Integer> list = LessonTimeHelper.getEnabledDaylist(ctx);
        List<Integer> a = new LinkedList<>();
        List<Integer> b = new LinkedList<>();
        for (Integer i : list) {
            if (D > i)
                a.add(i);
            else
                b.add(i);
        }
        b.addAll(a);
        return b;
    }
}
