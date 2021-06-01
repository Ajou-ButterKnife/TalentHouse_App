package kr.butterknife.talenthouse

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.butterknife.talenthouse.Util.registerFCMToken
import kr.butterknife.talenthouse.activity.MainActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FCMService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "onMessageReceived()")
//        if (remoteMessage.data.isNotEmpty()) {
//            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
//            sendNotification(
//                remoteMessage.data["title"] ?: "null",
//                remoteMessage.data["body"] ?: "null",
//            )
//        }

        if(LoginInfo.getNotiPermission(applicationContext)) {
            sendNotification(
                remoteMessage.data["title"] ?: "",
                remoteMessage.data["body"] ?: ""
            )
        }
        else {
            Log.d(TAG, "notification permission is denied")
        }
    }

    private fun sendNotification(messageTitle : String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        Log.d(TAG, messageLink)
//        intent.putExtra("url", messageLink)
//        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//            PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = "Talent House"
        val channelName = "알림 받기"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(kr.butterknife.talenthouse.R.drawable.ic_noti)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
//            .setFullScreenIntent(pendingIntent, true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, token)
        if(LoginInfo.getLoginInfo(applicationContext)[0] != "")
            registerFCMToken(applicationContext, token)
    }

}