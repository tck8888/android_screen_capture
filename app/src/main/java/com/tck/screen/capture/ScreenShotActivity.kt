package com.tck.screen.capture

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.display.DisplayManager
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.tck.screen.capture.databinding.ActivityScreenShotBinding

class ScreenShotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScreenShotBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenShotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartScreenShot.setOnClickListener {
            startScreenShot()
        }
    }


    private fun startScreenShot() {
        val mediaProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        MyLog.d("requestCode:${requestCode},resultCode:${resultCode},data:${data?.toString()}")
        if (requestCode == 100) {
            if (data != null) {
                val service = Intent(this, ScreenShotService::class.java)
                service.putExtra("data", data)
                service.putExtra("resultCode", resultCode)
                startForegroundService(service)
            }
        }
    }

}