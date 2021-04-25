package kr.butterknife.talenthouse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rv;
    private MainRVAdapter rvAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        FloatingActionButton btnWrite = view.findViewById(R.id.main_fab_write);

        btnWrite.setOnClickListener(this);

        rv = view.findViewById(R.id.main_rv);
        ArrayList<PostItem> arrayList = new ArrayList<>();

        //for test
        arrayList.add(new PostItem("asdfasdf", "asdf", LoginInfo.INSTANCE.getLoginInfo(getContext()), "2021.01.01", "adsfasdfasdfasdfasdfasdfasdfasdfasfasdfsdf"));
        arrayList.add(new PostItem("qwerqwer", "qwer", LoginInfo.INSTANCE.getLoginInfo(getContext()), "2021.01.01", "adsfasdfasdfasdfasdfasdfasdfasdfasfasdfsdf"));
        arrayList.add(new PostItem("zxcvzxcv", "zxcv", LoginInfo.INSTANCE.getLoginInfo(getContext()), "2021.01.01", "adsfasdfasdfasdfasdfasdfasdfasdfasfasdfsdf"));
        arrayList.add(new PostItem("xcvbxcvb", "xcvb", LoginInfo.INSTANCE.getLoginInfo(getContext()), "2021.01.01", "adsfasdfasdfasdfasdfasdfasdfasdfasfasdfsdf"));
        arrayList.add(new PostItem("sdfgsdfg", "sdfg", LoginInfo.INSTANCE.getLoginInfo(getContext()), "2021.01.01", "adsfasdfasdfasdfasdfasdfasdfasdfasfasdfsdf"));
        arrayList.add(new PostItem("wertwert", "wert", LoginInfo.INSTANCE.getLoginInfo(getContext()), "2021.01.01", "adsfasdfasdfasdfasdfasdfasdfasdfasfasdfsdf"));

        rvAdapter = new MainRVAdapter(arrayList);
        rvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                ((MainActivity) getActivity()).replaceFragment(
                        new ContentFragment(arrayList.get(pos)),
                        "Content"
                );
            }
        });

        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
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
}