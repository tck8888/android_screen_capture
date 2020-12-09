package com.tck.screen.capture

import android.hardware.display.DisplayManager
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.view.Surface

/**
 *
 * description:

 * @date 2020/12/9 22:51

 * @author tck88
 *
 * @version v1.0.0
 *
 */
class VideoCodec(val mediaProjection: MediaProjection) : Thread() {

    fun startScreenCapture() {
        val mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, 480, 640)
        // 码率、帧率、关键帧间隔
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 400_000);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2); //2秒
        //编码数据源的格式
        mediaFormat.setInteger(
            MediaFormat.KEY_COLOR_FORMAT,
            MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
        )

        try {
            val mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
            mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            val inputSurface: Surface = mediaCodec.createInputSurface()
            mediaProjection.createVirtualDisplay(
                "abc", 480, 640, 1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                inputSurface, null, null);
        } catch (e: Exception) {

        }
    }

    override fun run() {
        super.run()

    }
}