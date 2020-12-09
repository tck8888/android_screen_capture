package com.tck.screen.capture

import android.util.Log

/**
 *
 * description:

 * @date 2020/12/9 23:14

 * @author tck88
 *
 * @version v1.0.0
 *
 */
object MyLog {

    const val TAG = "screen_capture"

    fun d(msg: String) {
        Log.d(TAG, msg)
    }
}