package kr.butterknife.talenthouse

import com.squareup.moshi.Json
import java.io.Serializable

data class PostItem(
        val _id : String,
    val title : String,
    @Json(name = "writer_nickname")
    val writerNickname : String,
    @Json(name = "writer_id")
    val writerId : String,
    @Json(name = "update_time")
    val updateTime : String,
    val description : String,
    @Json(name = "mp3_url")
    var mp3Url : String? = null,
    @Json(name = "video_url")
    var videoUrl : String? = null,
    @Json(name = "image_url")
    var imageUrl : List<String>? = null,
    @Json(name = "like_cnt")
    val likeCnt : Int,
        @Json(name = "like_IDs")
        val likeIDs : List<String>,
    val category : String,
    val comments : List<CommentItem>
) : Serializable {
    constructor(_id : String, title : String, writerNickname : String, writerId : String, updateTime : String, description : String, likesCnt : Int, likeIDs: List<String>, category : String, comments : List<CommentItem>) : this (
            _id, title, writerNickname, writerId, updateTime, description, null, null, null, likesCnt, likeIDs, category, comments
    )

    constructor(_id : String, title : String, writerNickname : String, writerId : String, updateTime : String, description : String, url : String, likesCnt : Int, likeIDs: List<String>, category : String, comments : List<CommentItem>) : this (
        _id, title, writerNickname, writerId, updateTime, description, null, null, null, likesCnt, likeIDs, category, comments
    ) {
        if(url.contains(".mp4"))
            this.videoUrl = url
        else
            this.mp3Url = url
    }

    constructor(_id : String, title : String, writerNickname : String, writerId : String, updateTime : String, description : String, imageUrl: List<String>, likesCnt : Int, likeIDs: List<String>, category : String, comments : List<CommentItem>) : this (
        _id, title, writerNickname, writerId, updateTime, description, null, null, imageUrl, likesCnt, likeIDs, category, comments
    )
}

enum class ContentType(type : Int) {
    LOADING(-1), NO(0), VIDEO(1), IMAGE_1(2), IMAGE_2(3), IMAGE_3(4), IMAGE_4(5), IMAGE_5(6), IMAGE_6(7)
}

data class CommentItem(
    @Json(name = "writer_id")
    val writerId : String,
    @Json(name = "writer_nickname")
    val writerNickname : String,
    val date : String,
    val comment : String
)

data class likePerson(
        val nickname : String,
)