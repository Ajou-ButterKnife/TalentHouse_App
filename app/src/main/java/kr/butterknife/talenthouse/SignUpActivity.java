package kr.butterknife.talenthouse;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.request.NormalSignUpReq;
import kr.butterknife.talenthouse.network.request.OverlapEmail;
import kr.butterknife.talenthouse.network.request.OverlapNickname;
import kr.butterknife.talenthouse.network.response.CommonResponse;
import kr.butterknife.talenthouse.network.response.NormalSignUpRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private String TAG = "SIGN_UP_TAG";
    ArrayList<String> SelectedCategory;
    private Button btnSignup, btnEmailCheck, btnNicknameCheck;
    TextInputLayout textInputLayoutEmail, textInputLayoutNickname;
    boolean isOverlapEmail = true;
    boolean isOverlapNickname = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Spinner spinner = findViewById(R.id.signup_spinner);
        ChipGroup chipGroup = findViewById(R.id.signup_chipgroup);

        SpinnerUtil.INSTANCE.setCategorySpinner(spinner, chipGroup, getApplicationContext());


        // 이메일 중복체크 버튼
        btnEmailCheck = (Button)findViewById(R.id.signup_btn_emailCheck);
        btnEmailCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInputLayoutEmail = (TextInputLayout)findViewById(R.id.signup_til_email);
                String textEmail = textInputLayoutEmail.getEditText().getText().toString();
                if(textEmail.equals("")){
                    textInputLayoutEmail.setError("이메일을 입력해주세요");
                    return;
                }
                    new Runnable(){
                        @Override
                        public void run(){
                            try{
                                ButterKnifeApi.INSTANCE.getRetrofitService().overlapCheck(new OverlapEmail(textEmail)).enqueue(new Callback<CommonResponse>() {
                                    @Override
                                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                        // 정상 출력이 되면 아래 로그가 출력됨
                                        if(response.body() != null){
                                            CommonResponse result = response.body();
                                            if(result.getResult().equals("Success")){
                                                textInputLayoutEmail.setError(null);
                                                isOverlapEmail = false;
                                            }
                                            else{
                                                textInputLayoutEmail.setError("중복된 이메일입니다");
                                                isOverlapEmail = true;
                                            }
                                        }
                                        else{
                                            Log.d(TAG, response.errorBody().toString());
                                            Log.d(TAG, response.message());
                                            Log.d(TAG, String.valueOf(response.code()));
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                                        // 서버 쪽으로 메시지를 보내지 못한 경우
                                        Log.d(TAG, "SERVER CONNECTION ERROR");
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }.run();
                }

        });
        // 닉네임 중복체크 버튼
        btnNicknameCheck = (Button)findViewById(R.id.signup_btn_nicknameCheck);
        btnNicknameCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInputLayoutNickname = (TextInputLayout)findViewById(R.id.signup_til_nickname);
                String textNickname = textInputLayoutNickname.getEditText().getText().toString();
                if(textNickname.equals("")){
                    textInputLayoutNickname.setError("닉네임을 입력해주세요");
                    return;
                }
                new Runnable(){
                    @Override
                    public void run(){
                        try{
                            ButterKnifeApi.INSTANCE.getRetrofitService().overlapCheck(new OverlapNickname(textNickname)).enqueue(new Callback<CommonResponse>() {
                                @Override
                                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                    // 정상 출력이 되면 아래 로그가 출력됨
                                    if(response.body() != null){
                                        CommonResponse result = response.body();
                                        if(result.getResult().equals("Success")){
                                            textInputLayoutNickname.setError(null);
                                            isOverlapNickname = false;
                                        }
                                        else{
                                            textInputLayoutEmail.setError("중복된 닉네임입니다");
                                            isOverlapNickname = true;
                                        }
                                    }
                                    else{
                                        Log.d(TAG, response.errorBody().toString());
                                        Log.d(TAG, response.message());
                                        Log.d(TAG, String.valueOf(response.code()));
                                    }
                                }

                                @Override
                                public void onFailure(Call<CommonResponse> call, Throwable t) {
                                    // 서버 쪽으로 메시지를 보내지 못한 경우
                                    Log.d(TAG, "SERVER CONNECTION ERROR");
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.run();
            }
        });
        // 회원가입 버튼
        btnSignup = (Button)findViewById(R.id.signup_btn_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInputLayoutEmail = (TextInputLayout)findViewById(R.id.signup_til_email);
                textInputLayoutNickname = (TextInputLayout)findViewById(R.id.signup_til_nickname);

                String email = textInputLayoutEmail.getEditText().getText().toString();
                String password = ((TextInputLayout)findViewById(R.id.signup_til_password)).getEditText().getText().toString();
                String phone = ((TextInputLayout)findViewById(R.id.signup_til_phone)).getEditText().getText().toString();
                String nickname = textInputLayoutNickname.getEditText().getText().toString();

                ChipGroup chipGroup = findViewById(R.id.signup_chipgroup);
                String[] category = new String[chipGroup.getChildCount()];

                for(int i = 0; i< chipGroup.getChildCount(); i++){
                    category[i] = ((Chip)chipGroup.getChildAt(i)).getText().toString();
                }
                if(isOverlapEmail == true){
                    Toast.makeText(getApplicationContext(), "Email 중복확인을 해주세요", Toast.LENGTH_SHORT).show();
                }else if(password.equals("")){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(isOverlapNickname == true){
                    Toast.makeText(getApplicationContext(), "Nickname 중복확인을 해주세요", Toast.LENGTH_SHORT).show();
                }else if(phone.equals("")){
                    Toast.makeText(getApplicationContext(), "핸드폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ButterKnifeApi.INSTANCE.getRetrofitService().normalAddUser(new NormalSignUpReq(email, password, phone, nickname, Arrays.asList(category))).enqueue(new Callback<NormalSignUpRes>() {
                                    @Override
                                    public void onResponse(Call<NormalSignUpRes> call, Response<NormalSignUpRes> response) {
                                        // 정상 출력이 되면 아래 로그가 출력됨
                                        if (response.body() != null){
                                            Log.d(TAG, response.body().getResult());
                                            if(response.body().getResult().equals("Success")){
                                                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                        // 정상 출력이 되지 않을 때 서버에서의 response
                                        else {
                                            Log.d(TAG, response.errorBody().toString());
                                            Log.d(TAG, response.message());
                                            Log.d(TAG, String.valueOf(response.code()));
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<NormalSignUpRes> call, Throwable t) {
                                        // 서버쪽으로 아예 메시지를 보내지 못한 경우
                                        Log.d(TAG, "SERVER CONNECTION ERROR");
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.run();
                }
            }
        });
        List<String> spinner_items = Arrays.asList(getResources().getStringArray(R.array.category_spinner));
        // 스피너와 리스트를 연결하기 위해 사용되는 어댑터
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinner_items);

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 스피너의 어댑터 지정
        spinner.setAdapter(spinner_adapter);
    }
}
