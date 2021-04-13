package kr.butterknife.talenthouse.network.request

data class LoginReq(
    val email : String,
    val password : String,
)

data class SignUpReq(
    val email : String,
    val password : String,
    val phone : String,
)

data class SocialLoginReq(
    val uid : String,
)

data class SocialSignUpReq(
    val phone : String,
    val nickname : String,
)