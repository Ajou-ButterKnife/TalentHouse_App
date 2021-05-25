package kr.butterknife.talenthouse;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.request.PutLikeReq;
import kr.butterknife.talenthouse.network.response.LikeRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//https://youngest-programming.tistory.com/69
public class MainRVViewHolder {
    static class ContentNOViewHolder extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected TextView subject;
        protected TextView likeCnt;
        protected Button likeBtn;

        public ContentNOViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rvtext_tv_title);
            writer = itemView.findViewById(R.id.rvtext_tv_writer);
            date = itemView.findViewById(R.id.rvtext_tv_date);
            subject = itemView.findViewById(R.id.rvtext_tv_subject);
            likeCnt = itemView.findViewById(R.id.rvtext_tv_like);
            likeBtn = itemView.findViewById(R.id.rvtext_btn_like);
        }

        void updateLike(String postId, String userId, String nickname, String profile) {
            new Runnable() {
                @Override
                public void run() {
                    try {
                        PutLikeReq putLikeReq = new PutLikeReq(userId, nickname, profile);
                        ButterKnifeApi.INSTANCE.getRetrofitService().putLike(postId, putLikeReq).enqueue(new Callback<LikeRes>() {
                            @Override
                            public void onResponse(Call<LikeRes> call, Response<LikeRes> response) {
                                if (response.body() != null) {
                                    if (response.body().getResult().equals("Plus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setText("좋아요 취소");
                                    } else if (response.body().getResult().equals("Minus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setText("좋아요");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<LikeRes> call, Throwable t) {
                                // 서버 쪽으로 메시지를 보내지 못한 경우
                                Log.d("err", "SERVER CONNECTION ERROR");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.run();
        }
    }

    static class ContentImageViewHolder_1 extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected ViewStub viewStubImage;
        protected TextView likeCnt;
        protected ImageButton likeBtn;
        protected PostItem postItem;
        protected ImageButton settingBtn;
        protected ImageView profile;
        View inflated;

        public ContentImageViewHolder_1(@NonNull View itemView, ViewStub viewStub) {
            super(itemView);
            title = itemView.findViewById(R.id.rvimage_tv_title);
            writer = itemView.findViewById(R.id.rvimage_tv_writer);
            date = itemView.findViewById(R.id.rvimage_tv_date);
            likeCnt = itemView.findViewById(R.id.rvimage_tv_like);
            likeBtn = itemView.findViewById(R.id.rvimage_btn_like);
            settingBtn = itemView.findViewById(R.id.rvimage_btn_setting);
            profile = itemView.findViewById(R.id.rvimage_iv_profile);

            this.viewStubImage = viewStub;
            viewStubImage.setLayoutResource(R.layout.viewstub_main_image_1);
            inflated = viewStubImage.inflate();
        }

        void onBind(PostItem postItem, Context context) {
            this.postItem = postItem;
            title.setText(postItem.getTitle());
            writer.setText(postItem.getWriterNickname());
            date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(postItem.getUpdateTime())));
            Glide.with(context)
                    .load(postItem.getProfile())
                    .into(profile);
            boolean check = false;
            for (idNickname temp : postItem.getLikeIDs()) {
                if (temp.getUserId().equals(LoginInfo.INSTANCE.getLoginInfo(context)[0])) {
                    check = true;
                    break;
                }
            }
            if (check)
                likeBtn.setImageResource(R.drawable.btn_after_like);
            else
                likeBtn.setImageResource(R.drawable.btn_before_like);
            likeCnt.setText("좋아요 " + postItem.getLikeCnt() + "개");
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateLike(postItem.get_id(), LoginInfo.INSTANCE.getLoginInfo(context)[0], LoginInfo.INSTANCE.getLoginInfo(context)[1], LoginInfo.INSTANCE.getLoginInfo(context)[2]);
                }
            });
            List<String> urlList = postItem.getImageUrl();
            Glide.with(context)
                    .load(urlList.get(0))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv_1));
        }

        void updateLike(String postId, String userId, String nickname, String profile) {
            new Runnable() {
                @Override
                public void run() {
                    try {
                        PutLikeReq putLikeReq = new PutLikeReq(userId, nickname, profile);
                        ButterKnifeApi.INSTANCE.getRetrofitService().putLike(postId, putLikeReq).enqueue(new Callback<LikeRes>() {
                            @Override
                            public void onResponse(Call<LikeRes> call, Response<LikeRes> response) {
                                if (response.body() != null) {
                                    if (response.body().getResult().equals("Plus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setImageResource(R.drawable.btn_after_like);
                                    } else if (response.body().getResult().equals("Minus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setImageResource(R.drawable.btn_before_like);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<LikeRes> call, Throwable t) {
                                // 서버 쪽으로 메시지를 보내지 못한 경우
                                Log.d("err", "SERVER CONNECTION ERROR");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.run();
        }
    }

    static class ContentImageViewHolder_2 extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected ViewStub viewStubImage;
        protected PostItem postItem;
        protected TextView likeCnt;
        protected ImageButton likeBtn;
        protected ImageButton settingBtn;
        View inflated;

        public ContentImageViewHolder_2(@NonNull View itemView, ViewStub viewStub) {
            super(itemView);
            title = itemView.findViewById(R.id.rvimage_tv_title);
            writer = itemView.findViewById(R.id.rvimage_tv_writer);
            date = itemView.findViewById(R.id.rvimage_tv_date);
            likeCnt = itemView.findViewById(R.id.rvimage_tv_like);
            likeBtn = itemView.findViewById(R.id.rvimage_btn_like);
            this.viewStubImage = viewStub;
            viewStubImage.setLayoutResource(R.layout.viewstub_main_image_2);
            inflated = viewStubImage.inflate();
            settingBtn = itemView.findViewById(R.id.rvimage_btn_setting);
        }

        void onBind(PostItem postItem, Context context) {
            this.postItem = postItem;
            title.setText(postItem.getTitle());
            writer.setText(postItem.getWriterNickname());
            date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(postItem.getUpdateTime())));

            boolean check = false;
            for (idNickname temp : postItem.getLikeIDs()) {
                if (temp.getUserId().equals(LoginInfo.INSTANCE.getLoginInfo(context)[0])) {
                    check = true;
                    break;
                }
            }
            if (check)
                likeBtn.setImageResource(R.drawable.btn_after_like);
            else
                likeBtn.setImageResource(R.drawable.btn_before_like);
            likeCnt.setText("좋아요 " + postItem.getLikeCnt() + "개");
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateLike(postItem.get_id(), LoginInfo.INSTANCE.getLoginInfo(context)[0], LoginInfo.INSTANCE.getLoginInfo(context)[1], LoginInfo.INSTANCE.getLoginInfo(context)[2]);
                }
            });

            List<String> urlList = postItem.getImageUrl();
            Glide.with(context)
                    .load(urlList.get(0))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv2_1));
            Glide.with(context)
                    .load(urlList.get(1))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv2_2));
        }

        void updateLike(String postId, String userId, String nickname, String profile) {
            new Runnable() {
                @Override
                public void run() {
                    try {
                        PutLikeReq putLikeReq = new PutLikeReq(userId, nickname, profile);
                        ButterKnifeApi.INSTANCE.getRetrofitService().putLike(postId, putLikeReq).enqueue(new Callback<LikeRes>() {
                            @Override
                            public void onResponse(Call<LikeRes> call, Response<LikeRes> response) {
                                if (response.body() != null) {
                                    if (response.body().getResult().equals("Plus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setImageResource(R.drawable.btn_after_like);
                                    } else if (response.body().getResult().equals("Minus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setImageResource(R.drawable.btn_before_like);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<LikeRes> call, Throwable t) {
                                // 서버 쪽으로 메시지를 보내지 못한 경우
                                Log.d("err", "SERVER CONNECTION ERROR");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.run();
        }
    }

    static class ContentImageViewHolder_3 extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected ViewStub viewStubImage;
        protected PostItem postItem;
        protected TextView likeCnt;
        protected ImageButton likeBtn;
        protected ImageButton settingBtn;
        View inflated;

        public ContentImageViewHolder_3(@NonNull View itemView, ViewStub viewStub) {
            super(itemView);
            title = itemView.findViewById(R.id.rvimage_tv_title);
            writer = itemView.findViewById(R.id.rvimage_tv_writer);
            date = itemView.findViewById(R.id.rvimage_tv_date);
            likeCnt = itemView.findViewById(R.id.rvimage_tv_like);
            likeBtn = itemView.findViewById(R.id.rvimage_btn_like);
            this.viewStubImage = viewStub;
            viewStubImage.setLayoutResource(R.layout.viewstub_main_image_3);
            inflated = viewStubImage.inflate();
            settingBtn = itemView.findViewById(R.id.rvimage_btn_setting);
        }

        void onBind(PostItem postItem, Context context) {
            this.postItem = postItem;
            title.setText(postItem.getTitle());
            writer.setText(postItem.getWriterNickname());
            date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(postItem.getUpdateTime())));

            boolean check = false;
            for (idNickname temp : postItem.getLikeIDs()) {
                if (temp.getUserId().equals(LoginInfo.INSTANCE.getLoginInfo(context)[0])) {
                    check = true;
                    break;
                }
            }
            if (check)
                likeBtn.setImageResource(R.drawable.btn_after_like);
            else
                likeBtn.setImageResource(R.drawable.btn_before_like);
            likeCnt.setText("좋아요 " + postItem.getLikeCnt() + "개");
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateLike(postItem.get_id(), LoginInfo.INSTANCE.getLoginInfo(context)[0], LoginInfo.INSTANCE.getLoginInfo(context)[1], LoginInfo.INSTANCE.getLoginInfo(context)[2]);
                }
            });

            List<String> urlList = postItem.getImageUrl();
            Glide.with(context)
                    .load(urlList.get(0))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv3_1));
            Glide.with(context)
                    .load(urlList.get(1))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv3_2));
            Glide.with(context)
                    .load(urlList.get(2))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv3_3));
        }

        void updateLike(String postId, String userId, String nickname, String profile) {
            new Runnable() {
                @Override
                public void run() {
                    try {
                        PutLikeReq putLikeReq = new PutLikeReq(userId, nickname, profile);
                        ButterKnifeApi.INSTANCE.getRetrofitService().putLike(postId, putLikeReq).enqueue(new Callback<LikeRes>() {
                            @Override
                            public void onResponse(Call<LikeRes> call, Response<LikeRes> response) {
                                if (response.body() != null) {
                                    if (response.body().getResult().equals("Plus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setImageResource(R.drawable.btn_after_like);
                                    } else if (response.body().getResult().equals("Minus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setImageResource(R.drawable.btn_before_like);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<LikeRes> call, Throwable t) {
                                // 서버 쪽으로 메시지를 보내지 못한 경우
                                Log.d("err", "SERVER CONNECTION ERROR");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.run();
        }
    }

    static class ContentImageViewHolder_4 extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected ViewStub viewStubImage;
        protected PostItem postItem;
        protected TextView likeCnt;
        protected ImageButton likeBtn;
        protected ImageButton settingBtn;
        View inflated;

        public ContentImageViewHolder_4(@NonNull View itemView, ViewStub viewStub) {
            super(itemView);
            title = itemView.findViewById(R.id.rvimage_tv_title);
            writer = itemView.findViewById(R.id.rvimage_tv_writer);
            date = itemView.findViewById(R.id.rvimage_tv_date);
            likeCnt = itemView.findViewById(R.id.rvimage_tv_like);
            likeBtn = itemView.findViewById(R.id.rvimage_btn_like);
            this.viewStubImage = viewStub;
            viewStubImage.setLayoutResource(R.layout.viewstub_main_image_4);
            inflated = viewStubImage.inflate();
            settingBtn = itemView.findViewById(R.id.rvimage_btn_setting);
        }

        void onBind(PostItem postItem, Context context) {
            this.postItem = postItem;
            title.setText(postItem.getTitle());
            writer.setText(postItem.getWriterNickname());
            date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(postItem.getUpdateTime())));

            boolean check = false;
            for (idNickname temp : postItem.getLikeIDs()) {
                if (temp.getUserId().equals(LoginInfo.INSTANCE.getLoginInfo(context)[0])) {
                    check = true;
                    break;
                }
            }
            if (check)
                likeBtn.setImageResource(R.drawable.btn_after_like);
            else
                likeBtn.setImageResource(R.drawable.btn_before_like);
            likeCnt.setText("좋아요 " + postItem.getLikeCnt() + "개");
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateLike(postItem.get_id(), LoginInfo.INSTANCE.getLoginInfo(context)[0], LoginInfo.INSTANCE.getLoginInfo(context)[1], LoginInfo.INSTANCE.getLoginInfo(context)[2]);
                }
            });

            List<String> urlList = postItem.getImageUrl();
            Glide.with(context)
                    .load(urlList.get(0))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv4_1));
            Glide.with(context)
                    .load(urlList.get(1))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv4_2));
            Glide.with(context)
                    .load(urlList.get(2))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv4_3));
            Glide.with(context)
                    .load(urlList.get(3))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv4_4));
        }

        void updateLike(String postId, String userId, String nickname, String profile) {
            new Runnable() {
                @Override
                public void run() {
                    try {
                        PutLikeReq putLikeReq = new PutLikeReq(userId, nickname, profile);
                        ButterKnifeApi.INSTANCE.getRetrofitService().putLike(postId, putLikeReq).enqueue(new Callback<LikeRes>() {
                            @Override
                            public void onResponse(Call<LikeRes> call, Response<LikeRes> response) {
                                if (response.body() != null) {
                                    if (response.body().getResult().equals("Plus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setImageResource(R.drawable.btn_after_like);
                                    } else if (response.body().getResult().equals("Minus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setImageResource(R.drawable.btn_before_like);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<LikeRes> call, Throwable t) {
                                // 서버 쪽으로 메시지를 보내지 못한 경우
                                Log.d("err", "SERVER CONNECTION ERROR");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.run();
        }
    }

    static class ContentImageViewHolder_5 extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected ViewStub viewStubImage;
        protected PostItem postItem;
        protected TextView likeCnt;
        protected ImageButton likeBtn;
        protected ImageButton settingBtn;
        View inflated;

        public ContentImageViewHolder_5(@NonNull View itemView, ViewStub viewStub) {
            super(itemView);
            title = itemView.findViewById(R.id.rvimage_tv_title);
            writer = itemView.findViewById(R.id.rvimage_tv_writer);
            date = itemView.findViewById(R.id.rvimage_tv_date);
            likeCnt = itemView.findViewById(R.id.rvimage_tv_like);
            likeBtn = itemView.findViewById(R.id.rvimage_btn_like);
            this.viewStubImage = viewStub;
            viewStubImage.setLayoutResource(R.layout.viewstub_main_image_5);
            inflated = viewStubImage.inflate();
            settingBtn = itemView.findViewById(R.id.rvimage_btn_setting);
        }

        void onBind(PostItem postItem, Context context) {
            this.postItem = postItem;
            title.setText(postItem.getTitle());
            writer.setText(postItem.getWriterNickname());
            date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(postItem.getUpdateTime())));

            boolean check = false;
            for (idNickname temp : postItem.getLikeIDs()) {
                if (temp.getUserId().equals(LoginInfo.INSTANCE.getLoginInfo(context)[0])) {
                    check = true;
                    break;
                }
            }
            if (check)
                likeBtn.setImageResource(R.drawable.btn_after_like);
            else
                likeBtn.setImageResource(R.drawable.btn_before_like);
            likeCnt.setText("좋아요 " + postItem.getLikeCnt() + "개");
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateLike(postItem.get_id(), LoginInfo.INSTANCE.getLoginInfo(context)[0], LoginInfo.INSTANCE.getLoginInfo(context)[1], LoginInfo.INSTANCE.getLoginInfo(context)[2]);
                }
            });

            List<String> urlList = postItem.getImageUrl();
            Glide.with(context)
                    .load(urlList.get(0))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_1));
            Glide.with(context)
                    .load(urlList.get(1))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_2));
            Glide.with(context)
                    .load(urlList.get(2))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_3));
            Glide.with(context)
                    .load(urlList.get(3))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_4));
            Glide.with(context)
                    .load(urlList.get(4))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_5));
        }

        void updateLike(String postId, String userId, String nickname, String profile) {
            new Runnable() {
                @Override
                public void run() {
                    try {
                        PutLikeReq putLikeReq = new PutLikeReq(userId, nickname, profile);
                        ButterKnifeApi.INSTANCE.getRetrofitService().putLike(postId, putLikeReq).enqueue(new Callback<LikeRes>() {
                            @Override
                            public void onResponse(Call<LikeRes> call, Response<LikeRes> response) {
                                if (response.body() != null) {
                                    if (response.body().getResult().equals("Plus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setImageResource(R.drawable.btn_after_like);
                                    } else if (response.body().getResult().equals("Minus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setImageResource(R.drawable.btn_before_like);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<LikeRes> call, Throwable t) {
                                // 서버 쪽으로 메시지를 보내지 못한 경우
                                Log.d("err", "SERVER CONNECTION ERROR");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.run();
        }
    }

    static class ContentImageViewHolder_6 extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected ViewStub viewStubImage;
        protected PostItem postItem;
        protected TextView likeCnt;
        protected ImageButton likeBtn;
        protected ImageButton settingBtn;
        View inflated;

        public ContentImageViewHolder_6(@NonNull View itemView, ViewStub viewStub) {
            super(itemView);
            title = itemView.findViewById(R.id.rvimage_tv_title);
            writer = itemView.findViewById(R.id.rvimage_tv_writer);
            date = itemView.findViewById(R.id.rvimage_tv_date);
            likeCnt = itemView.findViewById(R.id.rvimage_tv_like);
            likeBtn = itemView.findViewById(R.id.rvimage_btn_like);
            this.viewStubImage = viewStub;
            viewStubImage.setLayoutResource(R.layout.viewstub_main_image_5);
            inflated = viewStubImage.inflate();
            settingBtn = itemView.findViewById(R.id.rvimage_btn_setting);
        }

        void onBind(PostItem postItem, Context context) {
            this.postItem = postItem;
            title.setText(postItem.getTitle());
            writer.setText(postItem.getWriterNickname());
            date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(postItem.getUpdateTime())));

            boolean check = false;
            for (idNickname temp : postItem.getLikeIDs()) {
                if (temp.getUserId().equals(LoginInfo.INSTANCE.getLoginInfo(context)[0])) {
                    check = true;
                    break;
                }
            }
            if (check)
                likeBtn.setImageResource(R.drawable.btn_after_like);
            else
                likeBtn.setImageResource(R.drawable.btn_before_like);
            likeCnt.setText("좋아요 " + postItem.getLikeCnt() + "개");
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateLike(postItem.get_id(), LoginInfo.INSTANCE.getLoginInfo(context)[0], LoginInfo.INSTANCE.getLoginInfo(context)[1], LoginInfo.INSTANCE.getLoginInfo(context)[2]);
                }
            });

            List<String> urlList = postItem.getImageUrl();
            Glide.with(context)
                    .load(urlList.get(0))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_1));
            Glide.with(context)
                    .load(urlList.get(1))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_2));
            Glide.with(context)
                    .load(urlList.get(2))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_3));
            Glide.with(context)
                    .load(urlList.get(3))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_4));
            Glide.with(context)
                    .load(urlList.get(3))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_5));

            ImageView imageView = inflated.findViewById(R.id.vs_main_iv5_5);
            TextView textView = inflated.findViewById(R.id.vs_main_tv5_5);
            int tempNum = urlList.size() - 5;
            textView.setText("+" + tempNum + "개 이상");
            imageView.setColorFilter(Color.parseColor("#55050900"));
        }

        void updateLike(String postId, String userId, String nickname, String profile) {
            new Runnable() {
                @Override
                public void run() {
                    try {
                        PutLikeReq putLikeReq = new PutLikeReq(userId, nickname, profile);
                        ButterKnifeApi.INSTANCE.getRetrofitService().putLike(postId, putLikeReq).enqueue(new Callback<LikeRes>() {
                            @Override
                            public void onResponse(Call<LikeRes> call, Response<LikeRes> response) {
                                if (response.body() != null) {
                                    if (response.body().getResult().equals("Plus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setImageResource(R.drawable.btn_after_like);
                                    } else if (response.body().getResult().equals("Minus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setImageResource(R.drawable.btn_before_like);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<LikeRes> call, Throwable t) {
                                // 서버 쪽으로 메시지를 보내지 못한 경우
                                Log.d("err", "SERVER CONNECTION ERROR");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.run();
        }
    }

    static class ContentVideoViewHolder extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected PlayerView pv;
        protected TextView likeCnt;
        protected ImageButton likeBtn;
        protected ImageButton settingBtn;
        protected ImageView profile;

        public ContentVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rvvideo_video_tv_title);
            writer = itemView.findViewById(R.id.rvvideo_video_tv_writer);
            date = itemView.findViewById(R.id.rvvideo_video_tv_date);
            pv = itemView.findViewById(R.id.rvvideo_video_player);
            likeCnt = itemView.findViewById(R.id.rvvideo_tv_like);
            likeBtn = itemView.findViewById(R.id.rvvideo_btn_like);
            settingBtn = itemView.findViewById(R.id.rvvideo_btn_setting);
            profile = itemView.findViewById(R.id.rvvideo_iv_profile);
        }

        void updateLike(String postId, String userId, String nickname, String profile) {
            new Runnable() {
                @Override
                public void run() {
                    try {
                        PutLikeReq putLikeReq = new PutLikeReq(userId, nickname, profile);
                        ButterKnifeApi.INSTANCE.getRetrofitService().putLike(postId, putLikeReq).enqueue(new Callback<LikeRes>() {
                            @Override
                            public void onResponse(Call<LikeRes> call, Response<LikeRes> response) {
                                if (response.body() != null) {
                                    if (response.body().getResult().equals("Plus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setImageResource(R.drawable.btn_after_like);
                                    } else if (response.body().getResult().equals("Minus")) {
                                        likeCnt.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                        likeBtn.setImageResource(R.drawable.btn_before_like);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<LikeRes> call, Throwable t) {
                                // 서버 쪽으로 메시지를 보내지 못한 경우
                                Log.d("err", "SERVER CONNECTION ERROR");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.run();
        }
    }

    static class ContentLoadingViewHolder extends ContentRVHolder {
        public ContentLoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

class ContentRVHolder extends RecyclerView.ViewHolder {

    private OnItemClickListener itemClickListener;

    void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public ContentRVHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(v -> {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(v, pos);
            }
        });
    }
}