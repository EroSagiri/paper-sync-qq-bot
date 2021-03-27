package me.sagiri.minecraft.bukkit.syncQQBot

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotOnlineEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class PluginMain : JavaPlugin() {
    var bot : Bot? = null
    var botLauch : Job? = null

    override fun onLoad() {

    }

    override fun onEnable() {
//        config.load("qq.yml")
//        val qq = config.getLong("qq")
//        val password = config.getString("password")
        config.addDefault("qq", 0L)
        config.addDefault("password", "*")
        config.addDefault("groupId", 0L)
        config.options().copyDefaults(true)
        saveConfig()

        val qq = config.getLong("qq")
        var password = config.getString("password")
        val groupId = config.getLong("groupId")
        if(password == null) {
            password = "0"
            logger.warning("密码没有")
            onDisable()
            return
        }
        if(qq == 0L) {
            logger.warning("帐号没有")
            onDisable()
            return
        }
        if(groupId == 0L) {
            logger.warning("同步qq群没有")
            onDisable()
            return
        }

        val q = this
        botLauch = GlobalScope.launch {
            bot = BotFactory.newBot(qq, password).apply { login() }
            GlobalEventChannel.subscribeAlways<GroupMessageEvent> {
                if(subject.id == groupId) {
                    Bukkit.broadcastMessage("QQ群 ${sender.nick}: ${message.content}")
                }
            }
            bot!!.eventChannel.subscribeAlways<BotOnlineEvent> {
                q.logger.info("机器人在线")
            }
            Bukkit.getPluginManager().registerEvents(McChatEvent(bot!!, groupId), q)
        }
    }

    override fun onDisable() {
        bot?.close()
        botLauch?.cancel()
    }
}