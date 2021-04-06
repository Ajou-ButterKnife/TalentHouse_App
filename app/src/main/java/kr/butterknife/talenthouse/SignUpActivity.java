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
        EditText editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        Toast.makeText(getApplicationContext(), editTextEmail.getText().toString(), Toast.LENGTH_LONG).show();
    }
}