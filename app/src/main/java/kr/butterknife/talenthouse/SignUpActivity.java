package kr.butterknife.talenthouse;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.request.NormalSignUpReq;
import kr.butterknife.talenthouse.network.response.NormalSignUpRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private String TAG = "SIGN_UP_TAG";
    ArrayList<String> SelectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Spinner spinner = findViewById(R.id.signup_spinner);
        ChipGroup chipGroup = findViewById(R.id.signup_chipgroup);
        SelectedCategory = new ArrayList<>();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();
                if(str.equals("카테고리") == false){
                    boolean alreadySelected = false;
                    // 이미 선택되었는지 확인
                    for(int i = 0; i< chipGroup.getChildCount(); i++){
                        String category = ((Chip)chipGroup.getChildAt(i)).getText().toString();
                        if(category.equals(str)){
                            alreadySelected = true;
                            Toast.makeText(getApplicationContext(), "이미 선택된 카테고리입니다.",Toast.LENGTH_SHORT).show();
                            parent.setSelection(0);
                            break;
                        }
                    }
                    if(alreadySelected == false){
                        // Chip 인스턴스 생성
                        Chip chip = new Chip(SignUpActivity.this);
                        chip.setText(str);
                        // chip icon 표시 여부
                        chip.setCloseIconVisible(true);
                        chip.setBackgroundColor(Color.BLUE);
                        // chip group 에 해당 chip 추가
                        chipGroup.addView(chip);
                        parent.setSelection(0);
                        // chip 인스턴스 클릭 리스너
                        chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chipGroup.removeView(v);
                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 데이터를 저장하게 되는 리스트
        List<String> spinner_items = new ArrayList<>();
        // 스피너와 리스트를 연결하기 위해 사용되는 어댑터
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinner_items);
        spinner_items.add("카테고리");
        spinner_items.add("춤");
        spinner_items.add("노래");
        spinner_items.add("랩");
        spinner_items.add("그림");
        spinner_items.add("사진");
        spinner_items.add("기타");

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 스피너의 어댑터 지정
        spinner.setAdapter(spinner_adapter);

    }

    public void btn_click(View view) {
        String email = ((EditText) findViewById(R.id.signup_et_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.signup_et_password)).getText().toString();
        String phone = ((EditText) findViewById(R.id.signup_et_phonenumber)).getText().toString();
        String nickname = ((EditText) findViewById(R.id.signup_et_nickname)).getText().toString();

//        String[] category = new String[SelectedCategory.size()];
//        int size = 0;
//        for(String temp : SelectedCategory){
//            category[size++] = temp;
//        }

        ChipGroup chipGroup = findViewById(R.id.signup_chipgroup);
        String[] category = new String[chipGroup.getChildCount()];

        for(int i = 0; i< chipGroup.getChildCount(); i++){
            category[i] = ((Chip)chipGroup.getChildAt(i)).getText().toString();
        }
      
        new Runnable() {
            @Override
            public void run() {
                try {
                    ButterKnifeApi.INSTANCE.getRetrofitService().normalAddUser(new NormalSignUpReq(email, password, phone, nickname, Arrays.asList(category))).enqueue(new Callback<NormalSignUpRes>() {
                        @Override
                        public void onResponse(Call<NormalSignUpRes> call, Response<NormalSignUpRes> response) {
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
                        public void onFailure(Call<NormalSignUpRes> call, Throwable t) {
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
}
