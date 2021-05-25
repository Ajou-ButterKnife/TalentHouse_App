package kr.butterknife.talenthouse;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.request.UploadPostReq;
import kr.butterknife.talenthouse.network.response.CommonResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteFragment extends Fragment implements View.OnClickListener {


    private String TAG = "UPLOAD_TAG";
    private String category;
    private Spinner spinner;
    private ImageView imageView;
    private VideoView videoView;
    private Button btnUploadImage;
    private Button btnUploadVideo;
    private Button btnUpPost;
    private Intent intent;
    private Uri uri;
    private List<imageObject> images;
    private File video;
    private final int imageSelected = 10;
    private final int videoSelected = 20;

    private TextInputEditText titleEt;
    private TextInputEditText descEt;

    private LinearLayout imageContainer, videoContainer;
    private PostItem beforeItem;


    HorizontalScrollView horizontalScrollView;
    LinearLayout linearLayout;

    public WriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write, container, false);

        spinner = view.findViewById(R.id.fw_spinner);

        videoView = view.findViewById(R.id.fw_vv);
        btnUploadImage = view.findViewById(R.id.fw_btn_uploadImage);
        btnUploadVideo = view.findViewById(R.id.fw_btn_uploadVideo);
        btnUpPost = view.findViewById(R.id.fw_btn_post);

        titleEt = view.findViewById(R.id.fw_et_title);
        descEt = view.findViewById(R.id.fw_et_desc);

        imageContainer = view.findViewById(R.id.fw_container_image);
        videoContainer = view.findViewById(R.id.fw_container_video);

        imageContainer.setVisibility(View.GONE);
        videoContainer.setVisibility(View.GONE);

        btnUploadImage.setOnClickListener(this);
        btnUploadVideo.setOnClickListener(this);
        btnUpPost.setOnClickListener(this);

        linearLayout = view.findViewById(R.id.fw_ll_image);
        horizontalScrollView = view.findViewById(R.id.fw_hsv);

        category = "카테고리";
        List<String> spinner_items = Arrays.asList(getResources().getStringArray(R.array.category_spinner));
        // 스피너와 리스트를 연결하기 위해 사용되는 어댑터
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinner_items);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 스피너의 어댑터 지정
        spinner.setAdapter(spinner_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();
                if (str.equals("카테고리") == false) {
                    category = str;
                    setLLVisibility(category);
                    Log.d("setCater", category);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        images = new ArrayList<>();

        beforeItem = (PostItem) getArguments().getSerializable("update");
        if (beforeItem != null) {
            titleEt.setText(beforeItem.getTitle());
            descEt.setText(beforeItem.getDescription());
//             spinner 아이템 세팅
            String[] temp = getResources().getStringArray(R.array.category_spinner);
            int initIdx = 0;
            for (; initIdx < temp.length; initIdx++) {
                if (temp[initIdx].equals(beforeItem.getCategory()))
                    break;
            }

            spinner.setSelection(initIdx);

//             LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = 5;
            layoutParams.gravity = Gravity.CENTER;

            if (beforeItem.getVideoUrl() != null) {
                MediaController mc = new MediaController(getContext());
                videoView.setVideoPath(beforeItem.getVideoUrl());
                videoView.setMediaController(mc);
                videoView.setBackground(null);
                videoView.requestFocus();
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        videoView.start();
                        videoView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                videoView.pause();
                            }
                        }, 100);
                    }
                });
            } else {
                for (int i = 0; i < beforeItem.getImageUrl().size(); i++) {
                    long currentTime = System.currentTimeMillis();
                    RelativeLayout relativeLayout = new RelativeLayout(getContext());
                    ImageView tempImage = new ImageView(getContext());
                    tempImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    relativeLayout.addView(tempImage);
                    RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(200, 200);
                    imageParams.setMargins(0, 10, 10, 10);
                    tempImage.setLayoutParams(imageParams);
                    tempImage.setId((int) currentTime);
                    Glide.with(requireContext()).load(beforeItem.getImageUrl().get(i)).into(tempImage);

                    TextView tempTextView = new TextView(getContext());
                    RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(40, 40);
                    textParams.addRule(RelativeLayout.ALIGN_RIGHT, tempImage.getId());
                    tempTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.deleteimage));
                    tempTextView.setLayoutParams(textParams);
                    tempTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int index = -1;
                            for(int i =0;i<images.size();i++){
                                if(images.get(i).getId() == tempImage.getId()){
                                    index = i;
                                    break;
                                }
                            }
                            images.remove(index);
                            relativeLayout.setVisibility(View.GONE);
                        }
                    });
                    images.add(new imageObject(null, tempImage.getId(), beforeItem.getImageUrl().get(i)));
                    relativeLayout.addView(tempTextView);
                    linearLayout.addView(relativeLayout);
                }
            }

        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fw_btn_uploadImage:
                intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, imageSelected);
                break;
            case R.id.fw_btn_uploadVideo:
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                startActivityForResult(intent, videoSelected);
                break;
            case R.id.fw_btn_post:
                String postTitle = titleEt.getText().toString();
                String postDesc = descEt.getText().toString();
                String postCategory = spinner.getSelectedItem().toString();
                String postId = LoginInfo.INSTANCE.getLoginInfo(getActivity().getApplicationContext())[0];
                Log.d("TESTTEST", postId);
                //String postId = "testId";
                if (beforeItem == null) {
                    if (video == null) {
                        // image 업로드일 경우
                        uploadWithTransferUtilty(images, postId);
                        ArrayList<String> postImageUrl = new ArrayList<>();
                        for (imageObject file : images) {
                            postImageUrl.add("https://talent-house-app.s3.ap-northeast-2.amazonaws.com/photo/" + postId + file.getFile().getName());
                        }
                        postPost(postTitle, postDesc, postCategory, postId, postImageUrl, null);
                    } else {
                        // video 업로드일 경우
                        uploadWithTransferUtilty(video, postId);
                        String postVideoUrl = "https://talent-house-app.s3.ap-northeast-2.amazonaws.com/video/" + postId + video.getName();
                        postPost(postTitle, postDesc, postCategory, postId, null, postVideoUrl);
                    }
                } else {
                    if (beforeItem.getVideoUrl() == null) {
                        // image
                        if (images.size() != 0)
                            uploadWithTransferUtilty(images, postId);
                        ArrayList<String> postImageUrl = new ArrayList<>();
                        for(imageObject temp : images){
                            if(temp.getFile() != null){
                                String tempUrl = "https://talent-house-app.s3.ap-northeast-2.amazonaws.com/photo/" + postId + temp.getFile().getName();
                                postImageUrl.add(tempUrl);
                            }else{
                                postImageUrl.add(temp.getBeforeUrl());
                            }
                        }
                        modifyPost(postTitle, postDesc, postCategory, beforeItem.get_id(), postImageUrl, null);
                    } else {
                        // video
                        String postVideoUrl = beforeItem.getVideoUrl();
                        if (video != null) {
                            uploadWithTransferUtilty(video, postId);
                            postVideoUrl = "https://talent-house-app.s3.ap-northeast-2.amazonaws.com/video/" + postId + video.getName();
                        }
                        modifyPost(postTitle, postDesc, postCategory, beforeItem.get_id(), null, postVideoUrl);
                    }
                }
                break;

        }
    }

    public void uploadWithTransferUtilty(List<imageObject> files, String id) {
        // CredentialsProvider 객체 생성 (Cognito에서 자격 증명 풀 ID 제공)
        AWSCredentials awsCredentials = new BasicAWSCredentials(getString(R.string.aws_access_key), getString(R.string.aws_secret_key));
        AmazonS3Client s3Client = new AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2));

        TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(getActivity().getApplicationContext()).build();
        TransferNetworkLossHandler.getInstance(getActivity().getApplicationContext());

        for (imageObject file : files) {
            if(file.getFile() == null){
                continue;
            }else{
                TransferObserver uploadObserver = transferUtility.upload("talent-house-app/photo", id + file.getFile().getName(), file.getFile());
                uploadObserver.setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        if (state == TransferState.COMPLETED) {
                            // Handle a completed upload
                        }
                    }
                    @Override
                    public void onProgressChanged(int id, long current, long total) {
                        int done = (int) (((double) current / total) * 100.0);
                        Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done");
                    }
                    @Override
                    public void onError(int id, Exception ex) {
                        Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX:" + ex.toString());
                    }
                });
                // If you prefer to long-poll for updates
                if (uploadObserver.getState() == TransferState.COMPLETED) {
                    /* Handle completion */
                }
            }
        }
    }

    public void uploadWithTransferUtilty(File file, String id) {
        // CredentialsProvider 객체 생성 (Cognito에서 자격 증명 풀 ID 제공)
        AWSCredentials awsCredentials = new BasicAWSCredentials(getString(R.string.aws_access_key), getString(R.string.aws_secret_key));
        AmazonS3Client s3Client = new AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2));

        TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(getActivity().getApplicationContext()).build();
        TransferNetworkLossHandler.getInstance(getActivity().getApplicationContext());

        TransferObserver uploadObserver = transferUtility.upload("talent-house-app/video", id + file.getName(), file);

        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state == TransferState.COMPLETED) {
                    // Handle a completed upload

                }
            }

            @Override
            public void onProgressChanged(int id, long current, long total) {
                int done = (int) (((double) current / total) * 100.0);
                Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX:" + ex.toString());
            }
        });
        // If you prefer to long-poll for updates
        if (uploadObserver.getState() == TransferState.COMPLETED) {
            /* Handle completion */
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case imageSelected:
                if (resultCode == -1) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                    layoutParams.rightMargin = 5;
                    layoutParams.gravity = Gravity.CENTER;

                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        long currentTime = System.currentTimeMillis();

                        RelativeLayout relativeLayout = new RelativeLayout(getContext());
                        ImageView tempImage = new ImageView(getContext());
                        tempImage.setScaleType(ImageView.ScaleType.FIT_XY);
                        relativeLayout.addView(tempImage);
                        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(700, 700);
                        imageParams.setMargins(0, 10, 10, 10);
                        tempImage.setLayoutParams(imageParams);
                        tempImage.setId((int) currentTime);
                        tempImage.setImageURI(data.getClipData().getItemAt(i).getUri());

                        TextView tempTextView = new TextView(getContext());
                        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(40, 40);
                        textParams.addRule(RelativeLayout.ALIGN_RIGHT, tempImage.getId());
                        tempTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.deleteimage));
                        tempTextView.setLayoutParams(textParams);
                        tempTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                relativeLayout.setVisibility(View.GONE);
                                int index = -1;
                                for(int i = 0 ; i < images.size() ; i++){
                                    if(images.get(i).getId() == tempImage.getId()){
                                        index = i;
                                        break;
                                    }
                                }
                                images.remove(index);
                            }
                        });
                        relativeLayout.addView(tempTextView);
                        linearLayout.addView(relativeLayout);

                        Uri temp = data.getClipData().getItemAt(i).getUri();
                        images.add(new imageObject(new File(getRealPathFromURI(temp)), tempImage.getId(), ""));
                    }
                } else {
                    Toast.makeText(getContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case videoSelected:
                if (resultCode == -1) {
                    // 선택한 사진의 경로(Uri) 객체 얻어오기
                    uri = data.getData();
                    if (uri != null) {
                        MediaController mc = new MediaController(getContext());
                        videoView.setMediaController(mc);
                        videoView.setVideoURI(uri);
                        videoView.setBackground(null);
                        videoView.requestFocus();
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                videoView.start();
                                videoView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        videoView.pause();
                                    }
                                }, 100);
                            }
                        });
                        video = new File(getRealPathFromURI(uri));
                    }

                } else {
                    Toast.makeText(getContext(), "동영상을 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.Images.Media.DATA};

        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        cursor.close();
        return path;
    }

    public void postPost(String title, String description, String category, String id, ArrayList<String> imageUrl, String videoUrl) {
        new Runnable() {
            @Override
            public void run() {
                try {
                    LoadingDialog.INSTANCE.onLoadingDialog(getActivity());
                    ButterKnifeApi.INSTANCE.getRetrofitService().postCreate(new UploadPostReq(id, LoginInfo.INSTANCE.getLoginInfo(getContext())[1], title, description, category, imageUrl, videoUrl)).enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            // 정상 출력이 되면 아래 로그가 출력됨
                            if (response.body() != null) {
                                CommonResponse result = response.body();
                                if (result.getResult().equals("Success")) {
                                    ((MainActivity) getActivity()).finishFragment(WriteFragment.this);
                                } else {

                                }
                            }
                            // 정상 출력이 되지 않을 때 서버에서의 response
                            else {
                                Log.d(TAG, response.errorBody().toString());
                                Log.d(TAG, response.message());
                                Log.d(TAG, String.valueOf(response.code()));
                            }
                            LoadingDialog.INSTANCE.offLoadingDialog();
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            // 서버쪽으로 아예 메시지를 보내지 못한 경우
                            Toast.makeText(getActivity().getApplicationContext(), "서버와 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "SERVER CONNECTION ERROR");
                            LoadingDialog.INSTANCE.offLoadingDialog();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    LoadingDialog.INSTANCE.offLoadingDialog();
                }
            }
        }.run();
    }

    public void modifyPost(String title, String description, String category, String id, ArrayList<String> imageUrl, String videoUrl) {
        new Runnable() {
            @Override
            public void run() {
                try {
                    LoadingDialog.INSTANCE.onLoadingDialog(getActivity());
                    ButterKnifeApi.INSTANCE.getRetrofitService().postUpdate(new UploadPostReq(
                            id,
                            LoginInfo.INSTANCE.getLoginInfo(getContext())[1],
                            title,
                            description,
                            category,
                            imageUrl,
                            videoUrl
                    )).enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            // 정상 출력이 되면 아래 로그가 출력됨
                            if (response.body() != null) {
                                CommonResponse result = response.body();
                                if (result.getResult().equals("Success")) {
                                    ((MainActivity) getActivity()).finishFragment(WriteFragment.this);
                                } else {

                                }
                            }
                            // 정상 출력이 되지 않을 때 서버에서의 response
                            else {
                                Log.d(TAG, response.errorBody().toString());
                                Log.d(TAG, response.message());
                                Log.d(TAG, String.valueOf(response.code()));
                            }
                            LoadingDialog.INSTANCE.offLoadingDialog();
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            // 서버쪽으로 아예 메시지를 보내지 못한 경우
                            Toast.makeText(getActivity().getApplicationContext(), "서버와 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "SERVER CONNECTION ERROR");
                            LoadingDialog.INSTANCE.offLoadingDialog();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    LoadingDialog.INSTANCE.offLoadingDialog();
                }
            }
        }.run();
    }

    public void setLLVisibility(String name) {
        String[] array = getResources().getStringArray(R.array.category_spinner);
        int idx;
        for (idx = 0; idx < array.length; idx++)
            if (array[idx].equals(name))
                break;

        switch (idx) {
            case 1:
            case 2:
            case 3:
                imageContainer.setVisibility(View.GONE);
                videoContainer.setVisibility(View.VISIBLE);
                break;
            case 4:
            case 5:
            case 6:
                imageContainer.setVisibility(View.VISIBLE);
                videoContainer.setVisibility(View.GONE);
                break;
            default:
                imageContainer.setVisibility(View.GONE);
                videoContainer.setVisibility(View.GONE);
                break;
        }
    }
}
