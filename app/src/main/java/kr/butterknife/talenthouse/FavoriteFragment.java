package kr.butterknife.talenthouse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.request.FavoriteReq;
import kr.butterknife.talenthouse.network.response.FavoritePostIdRes;
import kr.butterknife.talenthouse.network.response.FavoritePostRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteFragment extends Fragment {

    private RecyclerView rv;
    private MainRVAdapter rvAdapter;
    private ArrayList<PostItem> posts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_favorite, container, false);

        rv = view.findViewById(R.id.favorite_rv);
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
            Util.INSTANCE.postSetting(requireContext(), v, postId, posts, (item) -> {
                ((MainActivity) getActivity()).replaceFragment(new WriteFragment(), "Write", item);
                return true;
            }, (idx) -> {
                posts.remove((int) idx);
                rvAdapter.notifyItemRemoved(idx);
                return true;
            });
        });

        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        getFavortePosts();
        return view;
    }
    
    // UserDB 로부터 likePostId를 가져옴
    public void getFavortePosts(){
        new Runnable(){
            @Override
            public void run() {
                try{
                    String userId = LoginInfo.INSTANCE.getLoginInfo(getContext())[0];
                    ButterKnifeApi.INSTANCE.getRetrofitService().getFavoritePostIds(userId).enqueue(new Callback<FavoritePostIdRes>() {
                        @Override
                        public void onResponse(Call<FavoritePostIdRes> call, Response<FavoritePostIdRes> response) {
                            if(response.body() != null){
                                List<String> postIdList = response.body().getData();
                                if(postIdList.size() != 0){
                                    getPostById(postIdList);
                                }else{

                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<FavoritePostIdRes> call, Throwable t) {
                            Log.d("err", "SERVER CONNECTION ERROR");
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.run();
    }

    public void getPostById(List<String> postIdList){
        new Runnable(){
            @Override
            public void run() {
                try{
                    ButterKnifeApi.INSTANCE.getRetrofitService().getFavoritePost(new FavoriteReq(postIdList)).enqueue(new Callback<FavoritePostRes>() {
                        @Override
                        public void onResponse(Call<FavoritePostRes> call, Response<FavoritePostRes> response) {
                            if(response.body() != null){
                                List<PostItem> postItemList = response.body().getData();
                                for(PostItem p : postItemList){
                                    if(p.getVideoUrl() != null)
                                        posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getVideoUrl(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments()));
                                    else if(p.getImageUrl().size() != 0)
                                        posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getImageUrl(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments()));
                                    else
                                        posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments()));
                                }
                                rvAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<FavoritePostRes> call, Throwable t) {
                            Log.d("err", "SERVER CONNECTION ERROR");
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.run();
    }

}