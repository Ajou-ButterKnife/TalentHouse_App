package kr.butterknife.talenthouse.network.response

data class NormalLoginRes(
    val result : String,
    val userId : Double,
)

data class SignUpRes(
    val email : String,
)