package kr.butterknife.talenthouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.request.NormalLoginReq;
import kr.butterknife.talenthouse.network.request.OverlapNickname;
import kr.butterknife.talenthouse.network.request.SocialLoginReq;
import kr.butterknife.talenthouse.network.response.CommonResponse;
import kr.butterknife.talenthouse.network.response.NormalLoginRes;
import kr.butterknife.talenthouse.network.response.SocialLoginRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;

    // 구글로그인 result 상수
    private static final int RC_SIGN_IN = 900;

    // 구글api클라이
    private GoogleSignInClient googleSignInClient;

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 구글  로그인 버튼
    private SignInButton buttonGoogle;

    private String TAG = "LOGIN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.login_btn_login);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        //      한번 인증 되면 바로 main화면으로 이동. 로그아웃 버튼 구현 전 까지는 주석처리 예정
        /*
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            finish();
        }*/
        

        buttonGoogle = findViewById(R.id.signInButton);

        // Google 로그인을 앱에 통합
        // GoogleSignInOptions 개체를 구성할 때 requestIdToken을 호출
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onLoginButtonClick();
                onSocialSignUp("asdf");
            }
        });
    }

    // 사용자가 정상적으로 로그인한 후에 GoogleSignInAccount 개체에서 ID 토큰을 가져와서
    // Firebase 사용자 인증 정보로 교환하고 Firebase 사용자 인증 정보를 사용해 Firebase에 인증합니다.
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            onGoogleLoginButtonClick();
                        } else {
                            // 로그인 실패
                            Toast.makeText(getApplicationContext(), "fail" , Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 구글로그인 버튼 응답
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // 구글 로그인 성공
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
        }
    }


    public void onGoogleLoginButtonClick() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        try {
            ButterKnifeApi.INSTANCE.getRetrofitService().socialLogin(new SocialLoginReq(uid)).enqueue(new Callback<SocialLoginRes>() {
                @Override
                public void onResponse(Call<SocialLoginRes> call, Response<SocialLoginRes> response) {
                    String loginFlag = response.body().getSocialFlag();
                    if(loginFlag.equals("login")){
                        Intent i2 = new Intent (getApplicationContext(), MainActivity.class);
                        startActivity(i2);
                    }
                    else if(loginFlag.equals("signup")){
                        Toast.makeText(getApplicationContext(), "go sign up" , Toast.LENGTH_SHORT).show();
                    }
                        // 정상 출력이 되지 않을 때 서버에서의 response
                    else {
                        Log.d(TAG, response.errorBody().toString());
                        Log.d(TAG, response.message());
                        Log.d(TAG, String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<SocialLoginRes> call, Throwable t) {
                    // 서버쪽으로 아예 메시지를 보내지 못한 경우
                    Log.d(TAG, "SERVER CONNECTION ERROR");
                }
            });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onLoginButtonClick() {
        String id = ((EditText) findViewById(R.id.login_et_id)).getText().toString();
        String pw = ((EditText) findViewById(R.id.login_et_password)).getText().toString();

        try {
            ButterKnifeApi.INSTANCE.getRetrofitService().login(new NormalLoginReq(id, pw)).enqueue(new Callback<NormalLoginRes>() {
                @Override
                public void onResponse(Call<NormalLoginRes> call, Response<NormalLoginRes> response) {
                    // 정상 출력이 되면 아래 로그가 출력됨
                    if(response.body() != null) {
                        NormalLoginRes result = response.body();
                        if(result.getResult().equals("Success")) {
                            LoginInfo.INSTANCE.setLoginInfo(result.getData().get_id(), getApplicationContext());
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), result.getDetail(), Toast.LENGTH_SHORT).show();
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
                public void onFailure(Call<NormalLoginRes> call, Throwable t) {
                    // 서버쪽으로 아예 메시지를 보내지 못한 경우
                    Toast.makeText(getApplicationContext(), "서버와 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "SERVER CONNECTION ERROR");
                }
            });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onSignUpButtonClick(View view) {
        Intent i1 = new Intent (getApplicationContext(), SignUpActivity.class);
        startActivity(i1);
    }

    public void onSocialSignUp(String uid) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_social_signup, null, false);

        Spinner spinner = view.findViewById(R.id.social_spinner);
        ChipGroup chipGroup = view.findViewById(R.id.social_chipgroup);
        Button overlap = view.findViewById(R.id.social_btn_overlap);
        TextInputLayout nickname = view.findViewById(R.id.social_et_nickname);
        TextInputLayout phone = view.findViewById(R.id.social_et_phone);
        Button signup = view.findViewById(R.id.social_btn_signup);
        Button cancel = view.findViewById(R.id.social_btn_cancel);
        final Boolean[] isOverlapNickname = {false};

        SpinnerUtil.INSTANCE.setCategorySpinner(spinner, chipGroup, getApplicationContext());
        // 데이터를 저장하게 되는 리스트
        List<String> spinner_items = Arrays.asList(getResources().getStringArray(R.array.category_spinner));
        // 스피너와 리스트를 연결하기 위해 사용되는 어댑터
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinner_items);

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 스피너의 어댑터 지정
        spinner.setAdapter(spinner_adapter);

        overlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempNickname = nickname.getEditText().getText().toString();
                if(tempNickname.equals("")) {
                    nickname.setError("닉네임을 입력해주세요");
                    return;
                }

                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ButterKnifeApi.INSTANCE.getRetrofitService().overlapCheck(new OverlapNickname(tempNickname)).enqueue(new Callback<CommonResponse>() {
                                @Override
                                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                    // 정상 출력이 되면 아래 로그가 출력됨
                                    if(response.body() != null) {
                                        CommonResponse result = response.body();
                                        if(result.getResult().equals("Success")) {
                                            nickname.setError(null);
                                            isOverlapNickname[0] = true;
                                        }
                                        else {
                                            nickname.setError("중복된 닉네임입니다.");
                                            isOverlapNickname[0] = false;
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
                                public void onFailure(Call<CommonResponse> call, Throwable t) {
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
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        signup.setOnClickListener(v -> {
            if(nickname.getEditText().getText().toString().length() == 0) {
                nickname.setError("닉네임을 입력해주세요");
                return;
            }
            if(nickname.getError() != null ) {
                nickname.setError("닉네임 중복확인을 해주세요.");
                return;
            }
            String tempNickname = nickname.getEditText().getText().toString();
            String tempPhone = phone.getEditText().getText().toString();
            // 회원가입 프로세스 처리
            Toast.makeText(getApplicationContext(), tempNickname + " " + tempPhone, Toast.LENGTH_SHORT).show();
        });

        cancel.setOnClickListener(v -> {
            dialog.cancel();
        });

        dialog.show();
    }
}