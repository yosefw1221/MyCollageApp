package com.yotor.solution.mytimetable.qrcode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrFragment extends Fragment{
        private int i;
        private JsonUtil jsonUtil;
        QrFragment(int which) {
            i = which;
        }
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            AppCompatImageView qrview = new AppCompatImageView(getContext());
            qrview.setBackgroundColor(Color.WHITE);
            jsonUtil = new JsonUtil(getContext());
            qrview.setLayoutParams(new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Bitmap qr = getQRCode();
            qrview.setImageBitmap(qr);
            return qrview;
        }

        private Bitmap getQRCode() {
            String data = "error";
            switch (i) {
                case 0:
                    data = jsonUtil.exportSettingToJson().toString();
                    break;
                case 1:
                    data = jsonUtil.exportCourseToJson().toString();
                    break;
                case 2:
                    data = jsonUtil.exportLessonToJson().toString();
                    break;
            }
            return QrCodeHelper.getInstance(getContext())
                    .setMargin(2)
                    .setErrorCorrectionLevel(ErrorCorrectionLevel.L)
                    .setContent(data).getQRCode();
        }
    }
