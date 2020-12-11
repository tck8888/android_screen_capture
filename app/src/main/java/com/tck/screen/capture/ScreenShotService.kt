package com.tck.screen.capture

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.display.DisplayManager
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import java.io.File
import java.io.FileOutputStream


/**
 * https://www.codenong.com/cs106644505/
 * https://www.jianshu.com/p/24fca916dcfd
 *<p>description:</p>
 *<p>created on: 2020/12/10 13:02</p>
 * @author tck
 * @version v3.7.5
 *
 */
class ScreenShotService : Service() {


    private lateinit var handler: Handler

    override fun onCreate() {
        super.onCreate()
        handler = Handler(Looper.getMainLooper())

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val resultData = intent?.getParcelableExtra<Intent>("data")
        val resultCode = intent?.getIntExtra("resultCode", Activity.RESULT_OK) ?: Activity.RESULT_OK
        if (resultData != null) {
            val mediaProjectionManager =
                getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            val mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, resultData)
            if (mediaProjection != null) {
                startScreenShot(mediaProjection)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startScreenShot(mediaProjection: MediaProjection) {
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        val imageReader = ImageReader.newInstance(
            screenWidth,
            screenHeight,
            android.graphics.PixelFormat.RGBA_8888,
            1
        )
        val createVirtualDisplay = mediaProjection.createVirtualDisplay(
            "abc",
            screenWidth,
            screenHeight,
            resources.displayMetrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader.surface,
            null,
            null
        )

        imageReader.setOnImageAvailableListener({
            val acquireLatestImage = imageReader.acquireLatestImage()
            val planes = acquireLatestImage.planes
            val buffer = planes[0].buffer
            try {
                val createBitmap =
                    Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
                createBitmap.copyPixelsFromBuffer(buffer)

                val file = File(cacheDir, "${System.currentTimeMillis()}.png")
                FileOutputStream(file).use {
                    createBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
            } catch (e: Exception) {
            }
            acquireLatestImage.close()
            mediaProjection.stop()
            createVirtualDisplay.release()

        }, null)


    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }
}