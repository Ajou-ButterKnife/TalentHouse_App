 package kr.butterknife.talenthouse;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import android.widget.Spinner;
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

import java.io.File;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

public class WriteFragment extends Fragment implements View.OnClickListener{


    private String category;
    private Spinner spinner;
    private ImageView imageView;
    private VideoView videoView;
    private Button btnUploadImage;
    private Button btnUploadVideo;
    private Button btnUpWrite;
    private Intent intent;
    private Uri uri;
    private ArrayList<File> images;
    private File video;
    private String TEST = "TEST_TAG";
    private final int imageSelected = 10;
    private final int videoSelected = 20;


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
        btnUpWrite = view.findViewById(R.id.write_btn);




        btnUploadImage.setOnClickListener(this);
        btnUploadVideo.setOnClickListener(this);
        btnUpWrite.setOnClickListener(this);


        linearLayout = view.findViewById(R.id.fw_ll_image);
        horizontalScrollView = view.findViewById(R.id.fw_hsv);


        List<String> spinner_items = Arrays.asList(getResources().getStringArray(R.array.category_spinner));
        // 스피커와 리스트를 연결하기 위해 사용되는 어댑터
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinner_items);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 스피너의 어댑터 지정
        spinner.setAdapter(spinner_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();
                if(str.equals("카테고리") == false){
                    category = str;
                    Log.d("setCater", category);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        images = new ArrayList<>();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
            case R.id.write_btn:
                if(video == null)
                    uploadWithTransferUtilty(images);
                else
                    uploadWithTransferUtilty(video);
                break;

        }
    }

    public void uploadWithTransferUtilty(ArrayList<File> files) {
        // CredentialsProvider 객체 생성 (Cognito에서 자격 증명 풀 ID 제공)
        AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAQUQIUUPFLLOMLQUD", "Yi6ph/MUh6ISRliH2mv0jlwkqtg5hZoJ5LjQU4Ia");
        AmazonS3Client s3Client = new AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2));

        TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(getActivity().getApplicationContext()).build();
        TransferNetworkLossHandler.getInstance(getActivity().getApplicationContext());

        for(File file : files) {
            TransferObserver uploadObserver = transferUtility.upload("talent-house-app/photo", file.getName(), file);

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

        // arraylist http
                startActivityForResult(intent, videoSelected);

    }

    public void uploadWithTransferUtilty(File file) {
        System.out.println("asdf");
        Log.d("TAG", "TESTTEST");
        // CredentialsProvider 객체 생성 (Cognito에서 자격 증명 풀 ID 제공)
        AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAQUQIUUPFLLOMLQUD", "Yi6ph/MUh6ISRliH2mv0jlwkqtg5hZoJ5LjQU4Ia");
        AmazonS3Client s3Client = new AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2));

        TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(getActivity().getApplicationContext()).build();
        TransferNetworkLossHandler.getInstance(getActivity().getApplicationContext());

        TransferObserver uploadObserver = transferUtility.upload("talent-house-app/video", file.getName(), file);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case imageSelected:
                if(resultCode == -1){
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                    layoutParams.rightMargin = 5;
                    layoutParams.gravity = Gravity.CENTER;

                    for(int i=0 ; i<data.getClipData().getItemCount() ; i++){
                        ImageView tempImage = new ImageView(getContext());
                        tempImage.setImageURI(data.getClipData().getItemAt(i).getUri());
                        tempImage.setLayoutParams(layoutParams);
                        tempImage.setScaleType(ImageView.ScaleType.FIT_XY);
                        linearLayout.addView(tempImage);
                        Uri temp = data.getClipData().getItemAt(i).getUri();
                        images.add(new File(getRealPathFromURI(temp)));
                    }
                }else{
                    Toast.makeText(getContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case videoSelected:
                if(resultCode == -1){
                    // 선택한 사진의 경로(Uri) 객체 얻어오기
                    uri = data.getData();
                    if(uri != null) {
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

                }else{
                    Toast.makeText(getContext(), "동영상을 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
//        Uri uri = Uri.fromFile(new File(path));

        cursor.close();
        return path;
    }

}