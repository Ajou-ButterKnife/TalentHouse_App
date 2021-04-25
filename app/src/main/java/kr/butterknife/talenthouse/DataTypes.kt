package kr.butterknife.talenthouse

import com.squareup.moshi.Json

data class PostItem(
    val title : String,
    @Json(name = "writer_nickname")
    val writerNickname : String,
    @Json(name = "writer_id")
    val writerId : String,
    @Json(name = "update_time")
    val updateTime : String,
    val description : String,
    var mp3Url : String? = null,
    var videoUrl : String? = null,
    val imageUrl : List<String>? = null
) {
    constructor(title : String, writerNickname : String, writerId : String, updateTime : String, description : String) : this (
        title, writerNickname, writerId, updateTime, description, null, null, null
    )

    constructor(title : String, writerNickname : String, writerId : String, updateTime : String, description : String, url : String) : this (
        title, writerNickname, writerId, updateTime, description, null, null, null
    ) {
        if(url.contains(".mp4"))
            this.videoUrl = url
        else
            this.mp3Url = url
    }

    constructor(title : String, writerNickname : String, writerId : String, updateTime : String, description : String, imageUrl: List<String>) : this (
        title, writerNickname, writerId, updateTime, description, null, null, imageUrl
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