package com.github.patrick.slowfps.tasks

import com.github.noonmaru.tap.packet.Packet.TITLE

class SlowFpsTitleTask : SlowFpsTask {
    private var ticks: Int = -1

    /**
     * This 'execute' method works like a 'run'
     * method in 'Runnable'
     */
    override fun execute(): SlowFpsTask {
        ticks++
        if (ticks == 0) TITLE.compound("§4S§6L§2O§bW §1F§9P§dS§5!", null, 5, 50, 5).sendAll()
        return if (ticks >= 60) SlowFpsCountDownTask() else this
    }
}
