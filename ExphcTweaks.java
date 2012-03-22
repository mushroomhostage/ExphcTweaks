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


    public void onEnable() {
        fixPlasticCraft_MoFoods();
        fixRealisticChat_Crafting();

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    // https://github.com/mushroomhostage/exphc/issues/17
    public void fixRealisticChat_Crafting() {
        final net.minecraft.server.Enchantment EFFICIENCY = net.minecraft.server.Enchantment.DIG_SPEED;

        net.minecraft.server.ItemStack earTrumpetWoodItem = new net.minecraft.server.ItemStack(Material.GOLD_HELMET.getId(), 1, 0);
        net.minecraft.server.ItemStack earTrumpetLeatherItem = new net.minecraft.server.ItemStack(Material.GOLD_HELMET.getId(), 1, 0);
        net.minecraft.server.ItemStack earTrumpetIronItem = new net.minecraft.server.ItemStack(Material.GOLD_HELMET.getId(), 1, 0);

        earTrumpetWoodItem.addEnchantment(EFFICIENCY, 1);
        earTrumpetLeatherItem.addEnchantment(EFFICIENCY, 2);
        earTrumpetIronItem.addEnchantment(EFFICIENCY, 3);

        // TODO: why still loses enchants?
        net.minecraft.server.CraftingManager.getInstance().registerShapedRecipe(earTrumpetWoodItem,
            new Object[] { 
                "WWW",
                "WDW",
                Character.valueOf('W'), net.minecraft.server.Block.WOOD,
                Character.valueOf('D'), net.minecraft.server.Item.DIAMOND});

    /* TODO: replace 
        ShapedRecipe earTrumpetWood = new ShapedRecipe(earTrumpetWoodItem);
        ShapedRecipe earTrumpetLeather = new ShapedRecipe(earTrumpetLeatherItem);
        ShapedRecipe earTrumpetIron = new ShapedRecipe(earTrumpetIronItem);

         earTrumpetWood.shape(
            "WWW",
            "WDW");
        earTrumpetWood.setIngredient('W', Material.WOOD);   // planks
        earTrumpetWood.setIngredient('D', Material.DIAMOND);
        addRecipe602(earTrumpetWood);

        earTrumpetLeather.shape(
            "LLL",
            "LDL");
        earTrumpetLeather.setIngredient('L', Material.LEATHER);
        earTrumpetLeather.setIngredient('D', Material.DIAMOND);
        addRecipe602(earTrumpetLeather);

        earTrumpetIron.shape(
            "III",
            "IDI");
        earTrumpetIron.setIngredient('I', Material.IRON_INGOT);
        earTrumpetIron.setIngredient('D', Material.DIAMOND);
        addRecipe602(earTrumpetIron);
        */
    }

    // PlasticCraft and Mo' Food and Crops recipe conflict for wooden plank -> wood flour / dish
    // Add clay block -> dish, instead of wood plank -> dish
    // https://github.com/mushroomhostage/exphc/issues/2
    public void fixPlasticCraft_MoFoods() {
        final int ID_DISH = 3704;           // Mo' Food and Crops
        ShapelessRecipe dishRecipe = new ShapelessRecipe(new ItemStack(ID_DISH, 2));
        dishRecipe.addIngredient(Material.CLAY);
        Bukkit.addRecipe(dishRecipe);

        /* Adding a new recipe here doesn't override - instead we have to remove using RecipeRepo
        final int ID_WOOD_FLOUR = 1006;     // PlasticCraft
        ShapelessRecipe flourRecipe = new ShapelessRecipe(new ItemStack(ID_WOOD_FLOUR, 1));
        flourRecipe.addIngredient(Material.WOOD); //plank
        Bukkit.addRecipe(flourRecipe);
        */
    }

    public void onDisable() {
    }

    @EventHandler(priority = EventPriority.NORMAL) 
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        // Temporary fix for quantity=0 items (infinite) picked up in fallout shelters.
        // If players switch to/from these "infinite item" stacks, replace them with one stack
        // https://github.com/mushroomhostage/exphc/issues/12
        ItemStack newStack = player.getInventory().getContents()[event.getNewSlot()];
        if (newStack != null && newStack.getType() != Material.AIR && newStack.getAmount() <= 0) {
            newStack.setAmount(1);
            player.getInventory().setItem(event.getNewSlot(), newStack);
        }

        ItemStack oldStack = player.getInventory().getContents()[event.getPreviousSlot()];
        if (oldStack != null && oldStack.getType() != Material.AIR && oldStack.getAmount() <= 0) {
            oldStack.setAmount(1);
            player.getInventory().setItem(event.getPreviousSlot(), oldStack);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("chunk")) {
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("Must be sent by player");
            return true;
        }
        Player player = (Player)sender;

        // See Bananachunk http://forums.bukkit.org/threads/fix-mech-bananachunk-v4-6-stuck-in-a-lag-hole-request-a-chunk-resend-1060.19232/page-7
        World world = player.getWorld();
        Chunk chunk = world.getChunkAt(player.getLocation());
        world.refreshChunk(chunk.getX(), chunk.getZ());

        sender.sendMessage("Chunk resent");

        return true;
    }
}
