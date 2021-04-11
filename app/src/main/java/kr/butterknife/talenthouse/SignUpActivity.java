package kr.butterknife.talenthouse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.request.SignUpReq;
import kr.butterknife.talenthouse.network.response.SignUpRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private String TAG = "SIGN_UP_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void btn_click(View view) {
        String email = ((EditText) findViewById(R.id.signup_et_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.signup_et_password)).getText().toString();
        String phone = ((EditText) findViewById(R.id.signup_et_phonenumber)).getText().toString();

        try {
            ButterKnifeApi.INSTANCE.getRetrofitService().addUser(new SignUpReq(email, password, phone)).enqueue(new Callback<SignUpRes>() {
                @Override
                public void onResponse(Call<SignUpRes> call, Response<SignUpRes> response) {
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
                public void onFailure(Call<SignUpRes> call, Throwable t) {
                    // 서버쪽으로 아예 메시지를 보내지 못한 경우
                    Log.d(TAG, "SERVER CONNECTION ERROR");
                }
            });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}