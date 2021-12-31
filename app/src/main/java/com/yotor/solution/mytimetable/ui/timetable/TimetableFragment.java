package com.yotor.solution.mytimetable.ui.timetable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

//import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.AppConfig;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.ShardPref;
import com.yotor.solution.mytimetable.portal.TimetableChangeListener;
import com.yotor.solution.mytimetable.portal.Util;
import com.yotor.solution.mytimetable.portal.ui.GridTimeTable;
import com.yotor.solution.mytimetable.qrcode.QrCodeGenerator;
import com.yotor.solution.mytimetable.qrcode.QrCodeScanner;
import com.yotor.solution.mytimetable.ui.Setting.SettingsHeaderActivity;

@KeepName
public class TimetableFragment extends Fragment {
    private final TableLayout.LayoutParams params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private TableLayout tableLayout;
    private GridTimeTable timeTable;

    public static AlertDialog.Builder showShareDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.share_time_dialog, null, false);
        view.findViewById(R.id.timetable_share_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, QrCodeGenerator.class));
            }
        });
        view.findViewById(R.id.timetable_import_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, QrCodeScanner.class));
            }
        });
        builder.setView(view);
        return builder;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (AppConfig.terminateApp()) {
            View root = inflater.inflate(R.layout.terminate_layout, container, false);
            ((AppCompatTextView) root.findViewById(R.id.terminate_msg)).setText(AppConfig.terminateMsg());
            return root;
        }
        Util.applyConfig(getActivity());
        View root = inflater.inflate(R.layout.fragment_timetable, container, false);
        try {
           //Analytics.trackScreen(getActivity(), "TIMETABLE");
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            tableLayout = root.findViewById(R.id.timetable);
            FloatingActionButton fab = root.findViewById(R.id.timetable_fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showShareDialog(getContext()).show();
                }
            });
            timeTable = new GridTimeTable(getContext(), point.x, point.y, new TimetableChangeListener() {
                @Override
                public void onChanged() {
                   //Analytics.logClickEvent("Timetable", "FAB");
                    // new Update().execute();
                    UpdateTimeTable();
                }
            });
            // UpdateTimeTable();
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
        showHint();
        return root;
    }

    private void UpdateTimeTable() {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tableLayout.removeAllViews();
                    tableLayout.addView(timeTable.getHeader(), params);
                    for (TableRow row : timeTable.getLessonsRows()) {
                        tableLayout.addView(row);
                    }
                }
            });
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!AppConfig.terminateApp())
            UpdateTimeTable();
    }

    public void showHint() {
        if (ShardPref.getBool(getContext(), "timetableHint", false)) return;
        androidx.appcompat.app.AlertDialog.Builder a = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        a.setTitle("Timetable Setting");
        a.setTitle(R.string.timetable_setting);
        a.setMessage(R.string.action_card_timetable);
        a.setPositiveButton(R.string.go_to_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ShardPref.saveValue(getContext(), "timetableHint", true);
                startActivity(new Intent(getContext(), SettingsHeaderActivity.class));
            }
        });
        a.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ShardPref.saveValue(getContext(), "timetableHint", true);
            }
        });
        a.show();
    }

    /*class Update extends AsyncTask<Void, TableRow, List<TableRow>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<TableRow> doInBackground(Void... voids) {
            List<TableRow> rows = new LinkedList<>();
            rows.add(timeTable.getHeader());
            rows.addAll(timeTable.getLessonsRows());
            return rows;
        }

        @Override
        protected void onProgressUpdate(TableRow... values) {
            super.onProgressUpdate(values);
            tableLayout.addView(values[0], params);
            Log.e("onProgressUpdate ", "   size " + values.length);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(List<TableRow> tableRows) {
            super.onPostExecute(tableRows);
            tableLayout.removeAllViews();
            for (TableRow row : tableRows) {
                tableLayout.addView(row);
            }
        }
    }*/

}