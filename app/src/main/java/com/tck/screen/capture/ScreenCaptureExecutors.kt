package com.tck.screen.capture

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 *<p>description:</p>
 *<p>created on: 2020/12/10 9:09</p>
 * @author tck
 * @version v3.7.5
 *
 */
class ScreenCaptureExecutors private constructor() {

    private val cpuCount = Runtime.getRuntime().availableProcessors()
    private val corePoolSize = Math.max(2, Math.min(cpuCount - 1, 4))
    private val maximumPoolSize = cpuCount * 2 + 1
    private val keepAliveTime = 30L
    private val workQueue = LinkedBlockingQueue<Runnable>(5)
    private val threadPoolExecutor = ThreadPoolExecutor(
        corePoolSize,
        maximumPoolSize,
        keepAliveTime,
        TimeUnit.SECONDS,
        workQueue
    ).apply {
        allowCoreThreadTimeOut(true)

    }

    companion object {
        val instance: ScreenCaptureExecutors by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ScreenCaptureExecutors() }
    }

    fun execute(runnable: Runnable) {
        threadPoolExecutor.execute(runnable)
    }
}