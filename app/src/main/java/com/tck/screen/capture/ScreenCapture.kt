package com.tck.screen.capture

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.widget.Toast

/**
 *
 * description:

 * @date 2020/12/9 22:39

 * @author tck88
 *
 * @version v1.0.0
 *
 */
class ScreenCapture(val url: String, val activity: Activity) : Runnable {

    private lateinit var mediaProjectionManager: MediaProjectionManager
    private var mediaProjection: MediaProjection? = null

    fun startScreenCapture() {
        mediaProjectionManager =
            activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val createScreenCaptureIntent = mediaProjectionManager.createScreenCaptureIntent()
        activity.startActivityForResult(createScreenCaptureIntent, 100)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data)
                if (mediaProjection != null) {
                    //TODO:开启线程，开始执行
                } else {
                    Toast.makeText(activity, "data=null", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "data=null", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun run() {
        val tempMediaProjection = mediaProjection ?: return
        val videoCodec = VideoCodec(tempMediaProjection)
        videoCodec.startScreenCapture()
    }


}