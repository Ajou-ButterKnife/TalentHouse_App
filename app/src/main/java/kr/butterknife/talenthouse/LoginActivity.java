package kr.butterknife.talenthouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.request.LoginReq;
import kr.butterknife.talenthouse.network.response.LoginRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private String TAG = "LOGIN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginButtonClick(View view) {
        String id = ((EditText) findViewById(R.id.login_et_id)).getText().toString();
        String pw = ((EditText) findViewById(R.id.login_et_password)).getText().toString();

        new Runnable() {
            @Override
            public void run() {
                try {
                    ButterKnifeApi.INSTANCE.getRetrofitService().login(new LoginReq(id, pw)).enqueue(new Callback<LoginRes>() {
                        @Override
                        public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                            // 정상 출력이 되면 아래 로그가 출력됨
                            if(response.body() != null)
                                Log.d(TAG, response.body().getEmail());
                            // 정상 출력이 되지 않을 때 서버에서의 response
                            else {
                                Log.d(TAG, response.errorBody().toString());
                                Log.d(TAG, response.message());
                                Log.d(TAG, String.valueOf(response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginRes> call, Throwable t) {
                            // 서버쪽으로 아예 메시지를 보내지 못한 경우
                            Log.d(TAG, "SERVER CONNECTION ERROR");
                        }
                    });
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.run();
    }

    public void onSignUpButtonClick(View view) {
        Intent i1 = new Intent (getApplicationContext(), SignUpActivity.class);
        startActivity(i1);
    }
}