package com.tck.screen.capture

import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.view.Surface

/**
 *
 * description:
https://github.com/wossoneri/ScreenCapture
https://www.dazhuanlan.com/2020/01/31/5e33a1d4c1391/
 * @date 2020/12/9 22:51

 * @author tck88
 *
 * @version v1.0.0
 *
 */
class VideoCodec(private val url: String, private val mediaProjection: MediaProjection) : Runnable {


    private var muxer: MediaMuxer? = null
    private var mediaCodec: MediaCodec? = null

    fun prepareMediaCodec() {
        val mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, 480, 640)
        // 码率、帧率、关键帧间隔
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 400_000)
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2) //2秒
        //编码数据源的格式
        mediaFormat.setInteger(
            MediaFormat.KEY_COLOR_FORMAT,
            MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
        )
        try {
            val tempMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
            tempMediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            mediaCodec = tempMediaCodec
        } catch (e: Exception) {
            MyLog.d("VideoCodec:prepareMediaCodec error:${e.message}")
        }
    }

    override fun run() {
        try {
            prepareMediaCodec()

            val tempMediaCodec = mediaCodec

            if (tempMediaCodec == null) {
                return
            }
            muxer = MediaMuxer(url, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

            val inputSurface: Surface = tempMediaCodec.createInputSurface()

            val createVirtualDisplay = mediaProjection.createVirtualDisplay(
                "abc",
                480,
                640,
                1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                inputSurface,
                VirtualDisplayCallback(),
                null
            )

            tempMediaCodec.start()


        } catch (e: Exception) {
            MyLog.d("VideoCodec run error:${e.message}")
        }
    }

}


class VirtualDisplayCallback : VirtualDisplay.Callback() {
    override fun onPaused() {
        super.onPaused()
        MyLog.d("VirtualDisplayCallback--->onPaused")
    }

    override fun onResumed() {
        super.onResumed()
        MyLog.d("VirtualDisplayCallback--->onResumed")
    }

    override fun onStopped() {
        super.onStopped()
        MyLog.d("VirtualDisplayCallback--->onStopped")
    }
}