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
import retrofit2.http.*

//private const val BASE_URL = "http://3.137.162.68:4000/"
private const val BASE_URL = "http://10.0.2.2:4000/"
//private const val BASE_URL = "http://172.30.1.5:4000/"

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

    @POST("login/normal")
    fun login(@Body user : NormalLoginReq) : Call<NormalLoginRes>

    @POST("login/social")
    fun socialLogin(@Body uid : SocialLoginReq) : Call<SocialLoginRes>

    @POST("signup/normal")
    fun normalAddUser(@Body user : NormalSignUpReq) : Call<NormalSignUpRes>

    @POST("signup/nickname_overlap_check")
    fun overlapCheck(@Body data : OverlapNickname) : Call<CommonResponse>

    @POST("signup/email_overlap_check")
    fun overlapCheck(@Body data : OverlapEmail) : Call<CommonResponse>

    @POST("signup/social")
    fun socialAddUser(@Body user : SocialSignUpReq) : Call<SocialSignUpRes>

    @GET("post")
    fun getPosts(@Query("category") category: String, @Query("page") page: Int) : Call<PostRes>

    @POST("post/create")
    fun postCreate(@Body data : UploadPostReq) : Call<CommonResponse>

    @GET("post/comment/{postId}")
    fun getComments(@Path("postId") key: String): Call<GetCommentsRes>

    @POST("post/comment/create")
    fun commentCreate(@Body data : UploadCommentReq) : Call<CommentRes>

    @GET("post/{id}/{page}")
    suspend fun getMyPagePosts(@Path("id") id : String, @Path("page") page : Int) : PostRes

    @GET("user/category/{id}")
    fun getCategories(@Path("id") key: String) : Call<CategoryRes>

    @GET("user/nickname/{id}")
    suspend fun getUserNickname(@Path("id") id : String) : MyPageRes

    @GET("user/{id}")
    suspend fun getUserInfo(@Path("id") id : String) : UserInfoRes

    @PUT("user/password/{id}")
    suspend fun updatePassword(@Path("id") id : String, @Body data : PWUpdateReq) : CommonResponse

    @PUT("user/{id}")
    suspend fun updateInfo(@Path("id") id : String, @Body data : UserInfoUpdateReq) : CommonResponse
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