package me.exphc.BugTest1;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Formatter;
import java.lang.Byte;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.io.*;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.Material.*;
import org.bukkit.material.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.command.*;
import org.bukkit.inventory.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.*;
import org.bukkit.scheduler.*;
import org.bukkit.enchantments.*;
import org.bukkit.*;

class BugTest1Listener implements Listener {
    Logger log = Logger.getLogger("Minecraft");

    BugTest1 plugin;

    public BugTest1Listener(BugTest1 pl) {
        plugin = pl;

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(final BlockPlaceEvent event) {
        Player player = event.getPlayer();

        log.info("event.getItemInHand() = " + event.getItemInHand().getEnchantments());
        log.info("player.getItemInHand() = " + player.getItemInHand().getEnchantments());

        // BUG: event.getItemInHand() loses enchantments! (tested on craftbukkit-1.1-R1-20120121.235721-81.jar) Cannot use it
        //ItemStack item = event.getItemInHand();
    }
}

public class BugTest1 extends JavaPlugin {
    Logger log = Logger.getLogger("Minecraft");
    BugTest1Listener listener;

    public void onEnable() {
        listener = new BugTest1Listener(this);

        log.info("BugTest1 enabled");
    }

    public void onDisable() {
        log.info("BugTest1 disabled");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("bugtest1")) {
            return false;
        }

        Player player;

        if (sender instanceof Player) {
            player = (Player)sender;
        } else {
            // Get player name from first argument
            player = Bukkit.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage("no such player");
                return false;
            }
        }


    
        ItemStack item = new ItemStack(Material.DIRT, 1);
        item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 42);

        player.setItemInHand(item);

        return true;
    }
}
