package me.kleidukos.plugin;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class PlayerScoreboard {

    private final LuckPerms luckPerms;

    private final Scoreboard board;
    private Objective tablist;

    private final Map<String, String> playersPrefix;
    private final List<Group> groups;

    public PlayerScoreboard(LuckPerms luckPerms){
        this.luckPerms = luckPerms;
        groups = new ArrayList<>();
        this.playersPrefix = new HashMap<>();

        luckPerms.getGroupManager().loadAllGroups().thenAccept(v -> groups.addAll(luckPerms.getGroupManager().getLoadedGroups()));

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

        groups.sort(new Comparator<Group>() {
            @Override
            public int compare(Group o1, Group o2) {
                if(o1.getWeight().getAsInt() < o2.getWeight().getAsInt()){
                    return 1;
                }else {
                    return -1;
                }
            }
        });

        for (int i = 0; i < groups.size(); i++){
            board.registerNewTeam(i + groups.get(i).getName());
        }

        tablist.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void addPlayerToTabList(Player player){
        User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
        Group group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup());

        Team tempTeam = null;
        for (Team team : tablist.getScoreboard().getTeams()){
            if(team.getName().contains(group.getName())){
                tempTeam = team;
            }
        }

        if(tempTeam == null){
            tempTeam = board.registerNewTeam(group.getName());
        }
        String prefix = group.getCachedData().getMetaData().getPrefix();

        tempTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix+ "&f"));
        tempTeam.addPlayer(player);

        player.setDisplayName(prefix + player.getDisplayName());

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
