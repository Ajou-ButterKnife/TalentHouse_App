package kr.butterknife.talenthouse.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

import kr.butterknife.talenthouse.LoadingDialog;
import kr.butterknife.talenthouse.LoginInfo;
import kr.butterknife.talenthouse.activity.MainActivity;
import kr.butterknife.talenthouse.adapter.MainCategoryRVAdapter;
import kr.butterknife.talenthouse.adapter.MainRVAdapter;
import kr.butterknife.talenthouse.MyListDecoration;
import kr.butterknife.talenthouse.OnItemClickListener;
import kr.butterknife.talenthouse.PostItem;
import kr.butterknife.talenthouse.R;
import kr.butterknife.talenthouse.Util;
import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.response.CategoryRes;
import kr.butterknife.talenthouse.network.response.PostRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rv;
    private MainRVAdapter rvAdapter;
    private ArrayList<PostItem> posts;
    private LinearLayoutManager linearLayoutManager;

    private RecyclerView rvCategory;
    private MainCategoryRVAdapter rvCategoryAdapter;
    private ArrayList<String> categoryList;
    private String categorySet;
    private String id;

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
        rvAdapter.initScrollListener(rv);
        rvAdapter.setOnItemReloadListener(() -> getPosts());
        rvAdapter.setOnMyPageListener(writerId -> {
            ((MainActivity) getActivity()).setMyPageID(writerId);
            ((MainActivity) getActivity()).outsideMyPageClick();
        });
        rvAdapter.setOnSettingListener((v, postId) -> {
            Util.INSTANCE.postSetting(requireContext(), v, postId, posts, (item) -> {
                ((MainActivity) getActivity()).replaceFragment(new WriteFragment(), "Write", item);
                return true;
            }, (idx) -> {
                posts.remove((int) idx);
                rvAdapter.notifyDataSetChanged();
                return true;
            });
        });

        rv.setAdapter(rvAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);
        getCategories();

        return view;
    }

    public void getPosts(){
        new Runnable(){
            @Override
            public void run(){
                try{
                    LoadingDialog.INSTANCE.onLoadingDialog(getActivity());
                    ButterKnifeApi.INSTANCE.getRetrofitService().getPosts(categorySet, rvAdapter.getPage()).enqueue(new Callback<PostRes>() {
                        @Override
                        public void onResponse(Call<PostRes> call, Response<PostRes> response) {
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
                                        rvAdapter.notifyItemInserted(posts.size() - 1);
                                    }
                                    if(postList.size() == 0)
                                        rvAdapter.setPage(rvAdapter.getPage() - 1);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            LoadingDialog.INSTANCE.offLoadingDialog();
                        }
                        @Override
                        public void onFailure(Call<PostRes> call, Throwable t) {
                            // ?????? ????????? ???????????? ????????? ?????? ??????
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

    public void getCategories(){
        new Runnable(){
            @Override
            public void run() {
                try{
                    LoadingDialog.INSTANCE.onLoadingDialog(getActivity());
                    id = LoginInfo.INSTANCE.getLoginInfo(getContext())[0];

                    ButterKnifeApi.INSTANCE.getRetrofitService().getCategories(id).enqueue(new Callback<CategoryRes>() {
                        @Override
                        public void onResponse(Call<CategoryRes> call, Response<CategoryRes> response) {
                            if(response.body() != null){
                                try{
                                    List<String> cateList = response.body().getData().getCategory();
                                    categorySet = categoryClassification((ArrayList<String>) cateList);
                                    for(String c : cateList) {
                                        categoryList.add(c);
                                    }
                                    rvAdapter.doItemReload();
                                    rvCategoryAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            LoadingDialog.INSTANCE.offLoadingDialog();
                        }
                        @Override
                        public void onFailure(Call<CategoryRes> call, Throwable t) {
                            Log.d("err", "SERVER CONNECTION ERROR");
                            LoadingDialog.INSTANCE.offLoadingDialog();
                        }
                    });
                }catch (Exception e) {
                    e.printStackTrace();
                    LoadingDialog.INSTANCE.offLoadingDialog();
                }
            }
        }.run();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.main_fab_write:
                WriteFragment writeFragment = new WriteFragment();
                ((MainActivity)getActivity()).replaceFragment(writeFragment, "Write", (PostItem) null);
                break;
        }
    }

    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = (String) v.getTag();
            categorySet = str;
            rvAdapter.setPage(0);
            posts.clear();
            rv.setAdapter(null);
            rv.setLayoutManager(null);
            rv.setAdapter(rvAdapter);
            rv.setLayoutManager(linearLayoutManager);
            rvAdapter.notifyDataSetChanged();
            rvAdapter.doItemReload();

        }
    };

    private String categoryClassification(ArrayList<String> strList) {
        String resultStr = "";
        for(int i = 0; i < strList.size(); i++) {
            if(strList.get(i).equals("???")) {
                resultStr += "???-";
            } else if(strList.get(i).equals("??????")) {
                resultStr += "??????-";
            } else if(strList.get(i).equals("???")) {
                resultStr += "???-";
            } else if(strList.get(i).equals("??????")) {
                resultStr += "??????-";
            } else if(strList.get(i).equals("??????")) {
                resultStr += "??????-";
            } else if(strList.get(i).equals("??????")) {
                resultStr += "??????-";
            } else {    // ?????? ??? ????????????
                Log.d("err", "????????? ??????????????? ??????????????????");
            }
        }

        if(resultStr.charAt(resultStr.length()-1) == '-') {
            resultStr = resultStr.substring(0 , resultStr.length()-1);
        }
        return resultStr;
    }

    public void clearPlayer(){
        rvAdapter.clearPlayerList();
    }
}