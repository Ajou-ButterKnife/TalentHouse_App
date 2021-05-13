package kr.butterknife.talenthouse.network.response

import com.squareup.moshi.Json
import kr.butterknife.talenthouse.CommentItem
import kr.butterknife.talenthouse.PostItem
import java.util.*

data class CommonLoginRes(
    val _id : String,
    val nickname : String,
)

data class CommonSignUpRes(
    val _id : String,
)

data class NormalLoginRes(
    val result : String,
    val detail : String?,
    val data : CommonLoginRes?,
)

data class SignUpRes(
    val email : String,
)

data class NormalSignUpRes(
        val result : String,
        val detail : String?,
)

data class SocialLoginRes(
    val result : String,
    val detail : String?,
    val data : CommonLoginRes?,
)

data class SocialSignUpRes(
    val result : String,
    val detail : String?,
    val data : CommonSignUpRes?,
)

data class PostRes(
        val data : List<PostItem>?,
)

data class Category(
    val category : List<String>?,
)

data class CategoryRes(
    val data : Category,
)

data class GetCommentsRes(
        val data: List<CommentItem>?
)

data class CommentRes(
        val result : String,
        val data : CommentItem?,
)

data class MyPageRes(
    val result : String,
    val detail : String,
    val data : CommonLoginRes?
)

data class UserInfo(
    @Json(name = "social_login_flag")
    val isSocial : Boolean,
    val category : List<String>,
    @Json(name = "phone_num")
    val phone : String,
    val nickname : String,
)

data class UserInfoRes(
    val result : String,
    val detail : String?,
    val data : UserInfo?
)