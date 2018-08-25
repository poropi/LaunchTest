package jp.ne.poropi.launchtest.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import jp.ne.poropi.launchtest.R


class UIOverlayService : Service() {

    companion object {
        const val ACTION_OVERLAY_DISPLAY = "jp.ne.poropi.launchtest.service.action.ACTION_OVERLAY_DISPLAY"
    }


    var view: View? = null
    var wm: WindowManager? = null

    var overlayTextView: TextView? = null

    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            overlayTextView?.text = execCommand()
        }

    }

    override fun onCreate() {
        super.onCreate()

        initOverlay()

        registerReceiver(receiver, IntentFilter(ACTION_OVERLAY_DISPLAY))

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val channelId = "service"
        val title = "UIOverlayService"

        // 通知設定
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
            Notification.Builder(applicationContext, channelId)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText("service start")
                    .build()
        } else {
            Notification.Builder(applicationContext)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText("service start")
                    .build()
        }

        // フォアグラウンドで実行
        startForeground(1, notification)

        overlayTextView?.text = execCommand()
        view?.visibility = View.VISIBLE

        return START_STICKY
    }

    override fun onDestroy() {
        removeOverlay()
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    fun execCommand(): String {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val output = StringBuilder()

        activityManager.getRunningTasks(10).forEach {
            output.append("id:${it.id} ${it.description}\n")
        }

        return output.toString()
    }

    private fun initOverlay() {

        // Viewからインフレータを作成する
        val layoutInflater = LayoutInflater.from(this)
        // レイアウトファイルから重ね合わせするViewを作成する
        view = layoutInflater.inflate(R.layout.service_overlay, null)
        view?.visibility = View.GONE

        overlayTextView = view?.findViewById(R.id.overlayTextiew)
        // 重ね合わせするViewの設定を行う
        val params = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH),
                    PixelFormat.TRANSLUCENT)
        } else {
            WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH),
                    PixelFormat.TRANSLUCENT)
        }
        params.gravity = (Gravity.TOP or Gravity.RIGHT)

        // WindowManagerを取得する
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // Viewを画面上に重ね合わせする
        wm?.addView(view, params)

    }

    private fun removeOverlay() {
        wm?.removeView(view)
    }
}
