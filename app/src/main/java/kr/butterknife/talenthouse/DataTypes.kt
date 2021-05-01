package kr.butterknife.talenthouse

import com.squareup.moshi.Json
import java.util.*

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
    val imageUrl : List<String>? = null,
    @Json(name = "like_cnt")
    val likeCnt : Int,
    val category : String,
    val comments : List<CommentItem>
) {
    constructor(_id : String, title : String, writerNickname : String, writerId : String, updateTime : String, description : String, likesCnt : Int, category : String, comments : List<CommentItem>) : this (
            _id, title, writerNickname, writerId, updateTime, description, null, null, null, likesCnt, category, comments
    )

    constructor(_id : String, title : String, writerNickname : String, writerId : String, updateTime : String, description : String, url : String, likesCnt : Int, category : String, comments : List<CommentItem>) : this (
        _id, title, writerNickname, writerId, updateTime, description, null, null, null, likesCnt, category, comments
    ) {
        if(url.contains(".mp4"))
            this.videoUrl = url
        else
            this.mp3Url = url
    }

    constructor(_id : String, title : String, writerNickname : String, writerId : String, updateTime : String, description : String, imageUrl: List<String>, likesCnt : Int, category : String, comments : List<CommentItem>) : this (
        _id, title, writerNickname, writerId, updateTime, description, null, null, imageUrl, likesCnt, category, comments
    )
}

enum class ContentType(type : Int) {
    NO(0), IMAGE(1), MP3(2), VIDEO(3)
}

data class CommentItem(
    @Json(name = "writer_id")
    val writerId : String,
    @Json(name = "writer_nickname")
    val writerNickname : String,
    val date : String,
    val comment : String,
)