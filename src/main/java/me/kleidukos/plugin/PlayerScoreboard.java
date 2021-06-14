package me.kleidukos.plugin;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerScoreboard {

    private final LuckPerms luckPerms;

    private final Scoreboard board;
    private Objective tablist;

    private final Map<String, String> playersPrefix;

    public PlayerScoreboard(LuckPerms luckPerms){
        this.luckPerms = luckPerms;
        this.playersPrefix = new HashMap<>();

        board = Bukkit.getScoreboardManager().getNewScoreboard();

        defaultScoreboard();
    }

    public void defaultScoreboard(){
        tablist = null;
        if(board.getObjective("TabMatrix") != null){
            tablist = board.getObjective("TabMatrix");
        }else {
            tablist = board.registerNewObjective("TabMatrix", "QubikStudios");
        }

        tablist.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public String loadPlayerTabListPrefix(Player player){
        String prefix = luckPerms.getPlayerAdapter(Player.class).getMetaData(player).getPrefix();
        if(prefix == null){
            return "";
        }

        return prefix;
    }

    public void addPlayerToTabList(Player player){
        String prefix = loadPlayerTabListPrefix(player);

        if(prefix.equalsIgnoreCase("")){
            return;
        }

        Team tempTeam = null;
        for (Team team : tablist.getScoreboard().getTeams()){
            if(team.getName().equalsIgnoreCase(prefix)){
                tempTeam = team;
            }
        }

        if(tempTeam == null){
            tempTeam = board.registerNewTeam(prefix);
        }

        tempTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix + "&f"));
        tempTeam.addEntry(player.getDisplayName());

        getPlayersPrefix().put(player.getDisplayName(), prefix);

        updatePlayersTabList(Bukkit.getOnlinePlayers());
    }

    public void removePlayerFromTabList(Player player){
        for (Team team : board.getTeams()){
            if(team.getEntries().contains(player.getDisplayName())){
                team.removeEntry(player.getDisplayName());
                getPlayersPrefix().remove(player.getDisplayName());
            }
        }
    }

    public void updatePlayersTabList(Collection<? extends Player> players){
        players.forEach(player -> player.setScoreboard(board));
    }

    public String getPrefixByPlayer(Player player){
        return getPlayersPrefix().get(player.getDisplayName());
    }

    public Map<String, String> getPlayersPrefix() {
        return playersPrefix;
    }
}
