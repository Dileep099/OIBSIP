package com.example.stopwatchapp; // Aapke project ka package name yahan hoga

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvTimer;
    private Button btnStart, btnPause, btnReset;

    private Handler handler = new Handler();
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;

    private boolean isRunning = false;

    // Runnable timer ko update karne ke liye
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000) / 10;

            tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", mins, secs, milliseconds));
            handler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views initialize karein
        tvTimer = findViewById(R.id.tvTimer);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnReset = findViewById(R.id.btnReset);

        // Start Button
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimerThread, 0);
                    isRunning = true;

                    // Buttons UI state change
                    btnStart.setEnabled(false);
                    btnPause.setEnabled(true);
                }
            }
        });

        // Pause Button
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    timeSwapBuff += timeInMilliseconds;
                    handler.removeCallbacks(updateTimerThread);
                    isRunning = false;

                    // Buttons UI state change
                    btnStart.setEnabled(true);
                    btnPause.setEnabled(false);
                }
            }
        });

        // Reset Button
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0L;
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updatedTime = 0L;

                handler.removeCallbacks(updateTimerThread);
                isRunning = false;

                tvTimer.setText("00:00:00");

                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
            }
        });
    }
}