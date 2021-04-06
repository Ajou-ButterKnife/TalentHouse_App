package kr.butterknife.talenthouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private String TAG = "MAIN_TAG";
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    private BottomNavigationView bottomNavigationView;
    private MainFragment mainFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.main_bottomnavi);
        mainFrag = new MainFragment();

        transaction.add(R.id.main_ll, mainFrag).commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.btmnavi_home :
                transaction.replace(R.id.main_ll, mainFrag).commitAllowingStateLoss();
                return true;
            case R.id.btmnavi_like :
                return true;
            case R.id.btmnavi_menu :
                return true;
            case R.id.btmnavi_mypage :
                return true;
            case R.id.btmnavi_search :
                return true;
            default :
                Log.e(TAG, "bottom navigation view clicked");
        }
        return false;
    }
}