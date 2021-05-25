package kr.butterknife.talenthouse;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.response.SearchPostRes;
import retrofit2.Call;
import retrofit2.Callback;


public class SearchFragment extends Fragment {
    private RecyclerView rvPost;
    private MainRVAdapter rvPostAdapter;
    private ArrayList<PostItem> posts;
    private int spinner_item = 1;

    private Button btnSearch;
    private Spinner spinnerSearch;
    private EditText tvSearch;
    private TextView emptyText;
    private TextView firstText;

    private String searchItem;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        rvPost = view.findViewById(R.id.search_rv);
        posts = new ArrayList<>();

        emptyText = view.findViewById(R.id.empty_text);
        firstText = view.findViewById(R.id.first_text);

        rvPostAdapter = new MainRVAdapter(getContext(), posts);
        rvPostAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                ((MainActivity) getActivity()).replaceFragment(
                        new ContentFragment(posts.get(pos)),
                        "Content"
                );
            }
        });
        rvPostAdapter.initScrollListener(rvPost);
        rvPostAdapter.setOnItemReloadListener(() -> getSearchPosts());
        rvPostAdapter.setOnSettingListener((v, postId) -> {
            Util.INSTANCE.postSetting(requireActivity(), requireContext(), v, postId, posts, (item) -> {
                ((MainActivity) getActivity()).replaceFragment(new WriteFragment(), "Write", item);
                return true;
            }, (idx) -> {
                posts.remove((int) idx);
                rvPostAdapter.notifyItemRemoved(idx);
                return true;
            });
        });

        rvPost.setAdapter(rvPostAdapter);
        rvPost.setLayoutManager(new LinearLayoutManager(getContext()));

        String[] searchSpinnerItems = getResources().getStringArray(R.array.searchType);
        spinnerSearch = view.findViewById(R.id.search_spinner);
        ArrayAdapter searchPostAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, searchSpinnerItems);
        spinnerSearch.setAdapter(searchPostAdapter);
        btnSearch = view.findViewById(R.id.search_btn);
        tvSearch = view.findViewById(R.id.search_tv);

        spinnerSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        spinner_item = 1;
                        break;
                    case 1:
                        spinner_item = 2;
                        break;
                }
                Log.d("spin", String.valueOf(spinner_item));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchItem = tvSearch.getText().toString();
                getSearchPosts();
                Util.INSTANCE.hideKeyboard(getContext(), view);

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(posts.isEmpty()) {
                            rvPost.setVisibility(View.GONE);
                            firstText.setVisibility(View.GONE);
                            emptyText.setVisibility(View.VISIBLE);
                        }
                        else {
                            rvPost.setVisibility(View.VISIBLE);
                            firstText.setVisibility(View.GONE);
                            emptyText.setVisibility(View.GONE);
//                }
                        }
                    }
                }, 500);// 0.5초 정도 딜레이를 준 후 시작
            }
        });

        rvPost.setVisibility(View.GONE);
        firstText.setVisibility(View.VISIBLE);

        return view;
    }

    public void getSearchPosts(){
        new Runnable(){
            @Override
            public void run(){
                try{
                    LoadingDialog.INSTANCE.onLoadingDialog(getActivity());
                    ButterKnifeApi.INSTANCE.getRetrofitService().getSearchPosts(spinner_item, searchItem, rvPostAdapter.getPageNum()).enqueue(new Callback<SearchPostRes>() {
                        @Override
                        public void onResponse(Call<SearchPostRes> call, retrofit2.Response<SearchPostRes> response) {
                            if(response.body() != null){
                                try{
                                    List<PostItem> postList = response.body().getData();
                                    for(PostItem p : postList){
                                        if(p.getVideoUrl() != null)
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getVideoUrl(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments(), p.getProfile()));
                                        else if(p.getImageUrl().size() != 0)
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getImageUrl(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments(), p.getProfile()));
                                        else
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments(), p.getProfile()));
                                    }
                                    Log.d("TESTTEST", String.valueOf(postList));
                                    rvPostAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            LoadingDialog.INSTANCE.offLoadingDialog();
                        }

                        @Override
                        public void onFailure(Call<SearchPostRes> call, Throwable t) {
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


//    public void clearPlayer(){
//        rvAdapter.clearPlayerList();
//    }
}
