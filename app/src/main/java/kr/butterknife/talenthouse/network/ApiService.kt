package kr.butterknife.talenthouse.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kr.butterknife.talenthouse.network.request.*
import kr.butterknife.talenthouse.network.response.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

//private const val BASE_URL = "http://3.137.162.68:4000/"
private const val BASE_URL = "http://10.0.2.2:4000/"
//private const val BASE_URL = "http://172.30.1.3:4000/"


private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
private val clientBuilder = OkHttpClient.Builder().apply {
    addInterceptor(loggingInterceptor)
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface ButterKnifeApiService {

    @POST("signup/normal")
    fun normalAddUser(@Body user : NormalSignUpReq) : Call<NormalSignUpRes>

    @POST("login/normal")
    fun login(@Body user : NormalLoginReq) : Call<NormalLoginRes>

    @POST("login/social")
    fun socialLogin(@Body uid : SocialLoginReq) : Call<SocialLoginRes>

    @POST("signup/nickname_overlap_check")
    fun overlapCheck(@Body data : OverlapNickname) : Call<CommonResponse>

    @POST("signup/email_overlap_check")
    fun overlapCheck(@Body data : OverlapEmail) : Call<CommonResponse>

    @POST("signup/social")
    fun socialAddUser(@Body user : SocialSignUpReq) : Call<SocialSignUpRes>

    @GET("post")
    fun getPosts() :Call<PostRes>
  
    @POST("post/create")
    fun postCreate(@Body data : UploadPostReq) : Call<CommonResponse>

    @GET("post/comment/{postId}")
    fun getComments(@Path("postId") key: String): Call<GetCommentsRes>

    @POST("post/comment/create")
    fun commentCreate(@Body data : UploadCommentReq) : Call<CommentRes>
}

object ButterKnifeApi {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // json to kotlin
        .baseUrl(BASE_URL)
        .client(clientBuilder.build())
        .build()

    val retrofitService : ButterKnifeApiService by lazy {
        retrofit.create(ButterKnifeApiService::class.java)
    }
}