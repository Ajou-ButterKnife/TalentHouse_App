package kr.butterknife.talenthouse;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.response.CategoryRes;
import kr.butterknife.talenthouse.network.response.PostRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotBoardFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    public static int year, month, day;


    private RecyclerView rv;
    private MainRVAdapter rvAdapter;
    private ArrayList<PostItem> posts;
    private String startDate;
    private String endDate;
    private TextView startDateTv;
    private TextView endDateTv;
    private Button submitBtn;
    private int startEndFlag = 0;   // 0 : start, 1 : end

    public HotBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_board, container, false);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog startDatePickerDialog = new DatePickerDialog(
                getContext(), HotBoardFragment.this, year, month, day);

        DatePickerDialog endDatePickerDialog = new DatePickerDialog(
                getContext(), HotBoardFragment.this, year, month, day);
        startDateTv = (TextView) view.findViewById(R.id.hot_startDate_tv);
        endDateTv = (TextView) view.findViewById(R.id.hot_endDate_tv);
        submitBtn = (Button) view.findViewById(R.id.hot_submit_btn);

        startDateTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startEndFlag = 0;
                startDatePickerDialog.show();
            }
        });

        endDateTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startEndFlag = 1;
                endDatePickerDialog.show();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate = (String) startDateTv.getText();
                endDate = (String) endDateTv.getText();
                posts.clear();
                getHotPosts();
            }
        });
        rv = view.findViewById(R.id.hot_board_rv);
        posts = new ArrayList<>();

        rvAdapter = new MainRVAdapter(getContext(), posts);
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        calendar.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        endDate = df.format(calendar.getTime());
        calendar.add(Calendar.DATE, -7);
        startDate = df.format(calendar.getTime());
        getHotPosts();

        return view;
    }

    public void getHotPosts(){
        new Runnable(){
            @Override
            public void run(){
                try{
                    LoadingDialog.INSTANCE.onLoadingDialog(getActivity());
                    ButterKnifeApi.INSTANCE.getRetrofitService().getPostHotBoard(startDate,endDate).enqueue(new Callback<PostRes>() {
                        @Override
                        public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                            if(response.body() != null){
                                try{
                                    List<PostItem> postList = response.body().getData();
                                    for(PostItem p : postList){
                                        if(p.getVideoUrl() != null)
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getVideoUrl(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments()));
                                        else if(p.getImageUrl().size() != 0)
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getImageUrl(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments()));
                                        else
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments()));
                                    }
                                    rvAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            LoadingDialog.INSTANCE.offLoadingDialog();
                        }
                        @Override
                        public void onFailure(Call<PostRes> call, Throwable t) {
                            // 서버 쪽으로 메시지를 보내지 못한 경우
                            Log.d("err", "SERVER CONNECTION ERROR");
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(startEndFlag == 0) {     // start
            startDateTv.setText(String.valueOf(year) + "/" + String.valueOf(month+1) + "/" + String.valueOf(dayOfMonth));
        }else if(startEndFlag == 1){     // end
            endDateTv.setText(String.valueOf(year) + "/" + String.valueOf(month+1) + "/" + String.valueOf(dayOfMonth));
        }
    }
}