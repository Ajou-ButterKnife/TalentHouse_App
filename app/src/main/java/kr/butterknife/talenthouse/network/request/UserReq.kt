package kr.butterknife.talenthouse.network.request

data class NormalLoginReq(
    val email : String,
    val password : String,
)

data class SignUpReq(
    val email : String,
    val password : String,
    val phone : String,
)

data class NormalSignUpReq(
        val email : String,
        val password : String,
        val phone : String,
        val nickname : String,
        val category : List<String>,
)

data class SocialLoginReq(
    val uid : String,
)

data class SocialSignUpReq(
    val phone : String,
    val nickname : String,
    val category : List<String>,
    val uid : String,
)

data class OverlapEmail(
    val email : String,
)

data class OverlapNickname(
    val nickname : String
)

data class UploadPostReq(
    val id : String,
    val nickname : String,
    val title : String,
    val description : String,
    val category : String,
    val imageUrl : List<String>?,
    val videoUrl : String?,
)

data class UploadCommentReq(
        val _id : String,
        val id : String,
        val nickname : String,
        val comment : String
)

data class PWUpdateReq(
    val password : String
)

data class UserInfoUpdateReq(
    var phone : String,
    var nickname : String,
    var category : List<String>,
)