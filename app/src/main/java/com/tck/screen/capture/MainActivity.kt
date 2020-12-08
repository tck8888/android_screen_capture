package com.tck.screen.capture

import android.content.Context
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.tck.screen.capture.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnStartScreenCapture.setOnClickListener {


        }
    }


    private fun initScreenCapture() {
        val mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as? MediaProjectionManager
        if (mediaProjectionManager==null){
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


    private fun startScreenCapture(){

    }
}