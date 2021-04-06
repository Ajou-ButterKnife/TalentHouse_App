package kr.butterknife.talenthouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginButtonClick(View view) {
        EditText login_et_id = findViewById(R.id.login_et_id);
        EditText login_et_password = findViewById(R.id.login_et_password);
        Toast.makeText(getApplicationContext(), "ID : " + login_et_id.getText().toString() + "\tPassword : " + login_et_password.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    public void onSignUpButtonClick(View view) {
        Intent i1 = new Intent (getApplicationContext(), SignUpActivity.class);
        startActivity(i1);
    }
}