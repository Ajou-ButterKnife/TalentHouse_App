package kr.butterknife.talenthouse.network.request

data class NormalLoginReq(
    val email : String,
    val password : String
)

data class SignUpReq(
    val email : String,
    val password : String,
    val phone : String
)

data class NormalSignUpReq(
        val email : String,
        val password : String,
        val phone : String,
        val nickname : String,
        val category : List<String>
)

data class SocialLoginReq(
    val uid : String
)

data class SocialSignUpReq(
    val phone : String,
    val nickname : String,
    val category : List<String>,
    val uid : String
)

data class OverlapEmail(
    val email : String
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
    val videoUrl : String?
)

data class UploadCommentReq(
        val postId : String,
        val userId : String,
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

data class ProfileUpdateReq(
    val profile : String,
)

data class FCMTokenRegister(
    val token : String,
)

data class FavoriteReq(
    val postIdList : List<String>?,
)

data class FavoriteUserIdReq(
    val postId : String,
)


data class GetCommentReq(
        val postId : String,
)

data class IdReq(
    val _id : String,
)
  
data class DeleteCommentReq(
    val userId : String,
    val date : String
)

data class UpdateCommentReq(
    val userId : String,
    val date : String,
    val newComment : String
)