package kr.butterknife.talenthouse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.request.FavoriteReq;
import kr.butterknife.talenthouse.network.response.FavoritePostRes;
import kr.butterknife.talenthouse.network.response.PostRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteFragment extends Fragment {

    private RecyclerView rv;
    private MainRVAdapter rvAdapter;
    private ArrayList<PostItem> posts;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_favorite, container, false);

        rv = view.findViewById(R.id.favorite_rv);
        textView = view.findViewById(R.id.favorite_tv);
        posts = new ArrayList<>();
        rvAdapter = new MainRVAdapter(getContext(), posts);
        rvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@Nullable View v, int pos) {
                ((MainActivity)getActivity()).replaceFragment(
                        new ContentFragment(posts.get(pos)),
                        "Content"
                );
            }
        });
        rvAdapter.setOnSettingListener((v, postId) -> {
            Util.INSTANCE.postSetting(requireActivity(), requireContext(), v, postId, posts, (item) -> {
                ((MainActivity) getActivity()).replaceFragment(new WriteFragment(), "Write", item);
                return true;
            }, (idx) -> {
                posts.remove((int) idx);
                rvAdapter.notifyItemRemoved(idx);
                return true;
            });
        });
        rvAdapter.initScrollListener(rv);
        rvAdapter.setOnItemReloadListener(() -> getFavoritePosts());

        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        rvAdapter.doItemReload();
        return view;
    }

    public void getFavoritePosts(){
        new Runnable(){
            @Override
            public void run() {
                try{
                    LoadingDialog.INSTANCE.onLoadingDialog(getActivity());
                    String userId = LoginInfo.INSTANCE.getLoginInfo(getContext())[0];
                    ButterKnifeApi.INSTANCE.getRetrofitService().getFavoritePost(userId, String.valueOf(rvAdapter.getPageNum())).enqueue(new Callback<PostRes>() {
                        @Override
                        public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                            if(response.body() != null){
                                List<PostItem> postItemList = response.body().getData();
                                if(postItemList.size() != 0){
                                    for(PostItem p : postItemList) {
                                        if(p.getVideoUrl() != null)
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getVideoUrl(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments()));
                                        else if(p.getImageUrl().size() != 0)
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getImageUrl(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments()));
                                        else
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments()));
                                    }
                                    rvAdapter.notifyDataSetChanged();
                                }else{
                                    textView.setVisibility(View.VISIBLE);
                                }
                            }
                            LoadingDialog.INSTANCE.offLoadingDialog();
                        }
                        @Override
                        public void onFailure(Call<PostRes> call, Throwable t) {
                            Log.d("err", "SERVER CONNECTION ERROR");
                            LoadingDialog.INSTANCE.offLoadingDialog();
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                    LoadingDialog.INSTANCE.offLoadingDialog();
                }
            }
        }.run();
    }

}