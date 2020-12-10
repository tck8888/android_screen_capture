package com.tck.screen.capture

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.IBinder


/**
 *<p>description:</p>
 *<p>created on: 2020/12/10 13:02</p>
 * @author tck
 * @version v3.7.5
 *
 */
class ScreenCaptureService : Service() {

    private lateinit var mediaProjectionManager: MediaProjectionManager
    private var mediaProjection: MediaProjection? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val resultData = intent?.getParcelableExtra<Intent>("data")
        val resultCode = intent?.getIntExtra("resultCode", Activity.RESULT_OK) ?: Activity.RESULT_OK
        if (resultData != null) {
            mediaProjectionManager =
                getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, resultData)
            val videoCodec = VideoCodec("", mediaProjection!!)
            ScreenCaptureExecutors.instance.execute(videoCodec)
        }
        return super.onStartCommand(intent, flags, startId)
    }
}