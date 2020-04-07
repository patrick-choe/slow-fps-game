package com.github.patrick.slowfps.plugin

import com.github.patrick.slowfps.process.SlowFpsCommand
import com.github.patrick.slowfps.process.SlowFpsProcess.stopProcess
import org.bukkit.plugin.java.JavaPlugin

/**
 * This is the main class of this 'Slow FPS Plugin'.
 * It stores global variables, and initial process of this plugin.
 */
class SlowFpsPlugin : JavaPlugin() {
    /**
     * Following companion object stores project-wide variables
     */
    companion object {
        lateinit var instance: SlowFpsPlugin
    }

    /**
     * Following overridden method executes when the plugin is initializing.
     * It sets command executor and tab completer for '/slowfps' command.
     */
    override fun onEnable() {
        instance = this
        getCommand("slowfps").executor = SlowFpsCommand()
        getCommand("slowfps").tabCompleter = SlowFpsCommand()
    }

    /**
     * Following overridden method executes when the plugin is shut down.
     * It stops the ongoing game.
     */
    override fun onDisable() {
        stopProcess()
    }
}