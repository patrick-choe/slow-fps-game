package com.github.patrick.slowfps.tasks

import com.github.patrick.slowfps.process.SlowFpsGame.Companion.slowFpsProjectiles
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.slowFpsTeams
import com.github.patrick.slowfps.process.SlowFpsProcess.stopProcess

class SlowFpsScheduler : Runnable {
    private var rhythmTask: SlowFpsTask? = null

    init {
        rhythmTask = SlowFpsTitleTask()
    }

    /**
     * When an object implementing interface `Runnable` is used
     * to create a thread, starting the thread causes the object's
     * `run` method to be called in that separately executing
     * thread.
     *
     *
     * The general contract of the method `run` is that it may
     * take any action whatsoever.
     *
     * @see java.lang.Thread.run
     */
    override fun run() {
        slowFpsProjectiles.removeIf { it.dead }
        slowFpsTeams.removeIf { it.dead }
        rhythmTask = rhythmTask?.execute()
        if (rhythmTask == null) stopProcess()
    }
}