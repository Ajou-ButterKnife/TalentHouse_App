package kr.butterknife.talenthouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private String TAG = "MAIN_TAG";
    private BottomNavigationView bottomNavigationView;
    private MainFragment mainFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFrag = new MainFragment();

        bottomNavigationView = findViewById(R.id.main_bottomnavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.btmnavi_home);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.btmnavi_home :
                if(getVisibleFragment(mainFrag))
                    replaceFragment(mainFrag, "Main");
                return true;
            case R.id.btmnavi_favorite:
                return true;
            case R.id.btmnavi_menu :
                return true;
            case R.id.btmnavi_mypage :
                //임시로 로그아웃
                LoginInfo.INSTANCE.logout(getApplicationContext());
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                finish();
                return true;
            case R.id.btmnavi_search :
                return true;
            default :
                Log.e(TAG, "bottom navigation view clicked");
        }
        return false;
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
}