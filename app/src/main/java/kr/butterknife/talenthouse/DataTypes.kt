package kr.butterknife.talenthouse

import com.squareup.moshi.Json
import java.util.*

data class PostItem(
    val title : String,
    @Json(name = "writer_nickname")
    val writerNickname : String,
    @Json(name = "writer_id")
    val writerId : String,
    @Json(name = "update_time")
    val updateTime : Date,
    val description : String,
    var mp3Url : String? = null,
    var videoUrl : String? = null,
    val imageUrl : List<String>? = null,
    @Json(name = "likes_cnt")
    val likesCnt : Int,
    val category : String,
    val comments : List<CommentItem>
) {
    constructor(title : String, writerNickname : String, writerId : String, updateTime : Date, description : String, likesCnt : Int, category : String, comments : List<CommentItem>) : this (
        title, writerNickname, writerId, updateTime, description, null, null, null, likesCnt, category, comments
    )

    constructor(title : String, writerNickname : String, writerId : String, updateTime : Date, description : String, url : String, likesCnt : Int, category : String, comments : List<CommentItem>) : this (
        title, writerNickname, writerId, updateTime, description, null, null, null, likesCnt, category, comments
    ) {
        if(url.contains(".mp4"))
            this.videoUrl = url
        else
            this.mp3Url = url
    }

    constructor(title : String, writerNickname : String, writerId : String, updateTime : Date, description : String, imageUrl: List<String>, likesCnt : Int, category : String, comments : List<CommentItem>) : this (
        title, writerNickname, writerId, updateTime, description, null, null, imageUrl, likesCnt, category, comments
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