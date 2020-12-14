package com.tck.screen.capture

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.IBinder
import android.hardware.display.VirtualDisplay;
import android.media.Image


/**
 * https://www.codenong.com/cs106644505/
 * https://www.jianshu.com/p/24fca916dcfd
 * https://github.com/SMask/MediaProjectionLibrary_Android/blob/a2899245d9/MediaProjection/src/main/java/com/mask/mediaprojection/utils/MediaProjectionHelper.java
 *<p>description:</p>
 *<p>created on: 2020/12/10 13:02</p>
 * @author tck
 * @version v3.7.5
 *
 */
class ScreenShotService : Service() {

    private var imageReader: ImageReader? = null
    private var virtualDisplayImageReader: VirtualDisplay? = null
    private var mediaProjection: MediaProjection? = null
    private var isImageAvailable = false

    override fun onCreate() {
        super.onCreate()
        MyLog.d("ScreenShotService onCreate")
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
            val tempMediaProjection =
                mediaProjectionManager.getMediaProjection(resultCode, resultData)

        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun createVirtualDisplay(resultCode: Int, data: Intent?) {
        if (data == null) {
            stopSelf()
            return
        }
        val tempMediaProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as? MediaProjectionManager

        if (tempMediaProjectionManager == null) {
            stopSelf()
            return
        }

        val tempMediaProjection =
            tempMediaProjectionManager.getMediaProjection(resultCode, data)

        if (tempMediaProjection == null) {
            stopSelf()
            return
        }

        mediaProjection = tempMediaProjection

        showNotification()

        createImageReader()

    }

    private fun showNotification() {
        val pendingIntent: PendingIntent = Intent(this, ScreenShotActivity::class.java)
            .let { notificationIntent -> PendingIntent.getActivity(this, 0, notificationIntent, 0) }
        val notification = Notification.Builder(this, "notification_id")
            .setContentIntent(pendingIntent) // 设置PendingIntent
            .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
            .setContentText("is running......") // 设置上下文内容
            .setWhen(System.currentTimeMillis()) // 设置该通知发生的时间
            .build()

        startForeground(110, notification)
    }


    /**
     * 创建 屏幕截图
     */
    private fun createImageReader() {
        val tempMediaProjection = mediaProjection ?: return
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels

        val tempImageReader = ImageReader.newInstance(
            screenWidth,
            screenHeight,
            PixelFormat.RGBA_8888,
            2
        )
        virtualDisplayImageReader = tempMediaProjection.createVirtualDisplay(
            "ScreenCapture",
            screenWidth,
            screenHeight,
            resources.displayMetrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            tempImageReader.surface,
            null,
            null
        )
        tempImageReader.setOnImageAvailableListener({ isImageAvailable = true }, null)

        imageReader = tempImageReader
    }

    fun capture(callback: ScreenCaptureCallback?) {
        val tempImageReader = imageReader
        if (tempImageReader == null) {
            callback?.onFail("imageReader = null")
            return
        }
        if (!isImageAvailable) {
            callback?.onFail("setOnImageAvailableListener false")
            return
        }

        val acquireLatestImage = tempImageReader.acquireLatestImage()
        if (acquireLatestImage == null) {
            callback?.onFail("acquireLatestImage = null")
            return
        }
        val width = acquireLatestImage.width
        val height = acquireLatestImage.height

        // 重新计算Bitmap宽度，防止Bitmap显示错位
        val plane: Image.Plane = acquireLatestImage.planes[0]
        val pixelStride = plane.pixelStride
        val rowStride = plane.rowStride
        val rowPadding = rowStride - pixelStride * width
        val bitmapWidth = width + rowPadding / pixelStride

        val bitmap =
            Bitmap.createBitmap(bitmapWidth, height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(plane.buffer)

        acquireLatestImage.close()

        // 裁剪Bitmap，因为重新计算宽度原因，会导致Bitmap宽度偏大
        val result = Bitmap.createBitmap(bitmap, 0, 0, width, height)
        bitmap.recycle()

        isImageAvailable = false

        callback?.onSuccess(result)
    }


    override fun onDestroy() {
        destroy()
        super.onDestroy()
    }

    private fun destroy() {
        stopImageReader()

        mediaProjection?.stop()
        mediaProjection = null

        stopForeground(true)
    }

    private fun stopImageReader() {
        isImageAvailable = false
        imageReader?.close()
        imageReader = null
    }
}