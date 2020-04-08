/*
 * Copyright (C) 2020 PatrickKR
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact me on <mailpatrickkr@gmail.com>
 */

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