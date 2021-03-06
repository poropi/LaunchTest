package jp.ne.poropi.launchtest.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import jp.ne.poropi.launchtest.R
import jp.ne.poropi.launchtest.extension.REQUEST_SYSTEM_OVERLAY
import jp.ne.poropi.launchtest.extension.checkOverlayPermission
import jp.ne.poropi.launchtest.extension.requestOverlayPermission
import jp.ne.poropi.launchtest.service.UIOverlayService
import kotlinx.android.synthetic.main.activity_launch_top.*
import timber.log.Timber


open class LaunchTopActivity: AppCompatActivity(), View.OnClickListener{

    private var serviceIntetn: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("get intent frags : 0x%08x".format(intent!!.flags))

        setContentView(R.layout.activity_launch_top)

        standerdButton.setOnClickListener(this)
        singleTopButton.setOnClickListener(this)
        singleinstanceButton.setOnClickListener(this)
        singletaskButton.setOnClickListener(this)

        standerdNextButton.setOnClickListener(this)
        singleTopNextButton.setOnClickListener(this)
        singleinstanceNextButton.setOnClickListener(this)
        singletaskNextButton.setOnClickListener(this)

        serviceIntetn = Intent(this, UIOverlayService::class.java)

        serviceStartButton.setOnClickListener{
            if (!checkOverlayPermission()) {
                requestOverlayPermission()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntetn)
                } else {
                    startService(serviceIntetn)
                }
            }
        }
        serviceEndButton.setOnClickListener{
            stopService(serviceIntetn)
        }

        serviceStartButton.visibility = View.GONE
        serviceEndButton.visibility = View.GONE

        Thread {
            val intent = Intent(UIOverlayService.ACTION_OVERLAY_DISPLAY)
            sendBroadcast(intent)
        }.start()

    }

    override fun onDestroy() {
        Thread {
            val intent = Intent(UIOverlayService.ACTION_OVERLAY_DISPLAY)
            sendBroadcast(intent)
        }.start()

        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_SYSTEM_OVERLAY ->
                if (checkOverlayPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(Intent(this, UIOverlayService::class.java))
                    } else {
                        startService(Intent(this, UIOverlayService::class.java))
                    }
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onClick(v: View?) {
        var intent: Intent? = null
        when (v) {
            standerdButton -> {
                intent = Intent(this, StandardTop::class.java)
            }
            singleTopButton -> {
                intent = Intent(this, SingletopTop::class.java)
            }
            singleinstanceButton -> {
                intent = Intent(this, SingleinstanceTop::class.java)
            }
            singletaskButton -> {
                intent = Intent(this, SingletaskTop::class.java)
            }
            standerdNextButton -> {
                intent = Intent(this, StandardNext::class.java)
            }
            singleTopNextButton -> {
                intent = Intent(this, SingletopNext::class.java)
            }
            singleinstanceNextButton -> {
                intent = Intent(this, SingleinstanceNext::class.java)
            }
            singletaskNextButton -> {
                intent = Intent(this, SingletaskNext::class.java)
            }
        }

        intent?.flags = 0

        if (switchClearTop.isChecked) {
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        if (switchExcludeFromRecents.isChecked) {
            intent?.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
        if (switchLaunchedFromHistory.isChecked) {
            intent?.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
        }
        if (switchNewTask.isChecked) {
            intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (switchNoAnimation.isChecked) {
            intent?.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        if (switchNoHistory.isChecked) {
            intent?.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        }
        if (switchPreviousIsTop.isChecked) {
            intent?.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
        }
        if (switchReorderToFront.isChecked) {
            intent?.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }
        if (switchSingleTop.isChecked) {
            intent?.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        Timber.d("set intent frags : 0x%08x".format(intent!!.flags))
        startActivity(intent)
    }
}

