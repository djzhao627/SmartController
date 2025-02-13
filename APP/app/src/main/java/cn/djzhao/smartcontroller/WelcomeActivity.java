package cn.djzhao.smartcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import com.blankj.utilcode.util.ActivityUtils;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtils.startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }, 1500);
    }
}
