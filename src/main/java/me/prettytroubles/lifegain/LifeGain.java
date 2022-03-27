package me.prettytroubles.lifegain;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class LifeGain extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }
    @EventHandler
    public void onEntiyDeath(EntityDeathEvent event)
    {
        //Get the entity from the event and assign to the "entity" variable
        LivingEntity entity = event.getEntity();
        Location location = entity.getLocation();
        if (!(entity instanceof Monster) && entity.getType() != EntityType.WOLF)
        {
            return;
        }

        //Randomly determine if we should spawn a health pickup

        //Choose a random number between 0-5, and if it is 1, continue
        if (ThreadLocalRandom.current().nextInt(6) == 1)
        {
            //create the health item
            ItemStack healthItemStack = new ItemStack(Material.APPLE);
            ItemMeta healthItemMeta = healthItemStack.getItemMeta();
            healthItemMeta.setDisplayName("Health");
            healthItemStack.setItemMeta(healthItemMeta);

            //spawn the health item
            Item healthItem = location.getWorld().dropItem(location, healthItemStack);
            healthItem.setCustomName("Health");
            healthItem.setCustomNameVisible(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onHealthPickUp(PlayerAttemptPickupItemEvent event)
    {
        Player player = event.getPlayer();
        if (player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
        {
            return;
        }
        ItemStack itemStack = event.getItem().getItemStack();
        String name = itemStack.getDisplayName();
        if (name.equals("Health") && itemStack.getType() == Material.APPLE)
        {
            player.addPotionEffect(PotionEffectType.HEAL.createEffect(1, 1));
            player.playSound(player.getLocation(), Sound.ENTITY_WANDERING_TRADER_DRINK_POTION,300f, 1f);
            event.getItem().remove();
            event.setCancelled(true);

        }
    }
}
