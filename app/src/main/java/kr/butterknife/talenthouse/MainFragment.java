package kr.butterknife.talenthouse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.response.Category;
import kr.butterknife.talenthouse.network.response.CategoryRes;
import kr.butterknife.talenthouse.network.response.PostRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rv;
    private MainRVAdapter rvAdapter;
    private ArrayList<PostItem> posts;
    private int page = 0;
    private String id;
    private boolean isLoading = false;

    private RecyclerView rvCategory;
    private MainCategoryRVAdapter rvCategoryAdapter;
    private ArrayList<String> categoryList;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        FloatingActionButton btnWrite = view.findViewById(R.id.main_fab_write);

        btnWrite.setOnClickListener(this);

        rvCategory = view.findViewById(R.id.main_rv_category);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCategory.setLayoutManager(layoutManager);

        categoryList = new ArrayList<>();
        getCategories();
        rvCategoryAdapter = new MainCategoryRVAdapter(getContext(), categoryList, onClickItem);
        rvCategory.setAdapter(rvCategoryAdapter);
        MyListDecoration decoration = new MyListDecoration();
        rvCategory.addItemDecoration(decoration);


        rv = view.findViewById(R.id.main_rv);
        posts = new ArrayList<>();

        rvAdapter = new MainRVAdapter(getContext(), posts);
        rvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                ((MainActivity) getActivity()).replaceFragment(
                        new ContentFragment(posts.get(pos)),
                        "Content"
                );
            }
        });

        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        getPosts();
        initScrollListener();
        return view;
    }

    public void getPosts(){
        new Runnable(){
            @Override
            public void run(){
                try{
                    ButterKnifeApi.INSTANCE.getRetrofitService().getPosts(page).enqueue(new Callback<PostRes>() {
                        @Override
                        public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                            if(response.body() != null){
                                try{
                                    List<PostItem> postList = response.body().getData();
                                    for(PostItem p : postList){
                                        Log.d("aaa", p.get_id());
                                        if(p.getVideoUrl() != null)
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getVideoUrl(), p.getLikeCnt(), p.getCategory(), p.getComments()));
                                        else if(p.getImageUrl() != null)
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getLikeCnt(), p.getCategory(), p.getComments()));
                                        else
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getLikeCnt(), p.getCategory(), p.getComments()));
                                    }
                                    rvAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<PostRes> call, Throwable t) {
                            // 서버 쪽으로 메시지를 보내지 못한 경우
                            Log.d("err", "SERVER CONNECTION ERROR");
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.run();
    }

    public void getCategories(){
        new Runnable(){
            @Override
            public void run() {
                try{
                    id = LoginInfo.INSTANCE.getLoginInfo(getContext())[0];

//                    String encodeStr= URLEncoder.encode(id, "UTF-8");
                    ButterKnifeApi.INSTANCE.getRetrofitService().getCategories(id).enqueue(new Callback<CategoryRes>() {
                        @Override
                        public void onResponse(Call<CategoryRes> call, Response<CategoryRes> response) {
                            if(response.body() != null){
                                try{
                                    List<String>cateList = response.body().getData().getCategory();
                                    for(String c : cateList) {
                                        categoryList.add(c);
                                    }
                                    rvCategoryAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<CategoryRes> call, Throwable t) {
                            Log.d("err", "SERVER CONNECTION ERROR");
                        }
                    });
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.run();
    }

    private void initScrollListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(!isLoading) {
                    if(layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == posts.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        posts.add(null);
        int tempSize = posts.size() - 1;
        rvAdapter.notifyItemInserted(tempSize);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                posts.remove(tempSize);
                int scrollPosition = posts.size();
                rvAdapter.notifyItemRemoved(tempSize - 1);
                int currentSize = scrollPosition;
                page++;
                getPosts();
                rvAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 500);

    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.main_fab_write:
                WriteFragment writeFragment = new WriteFragment();
                ((MainActivity)getActivity()).replaceFragment(writeFragment, "Write");
                break;
        }
    }

    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = (String) v.getTag();
            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();

        }
    };
}