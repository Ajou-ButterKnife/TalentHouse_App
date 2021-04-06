package kr.butterknife.talenthouse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void btn_click(View view) {
        EditText editTextEmail = findViewById(R.id.signup_et_email);
        EditText editTextPassword = findViewById(R.id.signup_et_password);
        EditText editTextPhonenumber = findViewById(R.id.signup_et_phonenumber);
        Toast.makeText(getApplicationContext(), editTextEmail.getText().toString(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), editTextPassword.getText().toString(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), editTextPhonenumber.getText().toString(), Toast.LENGTH_LONG).show();
    }
}