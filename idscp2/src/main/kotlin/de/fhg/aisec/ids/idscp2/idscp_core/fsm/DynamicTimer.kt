package de.fhg.aisec.ids.idscp2.idscp_core.fsm

import java.util.concurrent.locks.ReentrantLock

/**
 * A DynamicTimer class that provides an API to the FSM to start and cancel timeout threads
 * without a fixed timeout delay (in ms)
 * The timer ensures that no canceled timer is able to trigger a timeout transitions
 *
 * @author Leon Beckmann (leon.beckmann@aisec.fraunhofer.de)
 */
class DynamicTimer internal constructor(private val fsmIsBusy: ReentrantLock, private val timeoutHandler: Runnable) {
    private var thread: TimerThread? = null
    private val mutex = ReentrantLock(true)
    fun resetTimeout(delay: Long) {
        cancelTimeout()
        start(delay)
    }

    /*
     * Start a timer thread that triggers the timeout handler routine after a given timout delay
     */
    fun start(delay: Long) {
        mutex.lock()
        thread = TimerThread(delay, timeoutHandler, fsmIsBusy).also { it.start() }
        mutex.unlock()
    }

    /*
     * Cancel the current timer thread
     */
    fun cancelTimeout() {
        mutex.lock()
        thread?.safeStop()
        thread = null
        mutex.unlock()
    }
}