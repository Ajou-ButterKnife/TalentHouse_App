package kr.butterknife.talenthouse.network.response

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
        val email : String,
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