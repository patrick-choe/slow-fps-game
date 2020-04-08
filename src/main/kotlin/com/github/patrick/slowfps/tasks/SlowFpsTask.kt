package com.github.patrick.slowfps.tasks

interface SlowFpsTask {
    /**
     * This 'execute' method works like a 'run'
     * method in 'Runnable'
     */
    fun execute(): SlowFpsTask?
}