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

            // https://github.com/mushroomhostage/exphc/issues/24 AnimalBikes should be one-time use
            if (block != null && item != null && item.getTypeId() == 567) { //  AnimalBikes
                Player player = event.getPlayer();

                log.info("[ExphcTweaks] [AB] Player "+player.getName()+" using AnimalBike item "+item.getDurability());

                boolean nearExistingBike = false;
                List<Entity> entities = player.getNearbyEntities(10, 10, 10);
                for (Entity entity: entities) {
                    //log.info("near entity " + entity.getEntityId() + " of type " + entity.getType() + " = " + entity.getType().getTypeId());
                    // Bukkit's EntityType only gives us 'UNKNOWN' for modded entities, heh, thanks
                    if (entity.getType() == EntityType.UNKNOWN) {
                        nearExistingBike = true;
                        break;
                    }
                }

                // If we think the player is _placing_ a bike, use it up, so it isn't infinite.
                // This heuristic is not perfect and should really be changed in the mod itself (there may
                // be other bikes placed by other players nearby).
                if (!nearExistingBike) {
                    log.info("[ExphcTweaks] [AB] Player "+player.getName()+" apparently placed AnimalBike "+item.getDurability()+", clearing hand");
                    player.setItemInHand(null);
                } else {
                    log.info("[ExphcTweaks] [AB] Player "+player.getName()+" apparently did not place AnimalBike "+item.getDurability()+", not clearing hand");
                }
            }
        }
    }


    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        // Clear entities
        if (cmd.getName().equalsIgnoreCase("clear")) {
            if (sender instanceof Player && !((Player)sender).isOp()) {
                return false;
            }

            // TODO: option to clear ALL item entities! like ClearLagg

            // Clear item entities near player
            Player player;
            if (args.length < 1) {
                if (sender instanceof Player) {
                    player = (Player)sender;
                } else {
                    sender.sendMessage("Must specify player name when using /clear from console");
                    return false;
                }
            } else {
                player = Bukkit.getPlayer(args[0]);
            }
            double r = 200.0;
            List<Entity> entities = player.getNearbyEntities(r, r, r);
            int items = 0;
            for (Entity entity: entities) {
                if (entity instanceof Item) {
                    entity.remove();
                    items += 1;
                }
            }
            sender.sendMessage("Removed "+items+" items out of "+entities.size()+" entities");
            return true;
        }

        // Load
        if (cmd.getName().equalsIgnoreCase("loadchunk")) {
            if (sender instanceof Player && !((Player)sender).isOp()) {
                return false;
            }

            if (args.length < 3) {
                sender.sendMessage("Must specify world and x and z");
                return false;
            }
            World world = Bukkit.getWorld(args[0]);
            int x = Integer.parseInt(args[1]);
            int z = Integer.parseInt(args[2]);

            sender.sendMessage("Loading "+world.getName()+" x="+x+", z="+z);

            world.loadChunk(x, z);
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Must be sent by player");
            return true;
        }
        Player player = (Player)sender;

        // Resend chunk to player
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
