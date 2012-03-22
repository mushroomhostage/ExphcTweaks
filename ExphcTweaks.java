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

    public void fixRealisticChat_Crafting() {
        final Enchantment EFFICIENCY = Enchantment.DIG_SPEED;

        ItemStack earTrumpetWoodItem = new ItemStack(Material.GOLD_HELMET, 1);
        ItemStack earTrumpetLeatherItem = new ItemStack(Material.GOLD_HELMET, 1);
        ItemStack earTrumpetIronItem = new ItemStack(Material.GOLD_HELMET, 1);

        earTrumpetWoodItem.addUnsafeEnchantment(EFFICIENCY, 1);
        earTrumpetLeatherItem.addUnsafeEnchantment(EFFICIENCY, 2);
        earTrumpetIronItem.addUnsafeEnchantment(EFFICIENCY, 3);

        net.minecraft.server.CraftingManager.getInstance().registerShapedRecipe(CraftItemStack.createNMSItemStack(earTrumpetWoodItem),
            new Object[] { 
                "WWW",
                "WDW",
                Character.valueOf('W'), net.minecraft.server.Block.WOOD,
                Character.valueOf('D'), net.minecraft.server.Item.DIAMOND});

    /* TODO: replace */
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
    }

    public void addRecipe602(ShapedRecipe recipe) {

        /* TODO: update for SHAPED recipes

        // Workaround for 1.1-R4 https://bukkit.atlassian.net/browse/BUKKIT-602 Enchantments lost on crafting recipe output
        ArrayList<MaterialData> ingred = recipe.getIngredientList();
        Object[] data = new Object[ingred.size()];
        int i = 0;
        for (MaterialData mdata : ingred) {
            int id = mdata.getItemTypeId();
            byte dmg = mdata.getData();
            data[i] = new net.minecraft.server.ItemStack(id, 1, dmg);
            i++;
        }

        // Convert Bukkit ItemStack to net.minecraft.server.ItemStack
        int id = recipe.getResult().getTypeId();
        int amount = recipe.getResult().getAmount();
        short durability = recipe.getResult().getDurability();
        Map<Enchantment, Integer> enchantments = recipe.getResult().getEnchantments();
        net.minecraft.server.ItemStack result = new net.minecraft.server.ItemStack(id, amount, durability);
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            result.addEnchantment(CraftEnchantment.getRaw(entry.getKey()), entry.getValue().intValue());
        }

        CraftingManager.getInstance().registerShapedRecipe(result, data);
        */
    }

    public void fixPlasticCraft_MoFoods() {
        // PlasticCraft and Mo' Food and Crops recipe conflict for wooden plank -> wood flour / dish
        // Add clay block -> dish, instead of wood plank -> dish
        // https://github.com/mushroomhostage/exphc/issues/2
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
