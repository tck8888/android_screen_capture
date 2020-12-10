package com.tck.screen.capture

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.tck.screen.capture.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var screenCapture: ScreenCapture


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        screenCapture =
            ScreenCapture("${cacheDir}${File.separator}${System.currentTimeMillis()}.mp4", this)

        binding.btnStartScreenCapture.setOnClickListener {
            screenCapture.startScreenCapture()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        screenCapture.onActivityResult(requestCode, resultCode, data)
    }


    private fun initScreenCapture() {
        val mediaProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as? MediaProjectionManager
        if (mediaProjectionManager == null) {
            return
        }
        val mediaProjection = mediaProjectionManager.getMediaProjection(100, intent)
        val metrics = DisplayMetrics()

//        mediaProjection.createVirtualDisplay(
//                "ScreenCapture",
//                binding.surfaceView.width,
//                binding.surfaceView.height,
//                display.getRealMetrics(metrics)
//        )

    }


    private fun startScreenCapture() {

    }
}