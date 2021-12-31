package com.yotor.solution.mytimetable.portal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

//import com.crashlytics.android.Crashlytics;
import com.yotor.solution.mytimetable.portal.Util;
import com.yotor.solution.mytimetable.portal.model.Course;
import com.yotor.solution.mytimetable.portal.model.ExamTaskModel;
import com.yotor.solution.mytimetable.portal.model.GpaList;
import com.yotor.solution.mytimetable.portal.model.GradeModel;
import com.yotor.solution.mytimetable.portal.model.Lesson;
import com.yotor.solution.mytimetable.ui.Setting.LessonTimeHelper;
import com.yotor.solution.mytimetable.ui.notifications.AlarmUtil;

import java.util.LinkedList;
import java.util.List;


public class StudentDB extends SQLiteOpenHelper {
    public static final String TABLE4 = "EXAM";
    public static final int MAXIMUM_LESSON_DAYS = 7;
    private static final String DB_NAME = "USER_DB";
    private static final String TABLE2 = "LESSON";
    private static final String TABLE3 = "GPA";
    private static final String TABLE5 = "GPA_LIST";
    private static final int MAXIMUM_LESSON = 15;
    private static final String[] COURSE_COLUMN = new String[]{"courseID", "courseCode", "courseName","abbri", "ects", "color"};
    private static final String[] LESSON_COLUMN = new String[]{"id", "courseID", "startTime", "endTime", "lesson", "day"};
    private static final String[] GPA_COLUMN = new String[]{"id", "courseID", "courseName", "ects", "gradePos", "color", "name"};
    private static final String[] GPA_LIST_COLUMN = new String[]{"name", "grade"};
    private static final String[] EXAM_COLUMN = new String[]{"id", "topic", "courseID", "courseName", "date", "notes", "isDone", "type"};
    private static final String TABLE = "COURSE";

    public Context ctx;
    public StudentDB(Context context) {
        super(context, DB_NAME, null, 1);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + "(courseID INTEGER PRIMARY KEY AUTOINCREMENT,courseCode TEXT,courseName TEXT UNIQUE,abbri TEXT,ects TEXT,color TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE2 + "(id INTEGER PRIMARY KEY AUTOINCREMENT ,courseID INTEGER,startTime TEXT,endTime TEXT,lesson TEXT,day TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE3 + "(id INTEGER PRIMARY KEY AUTOINCREMENT ,courseID INTEGER ,courseName TEXT,ects TEXT,gradePos INTEGER,color INTEGER,name TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE4 + "(id INTEGER PRIMARY KEY AUTOINCREMENT ,topic TEXT,courseID INTEGER,courseName TEXT,date TEXT,notes TEXT,isDone INTEGER,type INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE5 + "(name TEXT PRIMARY KEY,grade FLOAT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean createCourse(Course course) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            if (course.getID() != 0)
                values.put("courseID", course.getID());
            values.put("courseCode", course.getCODE() != null ? course.getCODE() : "");
            values.put("courseName", course.getNAME() != null ? course.getNAME() : "");
            values.put("abbri", course.getABBREVIATION() != null ? course.getABBREVIATION() : "");
            values.put("ects", Util.intToStr(course.getECTS()));
            values.put("color", Util.intToStr(course.getCOLOR()));
            db.insertOrThrow(TABLE, null, values);
            return true;
        } catch (Exception e) {
            Toast.makeText(ctx, "create Subject error" + e.getMessage(), Toast.LENGTH_SHORT).show();
           //Crashlytics.logException(e);
            return false;
        }

    }

    public Course getCourse(String CourseName) {
        Course course = new Course();
        if (CourseName.isEmpty() || CourseName.equalsIgnoreCase("null"))
            return null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.query(TABLE, COURSE_COLUMN, "courseName=?", new String[]{CourseName}, null, null, null);
            if (c.moveToFirst()) {
                course.setID(c.getInt(0));
                course.setCODE(c.getString(1));
                course.setNAME(c.getString(2));
                course.setABBREVIATION(c.getString(3));
                course.setECTS(c.getString(4));
                course.setCOLOR(Util.strToInt(c.getString(5)));

            }
            c.close();
        } catch (Exception e) {
            Toast.makeText(ctx, "Error when loading course" + e.toString(), Toast.LENGTH_SHORT).show();
           //Crashlytics.logException(e);
            return null;
        }
        return course;
    }

