package com.yotor.solution.mytimetable.portal;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.graphics.drawable.DrawableCompat;

//import com.crashlytics.android.Crashlytics;
import com.yotor.solution.mytimetable.R;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Util {
    public static final int RateInterval = 172800000;

    public static int getfLesontime(int h, int m) {
        String min;
        String Hour;
        min = Integer.toString(m);
        if (m < 10)
            min = "0" + min;
        Hour = Integer.toString(h);
        return Integer.parseInt(Hour.concat(min));
    }

    public static String concatInt(int a, int b) {
        return (a + "" + b);

    }

    public static String ArrayToString(String[] array) {
        StringBuilder data = new StringBuilder();
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (i != array.length - 1)
                    data.append(array[i]).append(",");
                else {
                    data.append(array[i]);
                }

            }
        }
        return data.toString();
    }

    public static String[] stringToArray(String data) {
        return data.split(",");
    }

    static String ArrayToReadableStr(String[] array, String spacer) {
        StringBuilder data = new StringBuilder();
        if (array != null) {
            for (String anArray : array) {
                data.append(anArray.concat(spacer));

            }
        }
        return data.toString();
    }

    public static String intToStr(int i) {
        String s = "";
        try {
            s = Integer.toString(i);
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
        return s;
    }

    public static int strToInt(String s) {
        int i = 0;
        try {
            if (!s.isEmpty())
                i = Integer.parseInt(s);
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
        return i;
    }

    public static long strToLong(String s) {
        long i = 0;
        try {
            if (!s.isEmpty())
                i = Long.parseLong(s);
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
        return i;
    }

    public static Drawable getTintDrawable(Drawable d, int Color) {
        Drawable drawable = DrawableCompat.wrap(d);
        drawable.mutate();
        DrawableCompat.setTint(drawable, Color);
        return drawable;

    }




    public static int getColorfromindex(Context ctx, int position) {
        String h = ctx.getResources().getStringArray(R.array.Colors)[position];
        return Color.parseColor(h);
    }

    public static float getGradeMultiplyer(int i) {
        switch (i) {
            case 1:
            case 0:
                return 4.0f;
            case 2:
                return 3.75f;
            case 3:
                return 3.5f;
            case 4:
                return 3.0f;
            case 5:
                return 2.75f;
            case 6:
                return 2.5f;
            case 7:
                return 2.0f;
            case 8:
                return 1.75f;
            case 9:
                return 1.0f;
            default:
                return 0;


        }
    }

    public static int getCellWidth(int deviceWidth, int Days) {
        return deviceWidth / (Days);
    }

    public static int getCellHight(int deviceHight, int Lesson) {
        return (deviceHight - 200) / (Lesson);

    }

    public static String[] getDaysRowTitle(String[] enabledDay) {
        List<String> row = new LinkedList<>();
        row.add(" ");
        Calendar cal = Calendar.getInstance();
        for (String s : enabledDay) {
            cal.set(Calendar.DAY_OF_WEEK, strToInt(s));
            row.add(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
        }
        return row.toArray(new String[]{});
    }

    public static String IN(int size) {
        StringBuilder s = new StringBuilder("(");
        for (int i = 0; i < size; i++) {
            if (size - 1 == i)
                s.append("?");
            else s.append("?,");
        }
        return s.append(")").toString();
    }

    public static synchronized void loadLanguage(Context ctx, String string) {
        if (string == null)
            string = ShardPref.getString(ctx, "language", Locale.getDefault().getLanguage());
        else
            ShardPref.saveValue(ctx, "language", string);
        Configuration configuration = ctx.getResources().getConfiguration();
        Locale locale = new Locale(string);
        Locale.setDefault(locale);
        configuration.locale = locale;
        ctx.getResources().updateConfiguration(configuration, ctx.getResources().getDisplayMetrics());

    }

    public static void loadAppTheme(Activity activity, String themeIndex) {
        switch (themeIndex) {
            case "1":
                activity.setTheme(R.style.AppTheme_Green);
                break;
            case "2":
                activity.setTheme(R.style.AppTheme_Brown);
                break;
            case "3":
                activity.setTheme(R.style.AppTheme_Pink);
                break;
            default:
                activity.setTheme(R.style.AppTheme_Blue);
        }
    }
    public static void loadDialogTheme(Activity activity) {
        String theme = ShardPref.getString(activity, "themes", "0");
        switch (theme) {
            case "1":
                activity.setTheme(R.style.DialogActivity_Green);
                break;
            case "2":
                activity.setTheme(R.style.DialogActivity_Brown);
                break;
            case "3":
                activity.setTheme(R.style.DialogActivity_Pink);
                break;
            default:
                activity.setTheme(R.style.DialogActivity_Blue);
        }
    }

    public static void applyConfig(Activity activity) {
        try {
            if (activity == null) return;
            String theme = ShardPref.getString(activity, "themes", "0");
            Util.loadAppTheme(activity, theme);
            loadLanguage(activity, null);
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
    }

    public static void RateApp(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent rater = new Intent(Intent.ACTION_VIEW, uri);
            try {
                context.startActivity(rater);
            }catch (Exception ignored){
                context.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id="+context.getPackageName())));
            }
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
    }

    public static boolean rateAppDialog(final Activity activity, final boolean forceToClose, boolean forceToShow) {
        if (!forceToShow) {
            if (ShardPref.getBool(activity, "israted", false)) return false;
            long lastShow = ShardPref.getLong(activity, "last_rate_show", System.currentTimeMillis());
            long now = System.currentTimeMillis();
            if ((now - lastShow) < RateInterval) return false;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.rate_app_dialog, null, false);
        final AppCompatRatingBar ratingBar = view.findViewById(R.id.rate_bar);
        dialog.setView(view);
        dialog.setNegativeButton(activity.getString(R.string.later), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (forceToClose)
                    activity.finish();
            }
        });
        dialog.setPositiveButton(activity.getString(R.string.ok_rate), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ShardPref.saveValue(activity, "israted", true);
                RateApp(activity);
            }
        });
        dialog.show();
        ratingBar.setRating(0);
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.ratingbaranim);
        ratingBar.setAnimation(animation);
        new CountDownTimer(1500, 300) {
            @Override
            public void onTick(long l) {
                ratingBar.setRating(ratingBar.getRating() + 1);
            }

            @Override
            public void onFinish() {
                ratingBar.setRating(5);
            }
        }.start();
    return true;
    }

}