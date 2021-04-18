package kr.butterknife.talenthouse

data class RVItem(
    val title : String,
    val writer : String,
    val date : String,
    val subject : String,
    val mp3Url : String? = null,
    val mp4Url : String? = null,
    val imageUrl : String? = null
) {
    constructor(title: String, writer: String, date: String, subject: String) : this(
        title, writer, date, subject, null, null, null
    )
}

enum class ContentType(type : Int) {
    NO(0), IMAGE(1), MP3(2), MP4(3)
}