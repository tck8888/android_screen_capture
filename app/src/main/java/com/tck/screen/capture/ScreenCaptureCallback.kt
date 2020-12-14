package com.tck.screen.capture

import android.graphics.Bitmap

/**
 *
 * description:

 * @date 2020/12/14 22:50

 * @author tck88
 *
 * @version v1.0.0
 *
 */
interface ScreenCaptureCallback {

    fun onSuccess(bitmap: Bitmap)

    fun onFail(msg:String)
}