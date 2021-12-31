package com.yotor.solution.mytimetable.qrcode;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScanner extends AppCompatActivity{
    ZXingScannerView scannerView;
    AppCompatTextView hintTv;
    AppCompatCheckedTextView chtv1, chtv2, chtv3;
    AppCompatToggleButton btn;
    LinearLayout statusLayout;
    boolean setting, course, lesson;
    int current;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.applyConfig(this);
        setContentView(R.layout.qrcode_scanner);
        scannerView = findViewById(R.id.qr_scanner);
        hintTv = findViewById(R.id.qr_code_status_tv);
        chtv1 = findViewById(R.id.qr_code_chtv_1);
        chtv2 = findViewById(R.id.qr_code_chtv_2);
        chtv3 = findViewById(R.id.qr_code_chtv_3);
        btn = findViewById(R.id.qr_code_button);
        statusLayout = findViewById(R.id.qr_code_status_layout);
        List<BarcodeFormat> formatList = new LinkedList<>();
        formatList.add(BarcodeFormat.QR_CODE);
        scannerView.setFormats(formatList);
        scannerView.setFocusableInTouchMode(true);
        if (Build.MANUFACTURER.equalsIgnoreCase("HUAWEI"))
            scannerView.setAspectTolerance(0.5f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23)
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 99);
               //Analytics.logAppEvent("QrCodeScanner", "camera permission request");
                return;
            }
       //Analytics.logAppEvent("QrCodeScanner", "start_scanning");
        scannerView.startCamera();
        scannerView.setResultHandler(resultHandler());

    }public ZXingScannerView.ResultHandler resultHandler(){
        return new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                if (result != null) {
                   //Analytics.logAppEvent("QrCodeScanner_scanned_result", result.getText());
                    new ScannerTask().execute(result.getText());
                } else {
                    btn.setTextOff(getString(R.string.try_again));
                   //Analytics.logAppEvent("QrCodeScanner_scanned_result", "null error");
                }
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            hintTv.setText(R.string.camera_permision_alert);
           //Analytics.logAppEvent("QrCodeScanner_Camera_Access", "result_denied");
        } else {
            hintTv.setText(getString(R.string.qr_code_hint));
           //Analytics.logAppEvent("QrCodeScanner_Camera_Access", "result_Granted");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    private void showPopUpAnimation(int type) {
        Animation popup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.popupanim);
        switch (type) {
            case 1:
                chtv1.startAnimation(popup);
                break;
            case 2:
                chtv2.startAnimation(popup);
                break;
            case 3:
                chtv3.startAnimation(popup);
                break;

        }
    }

    private void clearAnimations() {
        chtv1.clearAnimation();
        chtv2.clearAnimation();
        chtv3.clearAnimation();
    }

    class ScannerTask extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btn.setText("...");
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String result = strings[0];
            try {
                JsonUtil util = new JsonUtil(getApplicationContext());
                JSONObject object = new JSONObject(result);
                Looper.prepare();
                if (object.has("setting")) {
                    current = 1;
                    publishProgress(getString(R.string.import_setting_));
                    if (!util.importSettingFromJson(strings[0]))
                        return false;//("Error when importing data");
                    else {
                        setting = true;
                        return true;//"Imported Successfully";
                    }
                } else if (object.has("course")) {
                    current = 2;
                    publishProgress(getString(R.string.importing_course));
                    if (!util.importCourseFromJson(strings[0]))
                        return false;
                    else {
                        course = true;
                        return true;
                    }
                } else if (object.has("lesson")) {
                    current = 3;
                    publishProgress(getString(R.string.importing_course));
                    if (!util.importLessonFromJson(strings[0]))
                        return false;
                    else {
                        lesson = true;
                        return true;
                    }
                } else
                    return false;

            } catch (JSONException e) {
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            btn.setText(values[0]);
            showPopUpAnimation(current);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);
            String status = success ? getString(R.string.imported_success) : getString(R.string.importing_error);
            if (setting && lesson && course) {
                btn.setChecked(true);
                btn.setEnabled(true);
                btn.setText(R.string.completed);
               //Analytics.logAppEvent("QrCodeScanner", "Complete all Scan");
                btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        finish();
                    }
                });
            } else
                btn.setText(status);
            clearAnimations();
            if (success)
                switch (current) {
                    case 1:
                        chtv1.setChecked(true);
                        chtv1.setText(R.string.timetable_setting_imported);
                        break;
                    case 2:
                        chtv2.setChecked(true);
                        chtv2.setText(R.string.course_data_imported);
                        break;
                    case 3:
                        chtv3.setChecked(true);
                        chtv3.setText(R.string.lesson_data_imported);
                        break;
                }else{
                btn.setEnabled(true);
                btn.setTextOff(getString(R.string.tryagain));
            }
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (setting && lesson && course) {
                        scannerView.stopCamera();
                    } else {
                        if (!success)btn.setEnabled(false);
                        scannerView.stopCamera();
                        scannerView.startCamera();
                        scannerView.setResultHandler(resultHandler());
                        btn.setText(R.string.scanning_);
                    }
                }
            };
            handler.postDelayed(runnable, 2000);
        }
    }
}
