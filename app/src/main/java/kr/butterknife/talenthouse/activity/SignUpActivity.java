package kr.butterknife.talenthouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.butterknife.talenthouse.LoadingDialog;
import kr.butterknife.talenthouse.R;
import kr.butterknife.talenthouse.SpinnerUtil;
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
        textInputLayoutEmail = (TextInputLayout)findViewById(R.id.signup_til_email);
        textInputLayoutEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputLayoutEmail.setError("??????????????? ????????????");
                isOverlapEmail = true;
            }
        });
        textInputLayoutNickname = (TextInputLayout)findViewById(R.id.signup_til_nickname);
        textInputLayoutNickname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                textInputLayoutNickname.setError("??????????????? ????????????");
                isOverlapNickname = true;
            }
        });

        SpinnerUtil.INSTANCE.setCategorySpinner(spinner, chipGroup, getApplicationContext());


        // ????????? ???????????? ??????
        btnEmailCheck = (Button)findViewById(R.id.signup_btn_emailCheck);
        btnEmailCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = textInputLayoutEmail.getEditText().getText().toString();
                if(textEmail.equals("")){
                    textInputLayoutEmail.setError("???????????? ??????????????????");
                    return;
                }
                    new Runnable(){
                        @Override
                        public void run(){
                            try{
                                LoadingDialog.INSTANCE.onLoadingDialog(SignUpActivity.this);
                                ButterKnifeApi.INSTANCE.getRetrofitService().overlapCheck(new OverlapEmail(textEmail)).enqueue(new Callback<CommonResponse>() {
                                    @Override
                                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                        // ?????? ????????? ?????? ?????? ????????? ?????????
                                        if(response.body() != null){
                                            CommonResponse result = response.body();
                                            if(result.getResult().equals("Success")){
                                                textInputLayoutEmail.setError(null);
                                                isOverlapEmail = false;
                                                Toast.makeText(getApplicationContext(), "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                textInputLayoutEmail.setError("????????? ??????????????????");
                                                isOverlapEmail = true;
                                            }
                                        }
                                        else{
                                            Log.d(TAG, response.errorBody().toString());
                                            Log.d(TAG, response.message());
                                            Log.d(TAG, String.valueOf(response.code()));
                                        }
                                        LoadingDialog.INSTANCE.offLoadingDialog();
                                    }

                                    @Override
                                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                                        // ?????? ????????? ???????????? ????????? ?????? ??????
                                        Log.d(TAG, "SERVER CONNECTION ERROR");
                                        LoadingDialog.INSTANCE.offLoadingDialog();
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                                LoadingDialog.INSTANCE.offLoadingDialog();
                            }
                        }
                    }.run();
                }

        });
        // ????????? ???????????? ??????
        btnNicknameCheck = (Button)findViewById(R.id.signup_btn_nicknameCheck);
        btnNicknameCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textNickname = textInputLayoutNickname.getEditText().getText().toString();
                if(textNickname.equals("")){
                    textInputLayoutNickname.setError("???????????? ??????????????????");
                    return;
                }
                new Runnable(){
                    @Override
                    public void run(){
                        try{
                            LoadingDialog.INSTANCE.onLoadingDialog(SignUpActivity.this);
                            ButterKnifeApi.INSTANCE.getRetrofitService().overlapCheck(new OverlapNickname(textNickname)).enqueue(new Callback<CommonResponse>() {
                                @Override
                                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                    // ?????? ????????? ?????? ?????? ????????? ?????????
                                    if(response.body() != null){
                                        CommonResponse result = response.body();
                                        if(result.getResult().equals("Success")){
                                            textInputLayoutNickname.setError(null);
                                            isOverlapNickname = false;
                                            Toast.makeText(getApplicationContext(), "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            textInputLayoutNickname.setError("????????? ??????????????????");
                                            isOverlapNickname = true;
                                        }
                                    }
                                    else{
                                        Log.d(TAG, response.errorBody().toString());
                                        Log.d(TAG, response.message());
                                        Log.d(TAG, String.valueOf(response.code()));
                                    }
                                    LoadingDialog.INSTANCE.offLoadingDialog();
                                }

                                @Override
                                public void onFailure(Call<CommonResponse> call, Throwable t) {
                                    // ?????? ????????? ???????????? ????????? ?????? ??????
                                    Log.d(TAG, "SERVER CONNECTION ERROR");
                                    LoadingDialog.INSTANCE.offLoadingDialog();
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                            LoadingDialog.INSTANCE.offLoadingDialog();
                        }
                    }
                }.run();
            }
        });
        // ???????????? ??????
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
                    Toast.makeText(getApplicationContext(), "Email ??????????????? ????????????", Toast.LENGTH_SHORT).show();
                }else if(password.equals("")){
                    Toast.makeText(getApplicationContext(), "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }else if(isOverlapNickname == true){
                    Toast.makeText(getApplicationContext(), "Nickname ??????????????? ????????????", Toast.LENGTH_SHORT).show();
                }else if(phone.equals("")){
                    Toast.makeText(getApplicationContext(), "????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }else{
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                LoadingDialog.INSTANCE.onLoadingDialog(SignUpActivity.this);
                                ButterKnifeApi.INSTANCE.getRetrofitService().normalAddUser(new NormalSignUpReq(email, password, phone, nickname, Arrays.asList(category))).enqueue(new Callback<NormalSignUpRes>() {
                                    @Override
                                    public void onResponse(Call<NormalSignUpRes> call, Response<NormalSignUpRes> response) {
                                        // ?????? ????????? ?????? ?????? ????????? ?????????
                                        if (response.body() != null){
                                            Log.d(TAG, response.body().getResult());
                                            if(response.body().getResult().equals("Success")){
                                                Toast.makeText(getApplicationContext(), "???????????? ??????", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                        // ?????? ????????? ?????? ?????? ??? ??????????????? response
                                        else {
                                            Log.d(TAG, response.errorBody().toString());
                                            Log.d(TAG, response.message());
                                            Log.d(TAG, String.valueOf(response.code()));
                                        }
                                        LoadingDialog.INSTANCE.offLoadingDialog();
                                    }

                                    @Override
                                    public void onFailure(Call<NormalSignUpRes> call, Throwable t) {
                                        // ??????????????? ?????? ???????????? ????????? ?????? ??????
                                        Log.d(TAG, "SERVER CONNECTION ERROR");
                                        LoadingDialog.INSTANCE.offLoadingDialog();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                LoadingDialog.INSTANCE.offLoadingDialog();
                            }
                        }
                    }.run();
                }
            }
        });
        List<String> spinner_items = Arrays.asList(getResources().getStringArray(R.array.category_spinner));
        // ???????????? ???????????? ???????????? ?????? ???????????? ?????????
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinner_items);

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // ???????????? ????????? ??????
        spinner.setAdapter(spinner_adapter);
    }
}
