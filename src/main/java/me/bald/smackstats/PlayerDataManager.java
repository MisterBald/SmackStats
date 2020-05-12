package me.bald.smackstats;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerDataManager {

    private static File file;
    private static FileConfiguration customFile;

    public static void setup(){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("SmackStats").getDataFolder(), "playerdata/playerdata.yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException e){
                //a thing
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return customFile;
    }

    public static void save(){
        try {
            customFile.save(file);
        }catch (IOException e){
            //couldnt save
        }
    }

    public static void reload(){
       customFile = YamlConfiguration.loadConfiguration(file);
    }
}
