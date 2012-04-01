package me.exphc.ExphcTweaks;

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
import org.bukkit.event.entity.*;
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

import net.minecraft.server.CraftingManager;

import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.inventory.CraftItemStack;

public class ExphcTweaks extends JavaPlugin implements Listener {
    Logger log = Logger.getLogger("Minecraft");

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            ItemStack item = event.getItem();

            if (block != null && item != null && item.getTypeId() == 567) { //  AnimalBikes
                // https://github.com/mushroomhostage/exphc/issues/24 AnimalBikes should be one-time use
                Player player = event.getPlayer();

                log.info("Player "+player.getName()+" placing AnimalBike "+item.getDurability());

                // use up bike, one-time use not infinite!
                player.setItemInHand(null);

                // TODO: only use if actually PLACED bike not removed it!

                /*
                class EmptyHandTask implements Runnable {
                    Player player;

                    public EmptyHandTask(Player player) {
                        this.player = player;
                    }

                    public void run() {
                        //player.setItemInHand(null);
                    }
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(this, new EmptyHandTask(player));
                */
            }
        }
    }


    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Must be sent by player");
            return true;
        }
        Player player = (Player)sender;

        if (cmd.getName().equalsIgnoreCase("chunk")) {
            // See Bananachunk http://forums.bukkit.org/threads/fix-mech-bananachunk-v4-6-stuck-in-a-lag-hole-request-a-chunk-resend-1060.19232/page-7
            World world = player.getWorld();
            Chunk chunk = world.getChunkAt(player.getLocation());
            world.refreshChunk(chunk.getX(), chunk.getZ());

            sender.sendMessage("Chunk resent");

            return true;
        } else {
            return false;
        }
    }
}
