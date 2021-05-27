package kr.butterknife.talenthouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "MAIN_TAG";
    private BottomNavigationView bottomNavigationView;
    private MainFragment mainFrag;
    private MyPageFragment myPageFrag;
    private SearchFragment searchFrag;
    private BoardFragment boardFrag;
    private HotBoardFragment hotboardFrag;
  
    private long BACK_PREESED_TIME = 2000L;
    private long cur = 0L;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FavoriteFragment favoriteFragment;
    private String myPageID = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFCM();
        getFirebaseToken();

        mainFrag = new MainFragment();

        bottomNavigationView = findViewById(R.id.main_bottomnavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.btmnavi_home);

        drawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer_root);
        navigationView = (NavigationView) findViewById(R.id.nv_main_navigation_root);

        View nav_header_view = navigationView.getHeaderView(0);
        ImageView nav_header_image = (ImageView) nav_header_view.findViewById(R.id.nav_header_profile_iv);
        TextView nav_header_text = (TextView) nav_header_view.findViewById(R.id.nav_header_nickname_tv);

        String nickname = LoginInfo.INSTANCE.getLoginInfo(getApplicationContext())[1];
        String profileImg = LoginInfo.INSTANCE.getLoginInfo(getApplicationContext())[2];
        nav_header_text.setText(nickname + "님 반갑습니다!");
        Glide.with(this).load(profileImg).circleCrop().into(nav_header_image);
        String content = nav_header_text.getText().toString();

        SpannableString spannableString = new SpannableString(content);
        int start = content.indexOf(nickname);
        int end = start + nickname.length();

        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.8f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        nav_header_text.setText(spannableString);

        navigationView.setNavigationItemSelectedListener(this);

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
                mainFrag.clearPlayer();
                favoriteFragment = new FavoriteFragment();
                replaceFragment(favoriteFragment, "favorite");
                return true;
            case R.id.btmnavi_menu :
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.btmnavi_mypage :
                mainFrag.clearPlayer();
                myPageFrag = new MyPageFragment(myPageID);
                replaceFragment(myPageFrag, "myPage");
                myPageID = "";
                return true;
            case R.id.btmnavi_search :
                mainFrag.clearPlayer();
                searchFrag = new SearchFragment();
                replaceFragment(searchFrag, "search");
                return true;
            case R.id.hot_content:
                mainFrag.clearPlayer();
                hotboardFrag = new HotBoardFragment();
                replaceFragment(hotboardFrag, "board");
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.all_content:
                drawerLayout.closeDrawer(GravityCompat.START);
                mainFrag.clearPlayer();
                boardFrag = new BoardFragment();
                replaceFragment(boardFrag, "board", "all");
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.dance_content:
                mainFrag.clearPlayer();
                boardFrag = new BoardFragment();
                replaceFragment(boardFrag, "board", "춤");
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.song_content:
                mainFrag.clearPlayer();
                boardFrag = new BoardFragment();
                replaceFragment(boardFrag, "board", "노래");
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.rap_content:
                mainFrag.clearPlayer();
                boardFrag = new BoardFragment();
                replaceFragment(boardFrag, "board", "랩");
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.draw_content:
                mainFrag.clearPlayer();
                boardFrag = new BoardFragment();
                replaceFragment(boardFrag, "board", "그림");
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.picture_content:
                mainFrag.clearPlayer();
                boardFrag = new BoardFragment();
                replaceFragment(boardFrag, "board", "사진");
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.etc_content:
                mainFrag.clearPlayer();
                boardFrag = new BoardFragment();
                replaceFragment(boardFrag, "board", "기타");
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
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


    public void replaceFragment(Fragment fragment, String backstackName, String category){
        Bundle b = new Bundle();
        b.putString("category", category);

        fragment.setArguments(b);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_ll, fragment);
        fragmentTransaction.addToBackStack(backstackName);
        fragmentTransaction.commit();
    }

    public void replaceFragment(Fragment fragment, String backstackName, PostItem item){
        Bundle b = new Bundle();
        b.putSerializable("update", item);

        fragment.setArguments(b);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_ll, fragment);
        fragmentTransaction.addToBackStack(backstackName);
        fragmentTransaction.commit();
    }

    public void setMyPageID(String id) {
        myPageID = id;
    }

    public void outsideMyPageClick() {
        bottomNavigationView.setSelectedItemId(R.id.btmnavi_mypage);

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
        if(!getVisibleFragment(mainFrag)) {
            if(System.currentTimeMillis() - cur < BACK_PREESED_TIME) {
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                cur = System.currentTimeMillis();
            }
        }
        else {
            super.onBackPressed();
            FragmentManager fm = getSupportFragmentManager();
            int count = fm.getBackStackEntryCount();

            for(int i = 0; i < 5; i++) {
                bottomNavigationView.getMenu().getItem(i).setChecked(false);
            }
            switch(fm.getBackStackEntryAt(count - 1).getName()) {
                case "Main" :
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                    break;
                case "favorite" :
                    bottomNavigationView.getMenu().getItem(3).setChecked(true);
                    break;
                case "myPage" :
                    bottomNavigationView.getMenu().getItem(4).setChecked(true);
                    break;
                case "search" :
                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
                    break;
            }
        }
    }

    private void initFCM() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(
                    new NotificationChannel(channelId,
                            channelName, NotificationManager.IMPORTANCE_HIGH)
            );
        }
    }

    private void getFirebaseToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                String token = task.getResult();

                if(token != null) {
                    Util.INSTANCE.registerFCMToken(getApplicationContext(), token);
                    Log.d("FCM", token);
                }
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}