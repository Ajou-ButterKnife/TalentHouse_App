package kr.butterknife.talenthouse;

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
import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.response.CategoryRes;
import kr.butterknife.talenthouse.network.response.PostRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardFragment extends Fragment {

    private RecyclerView rv;
    private MainRVAdapter rvAdapter;
    private ArrayList<PostItem> posts;
    private String category;


    public BoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        rv = view.findViewById(R.id.board_rv);
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
        category = getArguments().getString("category");
        getPosts();

        return view;
    }

    public void getPosts(){
        new Runnable(){
            @Override
            public void run(){
                try{
                    ButterKnifeApi.INSTANCE.getRetrofitService().getPosts(category, rvAdapter.getPageNum()).enqueue(new Callback<PostRes>() {
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
}