package com.starcontrol.legacy;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;

public class MainActivity extends Activity {
    private LinearLayout content;
    private TextView batteryValue;
    private TextView memoryValue;
    private TextView storageValue;
    private final DecimalFormat number = new DecimalFormat("#,##0.0");

    private final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
            if (level >= 0 && scale > 0) {
                batteryValue.setText(level * 100 / scale + "%");
            } else {
                batteryValue.setText("Not available");
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(0xFF081522);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(22), dp(18), dp(22));
        scroll.addView(content);

        TextView title = new TextView(this);
        title.setText("STAR CONTROL");
        title.setTextColor(0xFF35D9F2);
        title.setTextSize(27);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, 1);
        content.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("LEGACY DEVICE DASHBOARD");
        subtitle.setTextColor(0xFF9BB7C8);
        subtitle.setTextSize(11);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, dp(5), 0, dp(20));
        content.addView(subtitle);

        addSection("DEVICE INFORMATION");
        addRow("Model", Build.MODEL);
        addRow("Manufacturer", Build.MANUFACTURER);
        addRow("Android version", Build.VERSION.RELEASE);
        addRow("API level", String.valueOf(Build.VERSION.SDK_INT));
        addRow("Device", Build.DEVICE);

        addSection("LIVE STATUS");
        batteryValue = addRow("Battery", "Reading...");
        memoryValue = addRow("Available RAM", "Reading...");
        storageValue = addRow("Available storage", "Reading...");

        Button refresh = new Button(this);
        refresh.setText("REFRESH STATUS");
        refresh.setTextColor(0xFF081522);
        refresh.setBackgroundColor(0xFF35D9F2);
        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(-1, dp(48));
        buttonParams.topMargin = dp(20);
        content.addView(refresh, buttonParams);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMemory();
                updateStorage();
            }
        });

        TextView footer = new TextView(this);
        footer.setText("STAR CONTROL Legacy 1.0  |  Offline");
        footer.setTextColor(0xFF7893A4);
        footer.setTextSize(11);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(24), 0, dp(4));
        content.addView(footer);

        setContentView(scroll);
        updateMemory();
        updateStorage();
    }

    private void addSection(String text) {
        TextView section = new TextView(this);
        section.setText(text);
        section.setTextColor(0xFF35D9F2);
        section.setTextSize(12);
        section.setTypeface(null, 1);
        section.setPadding(0, dp(18), 0, dp(8));
        content.addView(section);
    }

    private TextView addRow(String label, String value) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(dp(13), dp(10), dp(13), dp(10));
        row.setBackgroundColor(0xFF122536);

        TextView name = new TextView(this);
        name.setText(label);
        name.setTextColor(0xFF9BB7C8);
        name.setTextSize(12);
        row.addView(name);

        TextView result = new TextView(this);
        result.setText(value);
        result.setTextColor(0xFFFFFFFF);
        result.setTextSize(17);
        result.setPadding(0, dp(3), 0, 0);
        row.addView(result);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
        params.bottomMargin = dp(7);
        content.addView(row, params);
        return result;
    }

    private void updateMemory() {
        ActivityManager manager =
                (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        memoryValue.setText(number.format(info.availMem / (1024.0 * 1024.0))
                + " MB available");
    }

    @SuppressWarnings("deprecation")
    private void updateStorage() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long available;
        if (Build.VERSION.SDK_INT >= 18) {
            available = stat.getAvailableBytes();
        } else {
            available = (long) stat.getAvailableBlocks()
                    * (long) stat.getBlockSize();
        }
        storageValue.setText(number.format(available / (1024.0 * 1024.0))
                + " MB available");
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(batteryReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(batteryReceiver);
        } catch (IllegalArgumentException ignored) {
        }
    }
}
