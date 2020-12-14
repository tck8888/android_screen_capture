package com.tck.screen.capture

import android.app.Activity
import android.content.Context
import android.media.projection.MediaProjectionManager

/**
 *
 * description:

 * @date 2020/12/14 23:10

 * @author tck88
 *
 * @version v1.0.0
 *
 */
class MediaProjectionControl private constructor() {

    private var mediaProjectionManager: MediaProjectionManager? = null

    companion object {
        const val REQUEST_CODE = 100

        val instance: MediaProjectionControl by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { MediaProjectionControl() }
    }

    fun startService(activity: Activity) {
        if (mediaProjectionManager != null) {
            return
        }
        mediaProjectionManager =
            activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

        val tempMediaProjectionManager = mediaProjectionManager

        if (tempMediaProjectionManager != null) {
            activity.startActivityForResult(tempMediaProjectionManager.createScreenCaptureIntent(),REQUEST_CODE)
        }
    }
}