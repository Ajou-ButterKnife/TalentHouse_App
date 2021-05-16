package kr.butterknife.talenthouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.FirebaseApp;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseApp.initializeApp(this);

        new Handler().postDelayed(() -> {
            String loginId = LoginInfo.INSTANCE.getLoginInfo(getApplicationContext())[0];
            if(loginId == "") {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
            else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            finish();
        }, 2000);
    }

    @Override
    public void onBackPressed() {

    }
}