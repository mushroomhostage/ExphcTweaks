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

public class BugTest1 extends JavaPlugin {
    Logger log = Logger.getLogger("Minecraft");

    public void onEnable() {
    /*
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);

        log.info("enchantments = " + item.getEnchantments());

        ItemStack item2 = item.clone();

        log.info("enchantments = " + item2.getEnchantments());
        */

        /*
        ShapelessRecipe recipe = new ShapelessRecipe(item);
        recipe.addIngredient(2, Material.DIRT);
        Bukkit.getServer().addRecipe(recipe);
        */

        log.info("BugTest1 enabled");
    }

    public void onDisable() {
        log.info("BugTest1 disabled");
    }
}
