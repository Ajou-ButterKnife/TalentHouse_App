package kr.butterknife.talenthouse.network.response

import kr.butterknife.talenthouse.PostItem
import java.util.*

data class CommonLoginRes(
    val _id : String,
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