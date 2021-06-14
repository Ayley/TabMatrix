package me.kleidukos.plugin;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class TabMatrix extends JavaPlugin {

    private LuckPerms luckPerms;
    private PlayerScoreboard scoreboard;

    @Override
    public void onEnable() {
        if(getServer().getPluginManager().getPlugin("LuckPerms") == null){
            getLogger().log(Level.WARNING, "/////////////////////////////////////////////");
            getLogger().log(Level.WARNING, "Please install LuckPerms.");
            getLogger().log(Level.WARNING, "/////////////////////////////////////////////");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if(provider != null){
            luckPerms = provider.getProvider();
        }

        scoreboard = new PlayerScoreboard(luckPerms);

        Bukkit.getPluginManager().registerEvents(new JoinQuit(scoreboard), this);
        Bukkit.getPluginManager().registerEvents(new MessageReceived(scoreboard), this);
    }

    @Override
    public void onDisable() {
    }
}
