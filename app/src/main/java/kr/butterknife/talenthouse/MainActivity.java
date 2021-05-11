package kr.butterknife.talenthouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private String TAG = "MAIN_TAG";
    private BottomNavigationView bottomNavigationView;
    private MainFragment mainFrag;
    private MyPageFragment myPageFrag;
    private long BACK_PREESED_TIME = 2000L;
    private long cur = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFrag = new MainFragment();

        bottomNavigationView = findViewById(R.id.main_bottomnavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.btmnavi_home);

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }
        };

        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("사진 및 파일을 저장하기 위하여 접근 권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.btmnavi_home :
                if(getVisibleFragment(mainFrag)) {
                    clearBackStack();
                    mainFrag = new MainFragment();
                    replaceFragment(mainFrag, "Main");
                }
                return true;
            case R.id.btmnavi_favorite:
                return true;
            case R.id.btmnavi_menu :
                //임시로 로그아웃
                LoginInfo.INSTANCE.logout(getApplicationContext());
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                finish();
                return true;
            case R.id.btmnavi_mypage :
                mainFrag.clearPlayer();
                myPageFrag = new MyPageFragment();
                replaceFragment(myPageFrag, "myPage");
                return true;
            case R.id.btmnavi_search :
                return true;
            default :
                Log.e(TAG, "bottom navigation view clicked");
        }
        return false;
    }

    public void clearBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); i++)
            fragmentManager.popBackStack();
    }

    public void replaceFragment(Fragment fragment, String backstackName){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_ll, fragment);
        fragmentTransaction.addToBackStack(backstackName);
        fragmentTransaction.commit();
    }


    public void finishFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment).commit();
        fragmentManager.popBackStack();
    }

    public boolean getVisibleFragment(Fragment fragment) {
        for(Fragment f : getSupportFragmentManager().getFragments()) {
            if(f.isVisible()) {
                if(f.equals(fragment))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - cur < BACK_PREESED_TIME) {
            if(!getVisibleFragment(mainFrag)) {
                finish();
            }
            else
                super.onBackPressed();
        }
        else {
            Toast.makeText(getApplicationContext(), "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            cur = System.currentTimeMillis();
        }
    }
}