package me.sagiri.minecraft.bukkit.syncQQBot

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChatEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent

class McChatEvent(bot: Bot, groupId : Long) : Listener {
    val bot = bot
    val groupId = groupId


    @EventHandler
    fun onPlayerLoginEvent(event : PlayerLoginEvent) {
        GlobalScope.launch {
            val master = bot?.getGroup(groupId)
            master?.sendMessage("玩家: ${event.player.name} 登陆游戏")
        }
    }

    @EventHandler
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        GlobalScope.launch {
            val master = bot?.getGroup(groupId)
            master?.sendMessage("玩家: ${event.player.name} 退出游戏")
        }
    }

    @EventHandler
    fun onChatEvent(event: PlayerChatEvent) {
        GlobalScope.launch {
            val master = bot?.getGroup(groupId)
            master?.sendMessage("玩家: ${event.player.name} 说: ${event.message}")
        }
    }
}