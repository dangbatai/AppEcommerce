package com.example.app_ecommerce.activity;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class Utils {
    public static void vibrateDevice(Context context) {
        // Lấy dịch vụ rung từ hệ thống
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Kiểm tra xem thiết bị có hỗ trợ rung hay không
        if (vibrator != null && vibrator.hasVibrator()) {
            // Tạo hiệu ứng rung. Đối với Android SDK 26 trở lên, sử dụng VibrationEffect.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.vibrate(vibrationEffect);
            } else {
                // Đối với các phiên bản Android cũ hơn, sử dụng phương thức rung cũ.
                vibrator.vibrate(200);
            }
        }
    }
}

