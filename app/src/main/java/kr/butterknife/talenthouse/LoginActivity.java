package kr.butterknife.talenthouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginButtonClick(View view) {
        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
    }

    public void onSignUpButtonClick(View view) {
        Intent i1 = new Intent (getApplicationContext(), SignUpActivity.class);
        startActivity(i1);
    }
}