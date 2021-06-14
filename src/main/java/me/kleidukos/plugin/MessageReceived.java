package me.kleidukos.plugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MessageReceived implements Listener {

    public final PlayerScoreboard scoreboard;

    public MessageReceived(PlayerScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    @EventHandler
    public void onMessageReceived(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        event.setFormat(ChatColor.translateAlternateColorCodes('&', scoreboard.getPrefixByPlayer(player) + "&f" + player.getDisplayName() + "| ") + event.getMessage());
    }

}