    public Course getCourse(int CourseId) {
        Course course = new Course();
        if (CourseId == 0)
            return null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.query(TABLE, COURSE_COLUMN, "courseID=?", new String[]{Util.intToStr(CourseId)}, null, null, null);
            if (c.moveToFirst()) {
                course.setID(c.getInt(0));
                course.setCODE(c.getString(1));
                course.setNAME(c.getString(2));
                course.setABBREVIATION(c.getString(3));
                course.setECTS(c.getString(4));
                course.setCOLOR(Util.strToInt(c.getString(5)));

            }
            c.close();
        } catch (Exception e) {
            Toast.makeText(ctx, "get subject error " + e.toString(), Toast.LENGTH_SHORT).show();
           //Crashlytics.logException(e);
            return null;
        }

        return course;
    }

    public boolean deleteCourse(int courseID) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            if (courseID == -1) {
                db.delete(TABLE, null, null);
                deleteAllLesson(courseID);
            }
            db.delete(TABLE, "courseID=?", new String[]{Util.intToStr(courseID)});
            deleteAllLesson(courseID);
            return true;
        } catch (Exception e) {
           //Crashlytics.logException(e);
            return false;
        }
    }

    public List<Course> getAllCourses() {
        List<Course> courseList = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        Course course;
        try {
            Cursor c = db.query(TABLE, COURSE_COLUMN, null, null, null, null, "courseName");
            if (c != null)
                if (c.moveToFirst()) {
                    do {
                        course = new Course();
                        course.setID(c.getInt(0));
                        course.setCODE(c.getString(1));
                        course.setNAME(c.getString(2));
                        course.setABBREVIATION(c.getString(3));
                        course.setECTS(c.getString(4));
                        course.setCOLOR(Util.strToInt(c.getString(5)));
                        courseList.add(course);
                    } while (c.moveToNext());
                }
            if (c != null) c.close();
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
        return courseList;
    }

    public boolean updateCourse(Course course, int courseID) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("courseCode", course.getCODE());
            values.put("courseName", course.getNAME());
            values.put("ects", course.getECTS());
            values.put("abbri",course.getABBREVIATION());
            values.put("color", Util.intToStr(course.getCOLOR()));
            db.update(TABLE, values, "courseID=?", new String[]{Util.intToStr(courseID)});
            return true;
        } catch (Exception e) {
           //Crashlytics.logException(e);
            return false;
        }
    }

    private void createEmptyLessons() {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        int MAXIMUM_DAY = 7;
        try {
            for (int i = 0; i < MAXIMUM_LESSON; i++) {
                for (int j = 0; j < MAXIMUM_DAY; j++) {
                    values.put("courseID", "");
                    values.put("lesson", i + 1);
                    values.put("day", j + 1);
                    db.insertOrThrow(TABLE2, null, values);
                }
            }
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
    }

    public List<List<Lesson>> getAllLessons() {
        String[] enabledDay = LessonTimeHelper.getEnabledDay(ctx);
        SQLiteDatabase db = getReadableDatabase();
        Lesson lesson;
        List<Lesson> lessons;
        List<List<Lesson>> lessonList = new LinkedList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE2 + " WHERE day IN" + Util.IN(enabledDay.length), enabledDay);
        try {
            if (cursor.moveToFirst()) {
                for (int i = 0; i < LessonTimeHelper.getTotalLesson(ctx); i++) {
                    lessons = new LinkedList<>();
                    for (int j = 0; j < enabledDay.length; j++) {
                        lesson = new Lesson();
                        lesson.setID(cursor.getInt(0));
                        lesson.setCOURSE(getCourse(cursor.getInt(1)));
                        lesson.setLESSON(cursor.getString(4) == null ? 0 : Util.strToInt(cursor.getString(4)));
                        lesson.setDAY(cursor.getString(5) == null ? 0 : Util.strToInt(cursor.getString(5)));
                        lessons.add(lesson);
                        cursor.moveToNext();
                    }
                    lessonList.add(lessons);
                }
                cursor.close();
            } else {
                createEmptyLessons();
                getAllLessons();
            }
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
        return lessonList;
    }

    public void updateLesson(Lesson lesson) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues value = new ContentValues();
            if (lesson.getCOURSE() == null) {
                value.put("courseID", lesson.getCOURSE_ID());
                /*alarmUtil.scheduleLessonAlarmList(true);
                alarmUtil.scheduleMuteAlarmList(true);*/
            } else
                value.put("courseID", lesson.getCOURSE().getID());
                AlarmUtil.updateAlarmList(ctx);
            db.update(TABLE2, value, "id=?", new String[]{Util.intToStr(lesson.getID())});
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
    }

    public boolean deleteLesson(int lessonId) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues value = new ContentValues();
            value.put("courseID", "");
            db.update(TABLE2, value, "id=?", new String[]{Util.intToStr(lessonId)});
            AlarmUtil.updateAlarmList(ctx);
            return true;
        } catch (Exception e) {
           //Crashlytics.logException(e);
            return false;
        }
    }

    public boolean deleteAllLesson(int courseID) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues value = new ContentValues();
            value.put("courseID", "");
            if (courseID != -1)
                db.update(TABLE2, value, "courseID=?", new String[]{Util.intToStr(courseID)});
            else
                db.update(TABLE2, value, null, null);
            AlarmUtil.updateAlarmList(ctx);
            return true;
        } catch (Exception e) {
           //Crashlytics.logException(e);
            return false;
        }
    }

    public Lesson getLesson(int lessonId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE2, LESSON_COLUMN, "id=?", new String[]{Util.intToStr(lessonId)}, null, null, null);
        Lesson lesson = new Lesson();
        if (c.moveToFirst()) {
            lesson.setID(c.getInt(0));
            lesson.setCOURSE(getCourse(c.getInt(1)));
            lesson.setSTART_TIME(c.getString(2) == null ? 0 : Util.strToInt(c.getString(2)));
            lesson.setEND_TIME(c.getString(3) == null ? 0 : Util.strToInt(c.getString(3)));
            lesson.setLESSON(c.getString(4) == null ? 0 : Util.strToInt(c.getString(4)));
            lesson.setDAY(c.getString(5) == null ? 0 : Util.strToInt(c.getString(5)));
        }
        c.close();
        return lesson;
    }

    public List<Lesson> getLessonOfDay(int day) {
        List<Lesson> lessonList = new LinkedList<>();
        Lesson lesson;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c;
        if (day == -1)
            c = db.query(TABLE2, LESSON_COLUMN, null, null, null, null, null);
        else
            c = db.query(TABLE2, LESSON_COLUMN, "day=?", new String[]{Util.intToStr(day)}, null, null, null);
        if (c.moveToFirst()) {
            do {
                lesson = new Lesson();
                if (c.getInt(1) == 0 || c.getString(4) == null)
                    continue;
                lesson.setID(c.getInt(0));
                lesson.setCOURSE(getCourse(c.getInt(1)));
                lesson.setSTART_TIME(c.getString(2) == null ? 0 : Util.strToInt(c.getString(2)));
                lesson.setEND_TIME(c.getString(3) == null ? 0 : Util.strToInt(c.getString(3)));
                lesson.setLESSON(c.getString(4) == null ? 0 : Util.strToInt(c.getString(4)));
                lesson.setDAY(c.getString(5) == null ? 0 : Util.strToInt(c.getString(5)));
                lessonList.add(lesson);
            } while (c.moveToNext());

        }
        c.close();
        return lessonList;
    }

    public List<GpaList> getGpaList() {
        SQLiteDatabase db = getReadableDatabase();
        List<GpaList> gpaLists = new LinkedList<>();
        GpaList gpa;
        Cursor c = db.query(TABLE5, GPA_LIST_COLUMN, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                gpa = new GpaList();
                gpa.setName(c.getString(0));
                gpa.setGpa(c.getFloat(1));
                gpaLists.add(gpa);
            } while (c.moveToNext());
        }
        c.close();
        return gpaLists;
    }

    public boolean deleteGpa(String name) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE5, "name=?", new String[]{name});
            db.delete(TABLE3, "name=?", new String[]{name});
            return true;
        } catch (Exception e) {
           //Crashlytics.logException(e);
            return false;
        }
    }

    public boolean createUpdateCourseGrade(List<GradeModel> gradeModels, GpaList grade, boolean isUpdate) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues v = new ContentValues();
            v.put("name", grade.getName());
            v.put("grade", grade.getGpa());
            if (isUpdate)
                db.update(TABLE5, v, "name=?", new String[]{grade.getName()});
            else
                db.insert(TABLE5, null, v);
            for (GradeModel gradeModel : gradeModels) {
                ContentValues values = new ContentValues();
                values.put("courseID", gradeModel.getCOURSE_ID());
                values.put("courseName", gradeModel.getCOURSENAME());
                values.put("ects", Util.intToStr(gradeModel.getECTS()));
                values.put("gradePos", gradeModel.getSelectedGRADE());
                values.put("color", gradeModel.getCOLOR());
                values.put("name", grade.getName());
                if (gradeModel.getMODEL_ID() != 0) {
                    if (db.update(TABLE3, values, "id=?", new String[]{Util.intToStr(gradeModel.getMODEL_ID())}) == 0) {
                        values.put("id", gradeModel.getMODEL_ID());
                        db.insertOrThrow(TABLE3, null, values);
                    }
                } else
                    db.insertOrThrow(TABLE3, null, values);
            }
            return true;
        } catch (Exception e) {
           //Crashlytics.logException(e);
            return false;
        }


    }

    public List<GradeModel> getCourseGrade(String name) {
        List<GradeModel> list = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        try {
            GradeModel model;
            Cursor c = db.query(TABLE3, GPA_COLUMN, "name=?", new String[]{name}, null, null, "ects");
            if (c.moveToFirst()) {
                do {
                    model = new GradeModel();
                    model.setMODEL_ID(c.getInt(0));
                    model.setCOURSE_ID(c.getInt(1));
                    model.setCOURSENAME(c.getString(2));
                    model.setECTS(Util.strToInt(c.getString(3)));
                    model.setSelectedGRADE(c.getInt(4));
                    model.setCOLOR(c.getInt(5));
                    list.add(model);
                } while (c.moveToNext());

            }
            c.close();
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
        return list;
    }

    public List<GradeModel> getAllCourse() {
        List<GradeModel> list = new LinkedList<>();
        GradeModel model;
        for (Course course : getAllCourses()) {
            model = new GradeModel();
            model.setCOURSE_ID(course.getID());
            model.setCOURSENAME(course.getNAME());
            model.setECTS(course.getECTS());
            model.setSelectedGRADE(1);
            model.setCOLOR(course.getCOLOR());
            list.add(model);
        }
        return list;
    }

    public List<ExamTaskModel> getAllTaskExam(int id) {
        List<ExamTaskModel> examTaskModelList = new LinkedList<>();
        ExamTaskModel examTaskModel;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c;
        if (id != 0)
            c = db.query(TABLE4, EXAM_COLUMN, "id=?", new String[]{Util.intToStr(id)}, null, null, null);
        else
            c = db.query(TABLE4, EXAM_COLUMN, null, null, null, null, "date");
        if (c.moveToFirst()) {
            do {
                examTaskModel = new ExamTaskModel();
                examTaskModel.setID(c.getInt(0));
                examTaskModel.setTOPIC(c.getString(1));
                examTaskModel.setCOURSE_ID(c.getInt(2));
                examTaskModel.setCOURSENAME(c.getString(3));
                examTaskModel.setDATE(Util.strToLong(c.getString(4)));
                examTaskModel.setNOTES(c.getString(5));
                examTaskModel.setIsDONE(c.getInt(6) != 0);
                examTaskModel.setTYPE(c.getInt(7));
                Course course = getCourse(c.getInt(2));
                if (course != null) {
                    examTaskModel.setColor(course.getCOLOR());
                }
                examTaskModelList.add(examTaskModel);
            } while (c.moveToNext());
        }      c.close();
        return examTaskModelList;
    }

    public void AddExamTask(ExamTaskModel model, boolean isUpdate) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("topic", model.getTOPIC());
        values.put("courseID", model.getCOURSE_ID());
        values.put("courseName", model.getCOURSENAME());
        values.put("date", Long.toString(model.getDATE()));
        values.put("notes", model.getNOTES() == null ? "" : model.getNOTES());
        values.put("isDone", model.isDONE() ? 1 : 0);
        values.put("type", model.getTYPE());
        if (isUpdate) {
            db.update(TABLE4, values, "id=?", new String[]{Util.intToStr(model.getID())});
        } else
            db.insertOrThrow(TABLE4, null, values);
        AlarmUtil.updateAlarmList(ctx);
    }

    public void setExamDoneUpdate(int id, boolean isDone, @Nullable ExamTaskModel model) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isDone", !((model == null) ? isDone : model.isDONE()) ? 0 : 1);
        db.update(TABLE4, values, "id=?", new String[]{Util.intToStr(model == null ? id : model.getID())});
        AlarmUtil.updateAlarmList(ctx);

    }

    public boolean deleteExamTask(int id) {
        SQLiteDatabase db = getWritableDatabase();
        AlarmUtil.updateAlarmList(ctx);
        return db.delete(TABLE4, "id=?", new String[]{Util.intToStr(id)}) != 0;
    }


}
