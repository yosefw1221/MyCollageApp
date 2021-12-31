package com.yotor.solution.mytimetable.ui.home.recycler;

import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.android.gms.ads.NativeExpressAdView;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.ExamTaskActivity;
import com.yotor.solution.mytimetable.portal.MyCourseActivity;
import com.yotor.solution.mytimetable.portal.mywidget.WidgetData;
import com.yotor.solution.mytimetable.portal.mywidget.WidgetDataModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.security.auth.callback.Callback;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    public static final int VIEW_TYPE_TIMER_CARD = 0;
    public static final int VIEW_TYPE_UPCOMING_CARD = 1;
    public static final int VIEW_TYPE_HINT_CARD = 2;
    public static final int VIEW_TYPE_BASIC_CARD = 5;
    public static final int VIEW_TYPE_TIMELINE_CARD = 3;
    public static final int VIEW_TYPE_ADS_CARD = 4;
    public static final int VIEW_TYPE_NET_ERROR_CARD = 4;
    private Context context;
    private List<RecyclerDataModel> modelList;

    public RecyclerAdapter(Context context, List<RecyclerDataModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_TIMER_CARD:
                return new LessonTimerViewHolder(LayoutInflater.from(context).inflate(R.layout.card_lesson_timer, parent, false));
            case VIEW_TYPE_UPCOMING_CARD:
                return new UpcomingTaskViewHolder(LayoutInflater.from(context).inflate(R.layout.card_upcoming_task, parent, false));
            case VIEW_TYPE_HINT_CARD:
                return new ActionViewHolder(LayoutInflater.from(context).inflate(R.layout.card_action, parent, false));
            case VIEW_TYPE_BASIC_CARD:
                return new BasicCardViewHolder(LayoutInflater.from(context).inflate(R.layout.card_basic_action, parent, false));
            case VIEW_TYPE_TIMELINE_CARD:
                break;
            default:
                return new AdsCardViewHolder(LayoutInflater.from(context).inflate(R.layout.card_native_ads,parent,false));
        }
        return null;

    }

    @Override
    public int getItemViewType(int position) {
        return modelList.get(position).getType();
    }

    public void setCallback(Callback callback) {
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class LessonTimerViewHolder extends RecyclerViewHolder {

        public LessonTimerViewHolder(@NonNull View itemView) {
            super(itemView);
            TickTockView tickTockView = itemView.findViewById(R.id.card_timer_timer);
            AppCompatTextView title = itemView.findViewById(R.id.card_timer_title);
            AppCompatTextView subtitle = itemView.findViewById(R.id.card_timer_subtitle);
            List<WidgetDataModel> list = new WidgetData(context).getLessons(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            List<WidgetDataModel> upcoming = new LinkedList<>();
            Log.e("Start :", "============LessonTimerViewHolder============");
            StringBuilder upcomingClass = new StringBuilder();
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            for (WidgetDataModel widgetDataModel : list) {
                if (widgetDataModel.getEndTime().getMillis() > System.currentTimeMillis()) {
                    upcoming.add(widgetDataModel);
                    upcomingClass.append(" ▪ ").append(widgetDataModel.getCourseName()).append(" ").append(format.format(widgetDataModel.getStartTime().toDate()))
                            .append(" - ").append(format.format(widgetDataModel.getEndTime().toDate())).append("\n");
                }
            }
            if (upcoming.size() > 0) {
                if (upcoming.get(0).getStartTime().getMillis() <= System.currentTimeMillis()) {
                    title.setText(String.format(context.getString(R.string.timer_card_now), upcoming.get(0).getCourseName()));
                    Calendar end = Calendar.getInstance();
                    Calendar now = Calendar.getInstance();
                    now.setTime(upcoming.get(0).getStartTime().toDate());
                    end.setTime(upcoming.get(0).getEndTime().toDate());
                    tickTockView.setCircleDuration(true);
                    tickTockView.setCounterDirection(true);
                    tickTockView.start(now, end);
                    subtitle.setText(upcomingClass.toString());
                    subtitle.setMaxLines(6);
                    subtitle.setMovementMethod(new ScrollingMovementMethod());
                    Log.e("Start : " + now.getTime().toString() + " end " + end.getTime().toString(), "============NOW============");
                } else {
                    Calendar end = Calendar.getInstance();
                    end.setTime(upcoming.get(0).getStartTime().toDate());
                    tickTockView.setCircleDuration(false);
                    tickTockView.setCounterDirection(false);
                    tickTockView.start(end);
                    title.setText(String.format(context.getString(R.string.timer_card_upcoming), upcoming.get(0).getCourseName()));
                    subtitle.setText(upcomingClass.toString());
                    subtitle.setMaxLines(6);
                    subtitle.setMovementMethod(new ScrollingMovementMethod());
                    Log.e("end " + end.getTime().toString(), "=====UpCOMING=======");
                }
                Calendar end = Calendar.getInstance();
                Calendar now = Calendar.getInstance();
                now.setTime(upcoming.get(0).getStartTime().toDate());
                end.setTime(upcoming.get(0).getEndTime().toDate());
                tickTockView.setOnTickListener(new TickTockView.OnTickListener() {
                    @Override
                    public String getText(long timeRemainingInMillis) {
                        int sec = (int) (timeRemainingInMillis / 1000);
                        int s = sec % 60;
                        int min = sec / 60 % 60;
                        int hou = sec / 60 / 60;
                        if (hou > 0)
                            return String.format("%d:%02d:%02d", hou, min, s);
                        return String.format("%02d:%02d", min, s);
                    }
                });

            } else {
                title.setText(R.string.no_upcoming_class);
                subtitle.setText(R.string.no_upcoming_class_message);
            }
        }

        @Override
        public void onBind(int pos) {
            super.onBind(pos);
            Log.e("onBind :", "============LessonTimerViewHolder  onBind ============");
        }
    }

    class UpcomingTaskViewHolder extends RecyclerViewHolder {
        AppCompatTextView title;
        AppCompatTextView msg;
        AppCompatButton btn;

        public UpcomingTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd", Locale.getDefault());
            title = itemView.findViewById(R.id.upcoming_card_title);
            msg = itemView.findViewById(R.id.upcoming_card_msg);
            btn = itemView.findViewById(R.id.upcoming_card_btn);
            List<WidgetDataModel> data = new WidgetData(context).getExamTaskList();
            StringBuilder builder = new StringBuilder();
            for (WidgetDataModel model : data) {
                builder.append("▪ ").append(model.getCourseName()).append(context.getString(R.string.on)).append(dateFormat.format(model.getStartTime().toDate())).append("\n");
            }
            if (data.size() > 0)
                msg.setText(builder.toString());
            else msg.setText(R.string.no_upcoming_examtask);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ExamTaskActivity.class);
                   //Analytics.logClickEvent("Home_upcoming_card", "add examTask btn");
                    context.startActivity(intent);
                }
            });
        }
    }

    class ActionViewHolder extends RecyclerViewHolder {
        AppCompatTextView title;
        AppCompatTextView msg;
        AppCompatButton actionBtn;
        AppCompatImageView icon;

        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.action_card_title);
            msg = itemView.findViewById(R.id.action_card_msg);
            actionBtn = itemView.findViewById(R.id.action_card_btn);
            icon = itemView.findViewById(R.id.action_card_icon);
        }

        @Override
        public void onBind(final int pos) {
            super.onBind(pos);
            final ActionModel actionModel = modelList.get(pos).getActionModel();
            title.setText(modelList.get(pos).getTitle());
            msg.setText(modelList.get(pos).getSubtitle());
            icon.setImageDrawable(modelList.get(pos).getActionIcon());
            actionBtn.setText(modelList.get(pos).getActionTxt());
            actionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActionModel.performAction(context, actionModel);
                   //Analytics.logClickEvent("Home_Action_card", "action btn " + modelList.get(pos).getTitle());
                }
            });
        }
    }

    class BasicCardViewHolder extends RecyclerViewHolder {

        public BasicCardViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.findViewById(R.id.basic_course).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, MyCourseActivity.class));
                   //Analytics.logClickEvent("Home_Basic_card", "course btn");
                }
            });
            itemView.findViewById(R.id.basic_examtask).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   //Analytics.logClickEvent("Home_Basic_card", "exam btn");
                    context.startActivity(new Intent(context, ExamTaskActivity.class));
                }
            });
        }
    }

class AdsCardViewHolder extends RecyclerViewHolder {
    CardView cardView;
    public AdsCardViewHolder(@NonNull View itemView) {
        super(itemView);
         cardView = (CardView) itemView;
        if (cardView.getChildCount() > 0) {
            cardView.removeAllViews();
        }
    }

    @Override
    public void onBind(int pos) {
        super.onBind(pos);
        //NativeExpressAdView adView = modelList.get(pos).getExpressAdView();
        //Log.e("ADS CARD"," ads = "+(adView!=null)+" haschild "+cardView.getChildCount());
        ///if (adView!=null&&cardView.getChildCount()==0)
         //cardView.addView(adView);
    }
}
    }