package me.bald.smackstats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class SmackStats extends JavaPlugin implements Listener {

    FileConfiguration config = getConfig();
    String prefix = ChatColor.WHITE+"["+ChatColor.AQUA+"SmackStats"+ChatColor.WHITE+"] ";

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin Initiated");
        getServer().getPluginManager().registerEvents(this, this);
        loadConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin Shutdown");
    }

    private void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // The /smackstats command, Will show users smacks
        if(command.getName().equalsIgnoreCase("smackstats")){

            // If command sender is player, get stats & send to user
            if(sender instanceof Player){
                Player player = (Player) sender;
                if(args.length > 0) {
                    Player player1 = Bukkit.getPlayerExact(args[0]);
                    if(player1 != null) {
                        player.sendMessage(prefix+player1.getDisplayName()+" has been smacked "+getSmacked(player1.getUniqueId())+" times.");
                        player.sendMessage(prefix+player1.getDisplayName()+" has smacked "+getSmacks(player1.getUniqueId())+" others.");
                    }else{
                        player.sendMessage(prefix+ChatColor.RED+"Player not found");
                    }
                }else{
                    player.sendMessage(prefix+player.getDisplayName()+" has been smacked "+getSmacked(player.getUniqueId())+" times.");
                    player.sendMessage(prefix+player.getDisplayName()+" has smacked "+getSmacks(player.getUniqueId())+" others.");
                }
            }
        }
        return false;
    }

    public int getSmacks(UUID playerUUID){
        return Integer.parseInt(config.getString(playerUUID+".smacks"));
    }

    public int getSmacked(UUID playerUUID){
        return Integer.parseInt(config.getString(playerUUID+".smacked"));
    }

    public void saveSmacked(UUID playerUUID){
        config.set(playerUUID.toString()+".smacked", getSmacked(playerUUID)+1);
        saveConfig();
    }

    public void saveSmacks(UUID playerUUID){
        config.set(playerUUID.toString()+".smacks", getSmacks(playerUUID)+1);
        saveConfig();
    }


    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        config.addDefault(playerUUID.toString()+".smacks", 0);
        config.addDefault(playerUUID.toString()+".smacked", 0);
        saveConfig();
        player.sendMessage(prefix+"Your Smacks: "+getSmacks(playerUUID));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player whoWasHit = (Player) event.getEntity();
            Player whoHit = (Player) event.getDamager();
            saveSmacks(whoHit.getUniqueId());
            saveSmacked(whoWasHit.getUniqueId());
            whoWasHit.sendMessage(prefix+"You were smacked by "+whoHit.getDisplayName()+"!");
            whoHit.sendMessage(prefix+"You smacked "+whoWasHit.getDisplayName()+"!");
        }
    }
}
