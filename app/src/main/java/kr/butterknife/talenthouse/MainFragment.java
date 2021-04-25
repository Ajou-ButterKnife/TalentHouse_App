package kr.butterknife.talenthouse;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        ArrayList<RVItem> arrayList = new ArrayList<>();

        //for test
        arrayList.add(new RVItem("asdfasdf", "asdf", "2021.01.01", "adsfasdfasdfasdfasdfasdfasdfasdfasfasdfsdf"));
        arrayList.add(new RVItem("qwerqwer", "qwer", "2021.01.01", "adsfasdfasdfasdfasdfasdfasdfasdfasfasdfsdf"));
        arrayList.add(new RVItem("zxcvzxcv", "zxcv", "2021.01.01", "adsfasdfasdfasdfasdfasdfasdfasdfasfasdfsdf"));
        arrayList.add(new RVItem("xcvbxcvb", "xcvb", "2021.01.01", "adsfasdfasdfasdfasdfasdfasdfasdfasfasdfsdf"));
        arrayList.add(new RVItem("sdfgsdfg", "sdfg", "2021.01.01", "adsfasdfasdfasdfasdfasdfasdfasdfasfasdfsdf"));
        arrayList.add(new RVItem("wertwert", "wert", "2021.01.01", "adsfasdfasdfasdfasdfasdfasdfasdfasfasdfsdf"));

        rvAdapter = new MainRVAdapter(arrayList);
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